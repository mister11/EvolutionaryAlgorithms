package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OffspringSelection {
	
	private static final double SUCC_RATIO = 0.4;
	private static final double MAX_SEL_PRES = 1.3;
	private static final double COMP_FACTOR = 0.5;
	private static final int K_TOUR = 5;
	private static final double MUT_PROB = 0.02;
	
	
	private List<Kromosom> population;
	private ISelection selection;
	private ICrossover crossover;
	private IFunction function;
	private int size;
	private Random r;
	private int maxGenerations;
	
	public OffspringSelection(List<Kromosom> population, ISelection selection, 
			ICrossover crossover, IFunction function, Random r, int maxGenerations) {
		this.size = population.size();
		this.population = population;
		this.function = function;
		this.crossover = crossover;
		this.selection = selection;
		this.r = r;
		this.maxGenerations = maxGenerations;
	}
	
	public void run() {
		int x = 0;
		double actSelPres = 1;
		
		//spremam glavnu da ju mogu updateati
		List<Kromosom> workingPop = new ArrayList<>(this.population);
		
		while(x++ < this.maxGenerations && actSelPres < MAX_SEL_PRES) {
			List<Kromosom> nextPopulation = new ArrayList<>();
			List<Kromosom> poolForIdiots = new ArrayList<>();
			
			int succPopSize = (int) (SUCC_RATIO * this.size);
			int maxEffort = (int) (this.size * MAX_SEL_PRES);
			while(nextPopulation.size() < succPopSize && 
					(nextPopulation.size() + poolForIdiots.size()) < maxEffort) {
			
				Kromosom parent1 = this.selection.select(workingPop, K_TOUR, r);
				Kromosom parent2 = this.selection.select(workingPop, K_TOUR, r);
				
				Kromosom child = this.crossover.crossover(parent1, parent2, r);
				
				mutate(child, r);
				child.fitness = function.valueAt(child.locationPermutation);
				
				//granica prihvaćanja djeteta
				double level = getLevel(parent1, parent2, COMP_FACTOR);
				if(child.fitness < level) {
					nextPopulation.add(child);
				} else {
					poolForIdiots.add(child);
				}
			}
			actSelPres = (nextPopulation.size() + poolForIdiots.size()) / (double)workingPop.size();
			while(nextPopulation.size() < workingPop.size()) {
				if(poolForIdiots.isEmpty()) {
					Kromosom parent1 = this.selection.select(workingPop, K_TOUR, r);
					Kromosom parent2 = this.selection.select(workingPop, K_TOUR, r);
					Kromosom child = this.crossover.crossover(parent1, parent2, r);
					mutate(child, r);
					child.fitness = function.valueAt(child.locationPermutation);
					nextPopulation.add(child);
				} else {
					nextPopulation.add(poolForIdiots.get(r.nextInt(poolForIdiots.size())));
				}
			}
			workingPop = new ArrayList<>(nextPopulation);
		}
		//update glavne populacije
		for(int i=0; i < size; i++) {
			this.population.get(i).fitness = workingPop.get(i).fitness;
			this.population.get(i).locationPermutation = workingPop.get(i).locationPermutation;
		}
	}


	private static double getLevel(Kromosom parent1, Kromosom parent2, double compFactor) {
		if(parent1.fitness < parent2.fitness) {
			return parent2.fitness + Math.abs(parent1.fitness - parent2.fitness) * compFactor;
		} else {
			return parent1.fitness + Math.abs(parent2.fitness - parent1.fitness) * compFactor;
		}
		
		
	}

	
	private static void mutate(Kromosom child, Random r) {
		int size = child.locationPermutation.length;
		for(int i=0; i < size; i++) {
			if(r.nextDouble() < MUT_PROB) {
				int rand = r.nextInt(size);
				int temp = child.locationPermutation[i];
	            child.locationPermutation[i] = child.locationPermutation[rand];
	            child.locationPermutation[rand] = temp;
			}
		}
		
	}
}
