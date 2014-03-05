package hr.fer.zemris.trisat;


public class MutableBitVector extends BitVector {
	
	private boolean[] bitVector;
	
	//preuzima se dobiveno polje
	//možda bi se mogla napraviti i kopija, ali mislim da nije nužno
	public MutableBitVector(boolean... bits) { 
		super(bits);
		this.bitVector = bits;
	}

	public MutableBitVector(int n) {
		super(n);
	}

	public void set(int index, boolean value) {
		if(index < 0 || index >= bitVector.length) {
			throw new IndexOutOfBoundsException("Index should be between 0 and " + bitVector.length);
		}
		this.bitVector[index] = value;
	}
}