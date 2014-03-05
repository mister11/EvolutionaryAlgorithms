package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class BitVectorSolution extends SingleObjectiveSolution {
	
	public byte[] bits;
	
	public BitVectorSolution(int n) {
		this.bits = new byte[n];
	}
	
	public BitVectorSolution newLikeThis() {
		return new BitVectorSolution(this.bits.length);
	}

	public BitVectorSolution duplicate() {
		BitVectorSolution vector = new BitVectorSolution(this.bits.length);
		int size = this.bits.length;
		for(int i=0; i < size; i++) {
			vector.bits[i] = this.bits[i];
		}
		return vector;
	}
	
	public void randomize(Random rand) {
		int size = this.bits.length;
		for(int i=0; i < size; i++) {
			if(rand.nextDouble() < 0.5) {
				this.bits[i] = 1;
			} else {
				this.bits[i] = 0;
			}
		}
	}
}
