package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Problem3 implements IHFunction {

	private double[][] matrix;
	private double[]y;
	
	public Problem3(double[][] matrix, double[] y) {
		this.matrix = matrix;
		this.y = y;
	}
	
	@Override
	public int numOfVariables() {
		return y.length;
	}

	@Override
	public double functionValue(RealVector vector) {
		int size = this.matrix.length;
		double[] error = new double[size];
		for(int i=0; i < size; i++) {
			double myY = 0;
			for(int j=0; j < size; j++) {
				myY += vector.getEntry(j) * matrix[i][j];
			}
			error[i] = (myY - y[i]) * (myY - y[i]);
		}
		double err = 0.0;
		for(double d : error) {
			err += d;
		}
		return err;
	}

	@Override
	public RealVector gradValue(RealVector vector) {
		int size = this.matrix.length;
		double[] error = new double[size];
		for(int i=0; i < size; i++) {
			double myY = 0;
			for(int j=0; j < size; j++) {
				myY += vector.getEntry(j) * matrix[i][j];
			}
			error[i] = (myY - y[i]);
		}
		double[] grad = new double[size];
		for(int i=0; i < size; i++) {
			double value = 0.0;
			for(int j=0; j < size; j++) {
				value += 2 * this.matrix[j][i] * error[j];
			}
			grad[i] = value;
		}
		return new ArrayRealVector(grad);
	}

	@Override
	public RealMatrix hesseMatrix(RealVector vector) {
		
		int size = vector.getDimension();
		double[][] hesse = new double[size][size];
		for(int i=0; i < size; i++) {
			for(int j=0; j < size; j++) {
				double value = 0.0;
				for(int k=0; k < size; k++) {
					value += 2 * this.matrix[k][i] * this.matrix[k][j];
				}
				hesse[i][j] = value;
			}
		}
		return new Array2DRowRealMatrix(hesse);
	}

}
