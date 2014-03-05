package hr.fer.zemris.optjava.dz4.part2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class BoxFilling {

	private static int maxHeight;
	private static int[] weights;
	
	public static void main(String[] args) {
		if(args.length != 8) {
			System.err.println("You have to enter 8 paramas. 7 from assignment and one more for mutation.");
			return;
		}
		Random r = new Random();
		
		String path = args[0];
		parse(path);
		
		boolean p;
		if(args[4].equals("false")) {
			p = false;
		} else if(args[4].equals("true")) {
			p = true;
		} else {
			p = false;
		}
		int velPop = 0;
		int n = 0, m = 0;
		int maxIters = 0;
		int acceptableLength = 0;
		//mutation probability
		double mutProb = 0;
		
		try {
			velPop = Integer.parseInt(args[1]);
			n = Integer.parseInt(args[2]);
			m = Integer.parseInt(args[3]);
			maxIters = Integer.parseInt(args[5]);
			acceptableLength = Integer.parseInt(args[6]);
			mutProb = Double.parseDouble(args[7]);
		} catch (NumberFormatException e) {
			System.err.println("There was problem with params type.");
		}
		
		Kromosom[] population = createInitialPop(velPop, r);
		Kromosom best = null;
		IFunction fun = new Function(maxHeight);
		evaluatePopulation(population, fun);
		int i;
		for(i=0; i < maxIters; i++) {
			
			Kromosom parent1 = compete(population, r, n, true);
			Kromosom parent2 = compete(population, r, n, true);
			Kromosom child = crossover(parent1, parent2, r, fun);
			mutate(child, mutProb, r);
			child.fitness = fun.valueAt(child.solution);
			Kromosom worstPicked = compete(population, r, m, false);
			if(!p) {
				worstPicked.solution = child.solution;
				worstPicked.fitness = child.fitness;
			} else {
				if(child.fitness < worstPicked.fitness) {
					worstPicked.solution = child.solution;
					worstPicked.fitness = child.fitness;
				}
			}
			
			Arrays.sort(population);
			Kromosom nowBest = population[0];
			if(best == null || (best.fitness > nowBest.fitness)) {
				best = copy(nowBest);
				System.out.println("Number of bins: " + best.fitness);
			}
			if(best.fitness <= acceptableLength) {
				break;
			}
		}
		print(best, i);
		
	}

	private static void print(Kromosom best, int x) {
		int usedBoxes = 1;
		int size = best.solution.length;
		int currentSize = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("\nFinal solution is(" + x + "):\n");
		builder.append("Box" + usedBoxes + ": ");
		for(int i=0; i < size; i++) {
			if(currentSize + best.solution[i] <= maxHeight) {
				currentSize += best.solution[i];
				builder.append(best.solution[i] + ", ");
			} else {
				usedBoxes++;
				currentSize = best.solution[i];
				builder.replace(builder.length() - 2, builder.length(), "");
				builder.append("\nBox" + usedBoxes + ": " + best.solution[i] + ", ");
			}
		}
		builder.replace(builder.length() - 2, builder.length(), "");
		System.out.println(builder.toString());
	}

	private static Kromosom copy(Kromosom forCopy) {
		int size = forCopy.solution.length;
		Kromosom copyTo = new Kromosom(weights.length);
		for(int i=0; i < size; i++) {
			copyTo.solution[i] = forCopy.solution[i];
		}
		copyTo.fitness = forCopy.fitness;
		return copyTo;
	}

	private static void mutate(Kromosom child, double prob, Random r) {
		int size = child.solution.length;
		for(int i=0; i < size; i++) {
			if(r.nextDouble() < prob) {
				int rand = i + r.nextInt(size - i);
				int temp = child.solution[i];
	            child.solution[i] = child.solution[rand];
	            child.solution[rand] = temp;
			}
		}
	}

	//one breakpoint like crossover
	//do rand() broja čita parent1, onda "briše" sve te brojeve u parent 2 i ono
	//što nije pobirsano, kopira u dijete
	private static Kromosom crossover(Kromosom parent1, Kromosom parent2, Random r, IFunction fun) {
		int size = parent1.solution.length;
		int randIndex = r.nextInt(size);
		Kromosom child = new Kromosom(size);
		int[] parent2sol = getSolution(parent2);
		for(int i=0; i < randIndex; i++) {
			int sol = parent1.solution[i];
			child.solution[i] = sol;
			parent2sol = foundAndDelete(parent2sol, sol);
		}
		int a = randIndex;
		for(int i=0; i < size; i++) {
			if(parent2sol[i] == 0) {
				continue;
			}
			child.solution[a++] = parent2sol[i];
		}
		return child;
	}

	private static int[] foundAndDelete(int[] parent2sol, int a) {
		int size = parent2sol.length;
		for(int i=0; i < size; i++) {
			if(parent2sol[i] == a) {
				parent2sol[i] = 0;
				break;
			}
		}
		return parent2sol;
	}


	private static int[] getSolution(Kromosom parent) {
		int size = parent.solution.length;
		int[] sol = new int[size];
		for(int i=0; i < size; i++) {
			sol[i] = parent.solution[i];
		}
		return sol;
	}


	private static Kromosom compete(Kromosom[] population, Random r, int n, boolean flag) {
		Kromosom[] pool = new Kromosom[n];
		int size = population.length;
		boolean[] pickedIndex = new boolean[size];
		int i = 0;
		while(i < n) {
			int index = r.nextInt(size);
			if(pickedIndex[index]) {
				continue;
			}
			pickedIndex[index] = true;
			pool[i++] = population[index];
		}
		Arrays.sort(pool);
		return (flag) ? pool[0] : pool[pool.length - 1];
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
			pop[i] = new Kromosom(weights);
		}
		shuffle(pop, r);
		return pop;
	}

	//http://introcs.cs.princeton.edu/java/stdlib/StdRandom.java.html - source for this
	private static void shuffle(Kromosom[] pop, Random r) {
		int size = pop.length;
		for(int i=0; i < size; i++) {
			int length = pop[i].solution.length;
			for(int j=0; j < length; j++) {
				int rand = j + r.nextInt(length - j);
				int temp = pop[i].solution[j];
	            pop[i].solution[j] = pop[i].solution[rand];
	            pop[i].solution[rand] = temp;
			}
		}
	}

	private static void parse(String filename) {
		int index = filename.indexOf("-");
		String height = filename.substring(index + 1, index + 3);
		try {
			maxHeight = Integer.parseInt(height);
		} catch(NumberFormatException e) {
			System.err.println("Wrong file name.");
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(filename))));
			String line = br.readLine();
			line = line.trim().substring(1, line.length() - 1);
			String[] values = line.split(", ");
			int size = values.length;
			weights = new int[size];
			for(int i=0; i < size; i++) {
				try {
					weights[i] = Integer.parseInt(values[i].trim());
				} catch(NumberFormatException e) {
					System.err.println("There was a problem with weights in " + filename);
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
