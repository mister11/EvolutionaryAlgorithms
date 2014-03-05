package hr.fer.zemris.optjava.dz2;


public class Vector {

	private double[] elements;
	private int dimension;

	public Vector(final int dimension) {
		this.dimension = dimension;
		this.elements = new double[dimension];
	}


	public Vector(final double... elems) {
		if (elems.length == 0) {
			throw new IllegalArgumentException(
					"Argument must contain elements!");
		}
		int length = elems.length;
		elements = new double[elems.length];
		for (int i = 0; i < length; i++) {
			this.elements[i] = elems[i];
		}
		this.dimension = elements.length;
	}

	
	public Vector(final boolean readOnly, final boolean useGiven,
			final double... elems) {
		if (elems.length == 0) {
			throw new IllegalArgumentException(
					"Argument must contain elements!");
		}
		if (useGiven) { // take the existing reference
			elements = elems;
		} else {// make your own copy
			elements = new double[elems.length];
			for (int i = 0; i < elems.length; i++) {
				this.elements[i] = elems[i];
			}
		}
		this.dimension = elems.length;
	}

	public double get(int index) {
		if (index < 0 || index > elements.length - 1) {
			throw new IndexOutOfBoundsException(
					"Index must be greater than 0 and less than length-1");
		}

		return elements[index];
	}

	public Vector set(final int index, final double value) {
		if (index < 0 || index > elements.length - 1) {
			throw new IndexOutOfBoundsException(
					"Index must be greater than 0 and less than length-1");
		}
		elements[index] = value;
		return this;
	}

	public int getDimension() {
		return this.dimension;
	}

	
	public static Vector parseSimple(String input) {
		String[] helpArray = input.trim().replaceAll(" +", " ").split(" ");
		double[] newDoubleArray = new double[helpArray.length];

		for (int i = 0; i < helpArray.length; i++) {
			newDoubleArray[i] = Double.parseDouble(helpArray[i]);
		}
		return new Vector(newDoubleArray);
	}

}
