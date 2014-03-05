package hr.fer.zemris.trisat;

public class Clause {
	
	private int[] indices;
	
	public Clause(int[] indices) {
		this.indices = indices;
	}

	public int getSize() {
		return this.indices.length;
	}

	public int getLiteral(int index) {
		if(index < 0 || index >= getSize()) {
			throw new IndexOutOfBoundsException("Index should be between 0 and " + getSize());
		}
		return this.indices[index];
	}

	public boolean isSatisfied(BitVector assignment) {
		int size = this.indices.length;
		for(int i=0; i < size; i++) {
			boolean isNeg = false;
			int index = this.indices[i];
			if(index < 0) {
				isNeg = true;
			}
			boolean bit = assignment.get(Math.abs(index) - 1);
			if(isNeg) {
				bit = !bit;
			}
			if(bit) {
				return true;
			}
		}
		return false;
		
	}

	@Override
	public String toString() {
		return null;
	}
}