package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.RealVector;

public interface IFunction {
	
	public int numOfVariables();
	
	public double functionValue(RealVector vector);
	
	public RealVector gradValue(RealVector vector);

}
