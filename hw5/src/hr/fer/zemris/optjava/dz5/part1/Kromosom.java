package hr.fer.zemris.optjava.dz5.part1;

import java.util.Arrays;
import java.util.Random;

public class Kromosom implements Comparable<Kromosom> {

	boolean[] solution;
	double fitness;
	
	public Kromosom(int n, Random r) {
		this.solution = new boolean[n];
		for(int i=0; i < n; i++) {
			this.solution[i] = (r.nextDouble() < 0.5) ? true : false;
		}
		this.fitness = 0;
	}
	
	public Kromosom(int n) {
		this.solution = new boolean[n];
		this.fitness = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		Kromosom that = (Kromosom) obj;
		return this.fitness == that.fitness;
		
	}

	@Override
	public int compareTo(Kromosom o) {
		return Double.compare(this.fitness, o.fitness);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.solution);
	}
}
