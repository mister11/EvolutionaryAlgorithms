package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class GradientDescent2a implements IFunction {

	@Override
	public int numOfVariables() {
		return 2;
	}

	@Override
	public double functionValue(RealVector vector) {
		double x = vector.getEntry(0);
		double y = vector.getEntry(1);
		
		return (x - 1) * + (x - 1) + 10 * (y - 2) * (y - 2);
	}

	@Override
	public RealVector gradValue(RealVector vector) {
		double x = vector.getEntry(0);
		double y = vector.getEntry(1);
		
		return new ArrayRealVector(new double[] {2 * (x - 1), 20 * (y - 2)});
	}

}
