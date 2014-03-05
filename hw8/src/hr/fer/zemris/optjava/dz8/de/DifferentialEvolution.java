package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.Evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class DifferentialEvolution {

	private static final int LOW_W = -1;
	private static final int HIGH_W = 1;
	private static final double F = 0.5;
	private static final double CR = 0.6;

	private static Random r = new Random();

	private List<Solution> solutions;
	private Evaluation evaluation;
	private ICrossover crossover;
	private int size;
	private double minError;
	private int maxIters;

	public DifferentialEvolution(Evaluation evaluation, ICrossover crossover,
			int n, int dims, double minError, int maxIters) {
		this.solutions = new ArrayList<Solution>();
		this.evaluation = evaluation;
		this.crossover = crossover;
		this.size = n;
		this.minError = minError;
		this.maxIters = maxIters;
		for (int i = 0; i < n; i++) {
			Solution s = new Solution(dims);
			for (int j = 0; j < dims; j++) {
				s.weights[j] = LOW_W + r.nextDouble() * (HIGH_W - LOW_W);
			}
			this.solutions.add(s);
		}
		evaluateSolutions();
	}

	public Solution run() {
		Solution best = findBest();
		for (int iter = 0; iter < this.maxIters; iter++) {
			List<Solution> trials = new ArrayList<Solution>();
			for (int i = 0; i < this.size; i++) {
				List<Integer> indices = new ArrayList<Integer>();
				indices.add(i);
				Solution target = this.solutions.get(i);
				int b, c;
				
				b = r.nextInt(this.size);
				c = r.nextInt(this.size);
				
				while(indices.contains(b)) {
					b = r.nextInt(this.size);
				}
				indices.add(b);
				while(indices.contains(c)) {
					c = r.nextInt(this.size);
				}
				RealVector bestBase = new ArrayRealVector(best.weights);
				RealVector first = new ArrayRealVector(
						this.solutions.get(b).weights);
				RealVector second = new ArrayRealVector(
						this.solutions.get(c).weights);
				RealVector diff = first.subtract(second);
				RealVector toAdd = diff.mapMultiply(F);
				RealVector mutant = bestBase.add(toAdd);
				RealVector trial = this.crossover.crossover(
						new ArrayRealVector(target.weights), mutant, CR);
				Solution trialSol = new Solution(trial.toArray());
				trialSol.fitness = this.evaluation.evaluate(trialSol.weights);
				trials.add(trialSol);
			}
			for(int i=0; i < size; i++) {
				Solution trial = trials.get(i);
				Solution curr = this.solutions.get(i);
				if (trial.fitness <= curr.fitness) {
					this.solutions.set(i, trial);
					if(trial.fitness < best.fitness) {
						best = copy(trial);
					}
				}
			}
			if (best.fitness < minError) {
				break;
			}
			if (iter % 10 == 0) {
				System.out.println("Iteration: " + iter);
				System.out.println("Error: " + best.fitness);
			}
		}
		System.out.println("\n\nEND");
		System.out.println("Error: " + best.fitness);
		return best;
	}

	private Solution copy(Solution trial) {
		Solution s = new Solution(trial.weights);
		s.fitness = trial.fitness;
		return s;
	}

	private Solution findBest() {
		int bestIndex = 0;
		double bestFit = this.solutions.get(0).fitness;
		for (int i = 1; i < this.size; i++) {
			if (this.solutions.get(i).fitness < bestFit) {
				bestFit = this.solutions.get(i).fitness;
				bestIndex = i;
			}
		}
		return this.solutions.get(bestIndex);
	}

	private void evaluateSolutions() {
		for (int i = 0; i < this.size; i++) {
			Solution s = this.solutions.get(i);
			s.fitness = this.evaluation.evaluate(s.weights);
		}
	}
}
