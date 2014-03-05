package hr.fer.zemris.optjava.dz8.de;

import java.util.Arrays;


public class Solution implements Comparable<Solution> {

	public double[] weights;
	public double fitness;
	
	public Solution(int n) {
		super();
		this.weights = new double[n];
		this.fitness = 0;
	}
	
	public Solution(double[] weights) {
		int size = weights.length;
		this.weights = new double[size];
		for(int i=0; i < size; i++) {
			this.weights[i] = weights[i];
		}
		this.fitness = 0;
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
