package hr.fer.zemris.optjava.dz7.pso;

public class GlobalNeighborhood implements Neighborhood {

	private double[] best;
	
	public GlobalNeighborhood() {
	}
	
	@Override
	public void findBest(Solution[] solutions) {
		int size = solutions.length;
		double bestFit = solutions[0].fitness;
		int bestIndex = 0;
		for(int i=1; i < size; i++) {
			if(solutions[i].fitness < bestFit) {
				bestFit = solutions[i].fitness;
				bestIndex = i;
			}
		}
		this.best = copy(solutions[bestIndex]);
	}

	@Override
	public double[] getBest(int index) {
		return this.best;
	}
	
	private double[] copy(Solution solution) {
		int size = solution.weights.length;
		double[] best = new double[size];
		for(int i=0; i < size; i++) {
			best[i] = solution.weights[i];
		}
		return best;
	}

}
