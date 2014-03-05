package hr.fer.zemris.optjava.dz8.de;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class UniformCrossover implements ICrossover {

	private static Random r = new Random();
	
	@Override
	public RealVector crossover(RealVector target, RealVector mutant, double cr) {
		int size = target.getDimension();
		double[] trial = new double[size];
		for(int i=0; i < size; i++) {
			if(r.nextDouble() < cr) {
				trial[i] = mutant.getEntry(i);
			} else {
				trial[i] = target.getEntry(i);
			}
		}
		int index = r.nextInt(size);
		trial[index] = mutant.getEntry(index);
		return new ArrayRealVector(trial);
	}

}
