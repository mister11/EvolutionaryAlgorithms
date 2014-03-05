package hr.fer.zemris.optjava.dz5.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

	private static final int MIN_POP_SIZE = 4;
	private static final int MAX_POP_SIZE = 700;
	private static final int MAX_EFFORT = 1000;
	private static final double START_INIT_FACTOR = 0.001;
	private static final double INIT_STEP = 0.001;
	private static final double STEP = 0.001;
	private static final int K_TOUR = 5;
	private static final double MUT_PROB = 0.02;
	//true ako radimo za oba roditelja k-tour, false ako za jednog radimo 
	//k-tour, a drugog biramo random
	private static final boolean isPureTour = false;
	
	public static void main(String[] args) {
		Random r = new Random();
		int n = Integer.parseInt(args[0]);
		IFunction function = new Function();
		ISelection selection = new TournamentSelection();
		ICrossover crossover = new TwoBreakpointCrossover();
		ICompFactor factor = new CompFactorOne(START_INIT_FACTOR, INIT_STEP, STEP);
		List<Kromosom> population = createInitPopulation(n, r);
		evaluatePopulation(population, function);
		int x = 0;
		while(true) {
			System.out.println(x++);
			int createdChildren = 0;
			List<Kromosom> newPopulation = new ArrayList<>();
			do {
				Kromosom parent1;
				Kromosom parent2;
				if(isPureTour) {
					parent1 = selection.select(population, K_TOUR, r);
					parent2 = selection.select(population, K_TOUR, r);
				} else {
					parent1 = selection.select(population, K_TOUR, r);
					parent2 = population.get(r.nextInt(population.size()));
				}
				Kromosom child = crossover.crossover(parent1, parent2, r, function);
				createdChildren++;
				mutate(child, r);
				child.fitness = function.valueAt(child.solution);
				double compFactor = 0.5;
				//System.out.println("Factor " + compFactor);
				double level = getLevel(parent1, parent2, compFactor);
				if(child.fitness > level) {
					if(notExists(child, newPopulation)) {
						newPopulation.add(child);
					}
				}
				if(newPopulation.size() == MAX_POP_SIZE) {
					break;
				}
			} while(createdChildren < MAX_EFFORT);
			System.out.println("Size " + newPopulation.size());
			if(newPopulation.size() < MIN_POP_SIZE) {
				break;
			}
			printBest(population, x);
			population = new ArrayList<>(newPopulation);
		}
		printBest(population, x);
		
	}

	private static void printBest(List<Kromosom> population, int x) {
		double bestFitness = population.get(0).fitness;
		Kromosom bestKromosom = population.get(0);
		for(Kromosom k : population) {
			if(k.fitness > bestFitness) {
				bestFitness = k.fitness;
				bestKromosom = k;
			}
		}
		//System.out.println("Best found( " + x + " ): " + Arrays.toString(bestKromosom.solution));
		System.out.println("Fitness: " + bestFitness);
	}

	private static boolean notExists(Kromosom child,
			List<Kromosom> population) {
		List<Kromosom> sameFitness = new ArrayList<>();
		for(Kromosom k : population) {
			if(child.equals(k)) {
				sameFitness.add(k);
			}
		}
		if(sameFitness.isEmpty()) {
			return true;
		} else {
			return deepCheck(child, sameFitness);
		}
	}

	private static boolean deepCheck(Kromosom child, List<Kromosom> sameFitness) {
		boolean isSame;
		for(Kromosom k : sameFitness) {
			isSame = true;
			int size = k.solution.length;
			for(int i=0; i < size; i++) {
				if(child.solution[i] != k.solution[i]) {
					isSame = false;
					break;
				}
			}
			if(isSame) {
				return false;//jednaki kromosom već postoji u populaciji
			}
		}
		return true;//kromosom zbilja ne postoji u trenutnoj populaciji
	}

	private static double getLevel(Kromosom parent1, Kromosom parent2, double compFactor) {
		if(parent1.fitness > parent2.fitness) {
			return parent2.fitness + Math.abs(parent1.fitness - parent2.fitness) * compFactor;
		} else {
			return parent1.fitness + Math.abs(parent2.fitness - parent1.fitness) * compFactor;
		}
	}

	private static void mutate(Kromosom child, Random r) {
		int size = child.solution.length;
		for(int i=0; i < size; i++) {
			if(r.nextDouble() < MUT_PROB) {
				child.solution[i] = !child.solution[i];
			}
		}
	}

	private static void evaluatePopulation(List<Kromosom> population,
			IFunction function) {
		for(Kromosom k : population) {
			k.fitness = function.valueAt(k.solution);
		}
	}

	private static List<Kromosom> createInitPopulation(int n, Random r) {
		List<Kromosom> population = new ArrayList<>();
		for(int i=0; i < MAX_POP_SIZE; i++) {
			Kromosom k = new Kromosom(n, r);
			population.add(k);
		}
		return population;
	}

}
