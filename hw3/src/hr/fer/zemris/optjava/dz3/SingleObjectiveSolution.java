package hr.fer.zemris.optjava.dz3;

public class SingleObjectiveSolution {
	
	public double fitness;
	public double value;
	
	public SingleObjectiveSolution() {
	}
	
	public int compareTo(SingleObjectiveSolution that) {
		return Double.compare(this.fitness, that.fitness);
	}

}
