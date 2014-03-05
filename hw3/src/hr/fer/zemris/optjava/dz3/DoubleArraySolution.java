package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class DoubleArraySolution extends SingleObjectiveSolution {

	public double[] values;
	
	public DoubleArraySolution(int n) {
		this.values = new double[n];
	}
	
	public DoubleArraySolution newLikeThis() {
		return new DoubleArraySolution(this.values.length);
	}

	public DoubleArraySolution duplicate() {
		DoubleArraySolution vector = new DoubleArraySolution(this.values.length);
		int size = this.values.length;
		for(int i=0; i < size; i++) {
			vector.values[i] = this.values[i];
		}
		return vector;
	}
	
	public void randomize(Random rand, double[] mins, double[] maxs) {
		int size = this.values.length;
		for(int i=0; i < size; i++) {
			this.values[i] = -mins[i] + rand.nextDouble() * (maxs[i] - mins[i]);
		}
	}
	
	public void randomize(Random rand, double mins, double maxs) {
		int size = this.values.length;
		for(int i=0; i < size; i++) {
			this.values[i] = mins + rand.nextDouble() * (maxs - mins);
		}
	}
}
