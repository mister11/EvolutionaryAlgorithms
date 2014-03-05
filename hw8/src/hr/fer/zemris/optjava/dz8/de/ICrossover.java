package hr.fer.zemris.optjava.dz8.de;

import org.apache.commons.math3.linear.RealVector;

public interface ICrossover {
	
	public RealVector crossover(RealVector target, RealVector mutant, double cr);

}
