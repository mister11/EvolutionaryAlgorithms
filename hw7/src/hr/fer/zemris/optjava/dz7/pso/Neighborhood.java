package hr.fer.zemris.optjava.dz7.pso;

public interface Neighborhood {
	
	void findBest(Solution[] solutions);

	double[] getBest(int index);
}
