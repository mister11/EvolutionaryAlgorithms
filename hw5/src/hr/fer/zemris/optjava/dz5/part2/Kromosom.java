package hr.fer.zemris.optjava.dz5.part2;


public class Kromosom implements Comparable<Kromosom> {

	int[] locationPermutation;
	double fitness;
	
	public Kromosom(int n) {
		this.locationPermutation = new int[n];
		for(int i=0; i < n; i++) {
			this.locationPermutation[i] = i;
		}
		this.fitness = 0;
	}
	
	public Kromosom(int[] solutions) {
		this.locationPermutation = new int[solutions.length];
		for(int i=0; i < solutions.length; i++) {
			this.locationPermutation[i] = solutions[i];
		}
		this.fitness = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		Kromosom that = (Kromosom) obj;
		return this.fitness == that.fitness;
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

