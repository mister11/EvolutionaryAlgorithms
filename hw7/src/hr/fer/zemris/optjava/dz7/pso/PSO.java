package hr.fer.zemris.optjava.dz7.pso;

import java.util.Random;

import hr.fer.zemris.optjava.dz7.Evaluation;

public class PSO {

	private static final double SPACE = 10.0;
	private static final double START = 1.0;
	private static final double END = 0.1;
	private static final double C1 = 2;
	private static final double C2 = 2;
	private static final int LOW_W = -1;
	private static final int HIGH_W = 1;
	private static Random r = new Random();
	
	private Solution[] solutions;
	private Solution[] bestSols;
	private double[] spaceMin;
	private double[] spaceMax;
	private double[] speedMin;
	private double[] speedMax;
	private double minError;
	private int maxIters;
	private Evaluation evaluation;
	private Neighborhood neighborhood;
	
	public PSO(Evaluation evaluation, int n, int dims, double minError, int maxIters, Neighborhood neighborhood) {
		this.maxIters = maxIters;
		this.minError = minError;
		this.spaceMin = new double[dims];
		this.spaceMax = new double[dims];
		this.speedMin = new double[dims];
		this.speedMax = new double[dims];
		double speed = r.nextDouble() * (2 * SPACE * 0.1);
		for(int i=0; i < dims; i++) {
			this.spaceMin[i] = -SPACE;
			this.spaceMax[i] = SPACE;
			this.speedMin[i] = -speed;
			this.speedMax[i] = speed;
		}
		this.evaluation = evaluation;
		this.solutions = new Solution[n];
		this.bestSols = new Solution[n];
		for(int i=0; i < n; i++) {
			solutions[i] = new Solution(dims);
			bestSols[i] = new Solution(dims);
			for(int j=0; j < dims; j++) {
				solutions[i].weights[j] = LOW_W + r.nextDouble() * (HIGH_W - LOW_W);
				solutions[i].velocity[j] = this.spaceMin[j] + r.nextDouble() * (this.speedMax[j] - this.speedMin[j]);
				bestSols[i].weights[j] = bestSols[i].weights[j];
				bestSols[i].velocity[j] = bestSols[i].velocity[j];
			}
			solutions[i].fitness = evaluation.evaluate(solutions[i].weights);
			bestSols[i].fitness = solutions[i].fitness;
		}
		this.neighborhood = neighborhood;
	}

	public Solution run() {
		Solution sol = null;
		for(int iter=0; iter < maxIters; iter++) {
			this.neighborhood.findBest(bestSols);
			double w = START + ((END - START) / maxIters) * iter;
			int size = solutions.length;
			for(int i=0; i < size; i++) {
				double[] best = this.neighborhood.getBest(i);
				int length = best.length;
				for(int j=0; j < length; j++) {
					solutions[i].velocity[j] = 
						w * solutions[i].velocity[j] + 
						C1 * r.nextDouble() * (bestSols[i].weights[j] - solutions[i].weights[j]) + 
						C2 * r.nextDouble() * (best[j] - solutions[i].weights[j]);
					if(solutions[i].velocity[j] < this.speedMin[j]) {
						solutions[i].velocity[j] = this.speedMin[j];
					}
					if(solutions[i].velocity[j] > this.speedMax[j]) {
						solutions[i].velocity[j] = this.speedMax[j];
					}
					solutions[i].weights[j] += solutions[i].velocity[j];
				}
				
			}
			for(int i=0; i < size; i++) {
				solutions[i].fitness = this.evaluation.evaluate(solutions[i].weights);
				if(solutions[i].fitness < bestSols[i].fitness) {
					bestSols[i].fitness = solutions[i].fitness;
					int lenght = solutions[i].weights.length;
					for(int j=0; j < lenght; j++) {
						bestSols[i].weights[j] = solutions[i].weights[j];
					}
				}
			}
			sol = findBest();
			if(Double.compare(sol.fitness, minError) < 0) {
				break;
			}
			if(iter % 10 == 0) {
				System.out.println("Iteration: " + iter);
				System.out.println("Error: " + sol.fitness);
			}
		}
		System.out.println("\n\nEND");
		System.out.println("Error: " + sol.fitness);
		return sol;
	}

	private Solution findBest() {
		int bestIndex = 0;
		double bestFit = this.bestSols[0].fitness;
		int size = bestSols.length;
		for(int i=1; i < size; i++) {
			if(bestSols[i].fitness < bestFit) {
				bestFit = bestSols[i].fitness;
				bestIndex = i;
			}
		}
		return bestSols[bestIndex];
	}
}
