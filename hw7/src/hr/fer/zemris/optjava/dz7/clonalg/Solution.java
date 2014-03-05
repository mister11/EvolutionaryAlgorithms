package hr.fer.zemris.optjava.dz7.clonalg;

import java.util.Arrays;

public class Solution implements Comparable<Solution> {
	
	public double[] weights;
	double fitness;
	
	public Solution(int n) {
		this.weights = new double[n];
	}

	@Override
	public int compareTo(Solution o) {
		if(this.fitness < o.fitness) {
			return -1;
		}
		if(this.fitness > o.fitness) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(weights) + "\n" + fitness;
	}
}
