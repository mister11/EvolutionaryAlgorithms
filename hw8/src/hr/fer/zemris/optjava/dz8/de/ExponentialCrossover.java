package hr.fer.zemris.optjava.dz8.de;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class ExponentialCrossover implements ICrossover {
	
	private static Random r = new Random();

	@Override
	public RealVector crossover(RealVector target, RealVector mutant, double cr) {
		int size = target.getDimension();
		double[] trial = new double[size];
		int index = r.nextInt(size);
		trial[index] = mutant.getEntry(index);
		for(int i=index; i < index + size; i++) {
			if(r.nextDouble() < cr) {
				trial[i % size] = mutant.getEntry(i % size);
			} else {
				index = i;
				break;
			}
		}
		for(int i=index; i < index + size; i++) {
			trial[i % size] = target.getEntry(i % size);
			
		}
		
		return new ArrayRealVector(trial);
	}

}
