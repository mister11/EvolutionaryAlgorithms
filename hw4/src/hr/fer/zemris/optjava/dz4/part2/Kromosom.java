package hr.fer.zemris.optjava.dz4.part2;


public class Kromosom implements Comparable<Kromosom> {
	
	int[] solution;
	int fitness;
	
	public Kromosom(int n) {
		this.solution = new int[n];
		this.fitness = 0;
	}
	
	public Kromosom(int[] weights) {
		this.fitness = 0;
		int size = weights.length;
		this.solution = new int[size];
		for(int i=0; i < size; i++) {
			this.solution[i] = weights[i];
		}
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
