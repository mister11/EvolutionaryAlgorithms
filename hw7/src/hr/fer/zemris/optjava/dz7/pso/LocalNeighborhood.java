package hr.fer.zemris.optjava.dz7.pso;


public class LocalNeighborhood implements Neighborhood {
	
	private int localSize;
	private double[][] best;
	
	public LocalNeighborhood(int localSize) {
		this.localSize = localSize;
	}
	
	@Override
	public void findBest(Solution[] solutions) {
		int size = solutions.length;
		int dims = solutions[0].weights.length;
		best = new double[size][dims];
		for(int i=0; i < size; i++) {
			int[] localIndices = getLocalIndices(size, i);
			int length = localIndices.length;
			int bestIndex = localIndices[0];
			double bestFit = solutions[localIndices[0]].fitness;
			for(int j=1; j < length; j++) {
				if(solutions[localIndices[j]].fitness < bestFit) {
					bestFit = solutions[localIndices[j]].fitness;
					bestIndex = localIndices[j];
				}
			}
			for(int j=0; j < dims; j++) {
				best[i][j] = solutions[bestIndex].weights[j];
			}
		}
	}

	@Override
	public double[] getBest(int index) {
		return best[index];
	}
	
	private int[] getLocalIndices(int size, int i) {
		int[] localIndices = new int[this.localSize + this.localSize + 1];
		int index = 0;
		int x = i - localSize;
		while(x <= (i + localSize)) {
			//localIndices[index++] = (x < 0) ? (size + x) : x;
			if(x < 0) {
				localIndices[index++] = size + x;
			} else if(x >= 0 && x < size){
				localIndices[index++] = x;
			} else {
				localIndices[index++] = x % size;
			}
			x++;
		}
		return localIndices;
	}

}
