package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;

public class Kromosom implements Comparable<Kromosom>{
	
	double[] solution;
	double fitness;
	
	public Kromosom(int n) {
		this.solution = new double[n];
		this.fitness = 0;
	}
	
	public Kromosom(int n, Random r, int min, int max) {
		this.solution = new double[n];
		for(int i=0; i < n; i++) {
			this.solution[i] = min + r.nextDouble() * (max - min);
		}
		this.fitness = 0;
	}

	@Override
	public int compareTo(Kromosom o) {
		if(this.fitness < o.fitness) {
			return -1;
		}
		if(this.fitness > o.fitness) {
			return 1;
		}
		return 0;
	}

}
