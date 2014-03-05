package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public interface IHFunction extends IFunction {

	public RealMatrix hesseMatrix(RealVector vector);
}
