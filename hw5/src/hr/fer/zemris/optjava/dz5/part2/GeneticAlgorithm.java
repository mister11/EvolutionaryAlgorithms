package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

	private static final int MAX_GENERATIONS = 10000;
	
	
	public static void main(String[] args) {
		if(args.length != 3) {
			System.err.println("Must have 3 params");
			return;
		}
		Data data = new Data(args[0]);
		IFunction function = new Function(data);
		ISelection selection = new TournamentSelection();
		ICrossover crossover = new CustomCrossover();
		Random r = new Random();
		int popSize = Integer.parseInt(args[1]);
		int numOfSubPops = Integer.parseInt(args[2]);
		int subPopSize = popSize / numOfSubPops;
		
		
		List<Kromosom> population = createInitPopulation(popSize, r, data.getN());
		evaluatePopulation(population, function);
		List<List<Kromosom>> subPops = createSubPops(population, numOfSubPops, subPopSize);
		Kromosom best = null;
		while(numOfSubPops > 1) {
			System.out.println("Number od subpops: " + numOfSubPops);
			//brojač podpopulacije
			int o = 0;
			for(List<Kromosom> p : subPops) {
				OffspringSelection os = new OffspringSelection(p, selection, crossover, function, r, MAX_GENERATIONS);
				os.run();
				if(best == null) {
					best = findBest(p);
				} else {
					Kromosom tempBest = findBest(p);
					best = (tempBest.fitness < best.fitness) ? tempBest : best; 
				}
				System.out.print("Sub " + (++o) + "->" + best.fitness + "\t");
			}
			System.out.println("\n");
			numOfSubPops--;
			subPopSize = popSize / numOfSubPops;
			subPops = createSubPops(population, numOfSubPops, subPopSize);
		}
		OffspringSelection os = new OffspringSelection(subPops.get(0), selection, crossover, function, r, MAX_GENERATIONS);
		os.run();
		Kromosom tempBest = findBest(subPops.get(0));
		System.out.println("\n\nBest found: " + Arrays.toString(tempBest.locationPermutation));
		System.out.println("Error: " + tempBest.fitness);
	}


	private static Kromosom findBest(List<Kromosom> population) {
		double bestFitness = population.get(0).fitness;
		Kromosom bestKromosom = population.get(0);
		for(Kromosom k : population) {
			if(k.fitness < bestFitness) {
				bestFitness = k.fitness;
				bestKromosom = k;
			}
		}
		return bestKromosom;
	}


	private static void evaluatePopulation(
			List<Kromosom> population,
			IFunction function) {
		for(Kromosom k : population) {
			k.fitness = function.valueAt(k.locationPermutation);
		}
	}


	private static List<List<Kromosom>> createSubPops(List<Kromosom> population, int numOfSubPops, int subPopSize) {
		List<List<Kromosom>> subPops = new ArrayList<List<Kromosom>>(); 
		int popIndex = 0;
		for(int i=0; i < numOfSubPops; i++) {
			subPops.add(new ArrayList<Kromosom>());
			for(int j=0; j < subPopSize; j++) {
				subPops.get(i).add(population.get(popIndex++));
			}
		}
		return subPops;
	}


	private static List<Kromosom> createInitPopulation(int popSize, Random r, int n) {
		List<Kromosom> pop = new ArrayList<>();
		for(int i=0; i < popSize; i++) {
			Kromosom k = new Kromosom(n);
			pop.add(k);
		}
		shuffle(pop, r);
		return pop;
	}

	//http://introcs.cs.princeton.edu/java/stdlib/StdRandom.java.html - source for this
	private static void shuffle(List<Kromosom> pop, Random r) {
		int size = pop.size();
		for(int i=0; i < size; i++) {
			int length = pop.get(i).locationPermutation.length;
			for(int j=0; j < length; j++) {
				int rand = j + r.nextInt(length - j);
				int temp = pop.get(i).locationPermutation[j];
				pop.get(i).locationPermutation[j] = pop.get(i).locationPermutation[rand];
				pop.get(i).locationPermutation[rand] = temp;
			}
		}
	}
}
