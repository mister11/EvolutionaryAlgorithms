package hr.fer.zemris.optjava.dz4.part1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {

	private static final int NUM_OF_VARS = 6;
	private static final int MIN = -10;
	private static final int MAX = 10;
	private static final double ALPHA = 0.5;//heuristic lab default value
	
	private static double[][] matrix;
	private static double[] y;
	
	/**
	 * Algoritam radi najbolje za turnirsku selekciju sa oko 100 jedinki(možda je više bolje, ali 
	 * nisam primjetio neku veliku razliku, i sa 100 i sa 500 dođe do otprilike iste krajnje greške), 
	 * s malim sigma(zanimljivo, s 0.01 radi super, a s 0.015 ili 0.005 ne toliko dobro)
	 * 
	 */
	public static void main(String[] args) {
		if(args.length != 5) {
			System.err.println("Wrong number of params. You have to enter 5 params.");
			return;
		}
		
		int velPop = 0;
		double errorThreshold = 0;
		int maxIters = 0;
		double sigma = 0;
		
		try {
			velPop = Integer.parseInt(args[0]);
			errorThreshold = Double.parseDouble(args[1]);
			maxIters = Integer.parseInt(args[2]);
			sigma = Double.parseDouble(args[4]);
		} catch (NumberFormatException e) {
			System.out.println("First param must be integer, second one double and third and fifth one integers.");
			return;
		}
		String selection = args[3];
		
		String file = "zad-prijenosna.txt";
		parse(file);
		IFunction fun = new Function(matrix, y);
		
		Random r = new Random();
		Kromosom[] population = createInitialPop(velPop, r);
		evaluatePopulation(population, fun);
		Kromosom[] newPop = createInitialPop(velPop, null);
		
		for(int i=0; i < maxIters; i++) {
			Arrays.sort(population);
			copyToNew(population[0], newPop[0]);
			copyToNew(population[1], newPop[1]);
			for(int j=2; j < velPop; j++) {
				Kromosom parent1 = new Kromosom(NUM_OF_VARS);
				Kromosom parent2 = new Kromosom(NUM_OF_VARS);
				if(selection.startsWith("roulette")) {
					parent1 = spinTheWheel(population, r, population[population.length - 1]);
					parent2 = spinTheWheel(population, r, population[population.length - 1]);
//					while(Double.compare(parent1.fitness, parent2.fitness) == 0) {
//						parent2 = spinTheWheel(population, r, population[population.length - 1]);
//					}
				} else if(selection.startsWith("tour")) {
					int k = 0;
					try {
						k = Integer.parseInt(selection.substring(selection.indexOf(":") + 1));
					} catch (NumberFormatException e) {
						System.err.println("Tournament parameter must be like: tournament:n!");
						return;
					}
					parent1 = compete(population, r, k);
					parent2 = compete(population, r, k);
//					while(Double.compare(parent1.fitness, parent2.fitness) == 0) {
//						parent2 = compete(population, r, k);
//					}
				} else {
					System.out.println("rouletterWheel or tournament:x are allowed");
					return;
				}
				Kromosom child = crossover(parent1, parent2, r);
				mutate(child, sigma, r);
				newPop[j] = child;
			}
			Kromosom[] temp = population;
			population = newPop;
			newPop = temp;
			
			evaluatePopulation(population, fun);
			Kromosom best = new Kromosom(NUM_OF_VARS);
			best.fitness = Double.MAX_VALUE;
			for(int j=0; j < velPop; j++) {
				if(j == 0 || best.fitness > population[j].fitness) {
					best = population[j];
				}
			}
			System.out.println("Best so far: " + Arrays.toString(best.solution));
			double error = fun.valueAt(best.solution);
			System.out.println("Error: " + error);
			
			if(error <= errorThreshold) {
				break;
			}
		}
	}

	private static void mutate(Kromosom child, double sigma, Random r) {
		int size = child.solution.length;
		for(int i=0; i < size; i++) {
			child.solution[i] += r.nextGaussian() * sigma;
		}
	}

	private static Kromosom crossover(Kromosom parent1, Kromosom parent2, Random r) {
		int size = parent1.solution.length;
		Kromosom child = new Kromosom(size);
		for(int i=0; i < size; i++) {
			double cMin = Math.min(parent1.solution[i], parent2.solution[i]);
			double cMax = Math.max(parent1.solution[i], parent2.solution[i]);
			double diff = cMax - cMin;
			double lower = cMin - diff * ALPHA;
			double upper = cMax + diff * ALPHA;
			child.solution[i] = lower + r.nextDouble() * (upper - lower);
		}
		return child;
	}
	
	private static Kromosom compete(Kromosom[] population, Random r, int k) {
		int size = population.length;
		boolean[] pickedIndex = new boolean[size];
		Kromosom[] pool = new Kromosom[k];
		int i = 0;
		while(i < k) {
			int index = r.nextInt(size);
			if(pickedIndex[index]) {
				continue;
			}
			pickedIndex[index] = true;
			pool[i++] = population[index];
		}
		Arrays.sort(pool);
		return pool[0];
	}

	private static Kromosom spinTheWheel(Kromosom[] population, Random r, Kromosom worst) {
		double totalFit = 0;
		int size = population.length;
		for(int i=0; i < size; i++) {
			totalFit += population[i].fitness;
		}
		double probab = r.nextDouble() * (size * worst.fitness - totalFit);
		double total = 0;
		for(int i=0; i < size; i++) {
			total += worst.fitness - population[i].fitness;
			if(probab < total) {
				return population[i];
			}
		}
		return population[size - 1];
	}

	private static void copyToNew(Kromosom original, Kromosom newCopy) {
		int size = original.solution.length;
		for(int i=0; i < size; i++) {
			newCopy.solution[i] = original.solution[i];
		}
	}

	private static void evaluatePopulation(Kromosom[] population, IFunction fun) {
		int size = population.length;
		for(int i=0; i < size; i++) {
			population[i].fitness = fun.valueAt(population[i].solution);
		}
	}

	private static Kromosom[] createInitialPop(int velPop, Random r) {
		Kromosom[] pop = new Kromosom[velPop];
		for(int i=0; i < velPop; i++) {
			if(r != null) {
				pop[i] = new Kromosom(NUM_OF_VARS, r, MIN, MAX);
			} else {
				pop[i] = new Kromosom(NUM_OF_VARS);
			}
		}
		return pop;
	}
	
	private static void parse(String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(filename))));
			int numOfLines = 0;
			int numOfCols = 0;
			while (true) {
				String s = br.readLine();
				if (s == null) {
					break;
				} else if (s.startsWith("#")) {
					continue;
				} else {
					if(numOfCols == 0) {
						numOfCols = s.trim().split(",").length - 1;
					}
					numOfLines++;
				}
			}
			matrix = new double[numOfLines][numOfCols];
			y = new double[numOfLines];
			if (br != null) {
				br.close();
			}
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(filename)),
					"UTF-8"));
			int col = 0;
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				} else if (line.startsWith("#")) {
					continue;
				} else {
					String lineNums = line.trim().substring(1,
							line.length() - 1);
					String[] nums = lineNums.split(",");
					int size = nums.length;
					for (int i = 0; i < size - 1; i++) {
						matrix[col][i] = Double.parseDouble(nums[i].trim());
					}
					y[col] = Double.parseDouble(nums[size - 1].trim());
					col++;
				}
			}
		} catch (IOException e) {
			System.err.println("Error opening file " + filename);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("Error closing reader!");
				}
			}
		}
	}
}
