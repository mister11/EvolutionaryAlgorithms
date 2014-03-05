package hr.fer.zemris.optjava.dz7.pso;

public class Solution {
	
	public double[] weights;
	double[] velocity;
	double fitness;
	
	public Solution(int n) {
		this.weights = new double[n];
		this.velocity = new double[n];
	}

	public Solution(double[] weights) {
		copy(weights);
	}

	private void copy(double[] weights) {
		int size = weights.length;
		for(int i=0; i < size; i++) {
			this.weights[i] = weights[i];
		}
	}
}
