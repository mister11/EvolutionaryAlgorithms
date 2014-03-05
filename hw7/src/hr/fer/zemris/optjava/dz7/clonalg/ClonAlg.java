package hr.fer.zemris.optjava.dz7.clonalg;

import hr.fer.zemris.optjava.dz7.Evaluation;

import java.util.Arrays;
import java.util.Random;

public class ClonAlg {
	
	private static final double BETA = 1;
	private static final int D = 20;
	private static final int LOW_W = -1;
	private static final int HIGH_W = 1;
	private static Random r = new Random();
	
	private Solution[] solutions;
	private Solution[] clones;
	private int n;
	private Evaluation evaluation;
	private double minError;
	private int maxIters;
	
	public ClonAlg(Evaluation evaluation, int n, int dims, double minError, int maxIters) {
		this.solutions = new Solution[n];
		this.clones = new Solution[n];
		this.evaluation = evaluation;
		this.n = n;
		this.minError = minError;
		this.maxIters = maxIters;
		for(int i=0; i < n; i++) {
			Solution s = new Solution(dims);
			for(int j=0; j < dims; j++) {
				s.weights[j] = LOW_W + r.nextDouble() * (HIGH_W - LOW_W);
			}
			this.solutions[i] = s;
		}
		evaluate(this.solutions);
	}
	public Solution run() {
		Solution sol = null;
		for(int i=0; i < maxIters; i++) {
			//evaluate(this.solutions);
			this.clones = cloning();
			hypermutate();
			evaluate(this.clones);
			this.solutions = select();
			Solution[] newOnes = createNew(this.solutions[0].weights.length);
			evaluate(newOnes);
			addNewOnes(newOnes);
			sol = findBest();
			if(Double.compare(sol.fitness, minError) < 0) {
				break;
			}
			if(i % 10 == 0) {
				System.out.println("Iteration: " + i);
				System.out.println("Error: " + sol.fitness);
			}
		}
		sol = findBest();
		System.out.println("\n\nEND");
		System.out.println("Error: " + sol.fitness);
		return sol;
		
	}
	
	private Solution findBest() {
		int bestIndex = 0;
		double bestFit = this.solutions[0].fitness;
		int size = this.solutions.length;
		for(int i=1; i < size; i++) {
			if(this.solutions[i].fitness < bestFit) {
				bestFit = this.solutions[i].fitness;
				bestIndex = i;
			}
		}
		return this.solutions[bestIndex];
	}
	
	private void addNewOnes(Solution[] newOnes) {
		int size = newOnes.length;
		int index = this.solutions.length - D;
		for(int i=0; i < size; i++) {
			this.solutions[index + i] = newOnes[i];
		}
	}
	
	private Solution[] createNew(int dims) {
		Solution[] newOnes = new Solution[D];
		for(int i=0; i < D; i++) {
			Solution s = new Solution(dims);
			for(int j=0; j < dims; j++) {
				s.weights[j] = LOW_W + r.nextDouble() * (HIGH_W - LOW_W);
			}
			newOnes[i] = s;
		}
		return newOnes;
	}
	private Solution[] select() {
		Solution[] solutions = new Solution[n];
		Arrays.sort(this.clones);
		for(int i=0; i < n; i++) {
			int length = clones[i].weights.length;
			solutions[i] = new Solution(length);
			for(int j=0; j < length; j++) {
				solutions[i].weights[j] = this.clones[i].weights[j];
			}
			solutions[i].fitness = this.clones[i].fitness;
		}
		return solutions;
	}
	private void hypermutate() {
		int size = this.clones.length;
		for(int i=0; i < size; i++) {
			Solution forMutation = this.clones[i];
			int length = forMutation.weights.length;
			//na neki način ovisi o dobroti klona...
			//nije najbolja ovisnost, ali oni na kraju će imati veću šansu za mutaciju
			//a oni na početku manju
			double x = (double)length / 2;
			double y = 1 - Math.exp(-(i+1) / (double) length);
			int numOfMutation = (int) (x * y);
			for(int j=0; j < numOfMutation; j++) {
				int index = r.nextInt(length);
				forMutation.weights[index] += LOW_W + r.nextDouble() * (HIGH_W - LOW_W);
//				int a = r.nextInt(length);
//				int b = r.nextInt(length);
//				while(a == b) {
//					b = r.nextInt(length);
//				}
//				double temp = forMutation.weights[a];
//				forMutation.weights[a] = forMutation.weights[b];
//				forMutation.weights[b] = temp;
			}
		}
	}
	private Solution[] cloning() {
		int size = solutions.length;
		int totalClones = 0;
		for(int i=1; i <= size; i++) {
			totalClones += (int) ((BETA * n) / (double)i);
		}
		Solution[] clones = new Solution[totalClones];
		Arrays.sort(solutions);
		int x = 0;
		for(int i=1; i <= size; i++) {
			Solution oneForCloning = solutions[i-1];
			int numOfClones = (int) ((BETA * n) / (double)i);
			for(int j=0; j < numOfClones; j++) {
				clones[x++] = copy(oneForCloning);
			}
		}
		
		return clones;
	}
	private Solution copy(Solution oneForCloning) {
		Solution s = new Solution(oneForCloning.weights.length);
		int size = s.weights.length;
		for(int i=0; i < size; i++) {
			s.weights[i] = oneForCloning.weights[i];
		}
		return s;
	}
	private void evaluate(Solution[] solutions) {
		int size = solutions.length;
		for(int i=0; i < size; i++) {
			solutions[i].fitness = this.evaluation.evaluate(solutions[i].weights);
		}
	}
	

}
