package hr.fer.zemris.trisat;

import java.util.Random;

public class BitVector {
	
	private boolean[] bitVector;
	
	public BitVector(Random r, int numOfBits) {
		this.bitVector = new boolean[numOfBits];
		for(int i=0; i < numOfBits; i++) {
			if(r.nextInt() < 0.5) {
				this.bitVector[i] = false;
			} else {
				this.bitVector[i] = true;
			}
		}
	}
	
	public BitVector(boolean ... bits) {
		this.bitVector = bits;
	}
	
	public BitVector(int n) {
		this.bitVector = new boolean[n];
	}

	public boolean get(int index) {
		if(index < 0 || index >= this.bitVector.length) {
			throw new IndexOutOfBoundsException("Index should be between 0 and " + this.bitVector.length);
		}
		return this.bitVector[index];
	}
	
	public int getSize() {
		return this.bitVector.length;
	}
	
	@Override
	public String toString() {
		int size = getSize();
		StringBuilder sb = new StringBuilder(size);
		for(int i=0; i < size; i++) {
			if(bitVector[i]) {
				sb.append(1);
			} else {
				sb.append(0);
			}
		}
		return sb.toString();
	}
	
	public MutableBitVector copy() {
		boolean[] copy = new boolean[this.bitVector.length];
		for(int i=0; i < copy.length; i++) {
			copy[i] = this.bitVector[i];
		}
		return new MutableBitVector(copy);
	}
	
}
