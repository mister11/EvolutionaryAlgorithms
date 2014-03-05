package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {
	
	private BitVector bitVector;
	
	public BitVectorNGenerator(BitVector assignment) {
		this.bitVector = assignment;
	}

	@Override
	public Iterator<MutableBitVector> iterator() {
		//pametan način da se izbjegne pisanje vlastitog iteratora :)
		return Arrays.asList(createNeighborhood()).iterator();
	}

	public MutableBitVector[] createNeighborhood() {
		MutableBitVector[] neighbors = new MutableBitVector[this.bitVector.getSize()];
		int size = neighbors.length;
		for(int i=0; i < size; i++) {
			MutableBitVector initial = this.bitVector.copy();
			initial.set(i, !this.bitVector.get(i));
			neighbors[i] = initial;
		}
		return neighbors;
	}
	
}