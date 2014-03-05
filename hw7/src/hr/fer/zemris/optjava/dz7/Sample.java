package hr.fer.zemris.optjava.dz7;

import java.util.Arrays;

public class Sample {
	
	public double[] input;
	public double[] output;
	
	public Sample(double[] input, double[] output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public String toString() {
		return Arrays.toString(input) + "\n" + Arrays.toString(output);
	}
}
