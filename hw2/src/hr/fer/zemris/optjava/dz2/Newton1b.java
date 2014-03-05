package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Newton1b extends GradientDescent1a implements IHFunction {


	@Override
	public RealMatrix hesseMatrix(RealVector vector) {
		double[][] matrix = {{2, 0}, {0, 2}};
		return new Array2DRowRealMatrix(matrix);
	}
	
}
