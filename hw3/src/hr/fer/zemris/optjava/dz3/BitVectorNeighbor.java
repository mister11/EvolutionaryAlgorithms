package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class BitVectorNeighbor implements INeighborhood<BitVectorSolution> {

	private Random r;
	
	public BitVectorNeighbor() {
		this.r = new Random();
	}
	
	@Override
	public BitVectorSolution randomNeighbor(BitVectorSolution vector) {
		int size = vector.bits.length;
		int changeIndex = r.nextInt(size);
		BitVectorSolution neighbor = new BitVectorSolution(size);
		for(int i=0; i < size; i++) {
			neighbor.bits[i] = vector.bits[i];
			if(i == changeIndex) {
				if(vector.bits[i] == 0) {
					neighbor.bits[i] = 1;
				} else {
					neighbor.bits[i] = 0;
				}
			}
		}
		return neighbor;
	}

	
}
