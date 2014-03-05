package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class DoubleArrayNeighbor implements INeighborhood<DoubleArraySolution> {

	private double[] deltas;
	Random rand = new Random();
	
	
	public DoubleArrayNeighbor(double[] deltas) {
		super();
		this.deltas = deltas;
	}
	
	public DoubleArrayNeighbor(double deltas, int n) {
		super();
		this.deltas = new double[n];
		for(int i=0; i < n; i++) {
			this.deltas[i] = deltas;
		}
	}


	@Override
	public DoubleArraySolution randomNeighbor(DoubleArraySolution vector) {
		int size = vector.values.length;
		DoubleArraySolution neighbor = new DoubleArraySolution(size);
		int changeIndex1 = rand.nextInt(size);
		for(int i=0; i < size; i++) {
			if(i != changeIndex1) {
				neighbor.values[i] = vector.values[i];
			} else {
				double r = -this.deltas[i] + rand.nextDouble() * this.deltas[i] * 2;
				neighbor.values[i] = vector.values[i] + r;
			}
		}
		return neighbor;
	}

}
