package hr.fer.zemris.optjava.dz4.part1;

public class Function implements IFunction {
	
	private double[][] matrix;
	private double[] y;
	
	public Function(double[][] matrix, double[] y) {
		this.matrix = matrix;
		this.y = y;
	}
	
	@Override
	public double valueAt(double[] v) {
		int size = y.length;
		double[] error = new double[size];
		for(int i=0; i < size; i++) {
			error[i] = v[0]*matrix[i][0] + v[1] * Math.pow(matrix[i][0], 3) * matrix[i][1] + 
					v[2] * Math.exp(v[3] * matrix[i][2]) * (1 + Math.cos(v[4] * matrix[i][3])) +
					v[5] * matrix[i][3] * matrix[i][4] * matrix[i][4] - y[i];
		}
		double err = 0.0;
		for(double d : error) {
			err += d * d;
		}
		return err;	
	}

}
