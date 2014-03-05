package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Problem4 implements IHFunction {

	private double[][] matrix;
	private double[] y;
	
	public Problem4(double[][] matrix, double[] y) {
		this.matrix = matrix;
		this.y = y;
	}

	@Override
	public int numOfVariables() {
		return 6;
	}

	@Override
	public double functionValue(RealVector vector) {
		int size = y.length;
		double[] error = new double[size];
		double[] v = vector.toArray();
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

	@Override
	public RealVector gradValue(RealVector vector) {
		int size = y.length;
		double[] error = new double[size];
		double[] v = vector.toArray();
		for(int i=0; i < size; i++) {
			error[i] = v[0]*matrix[i][0] + v[1] * Math.pow(matrix[i][0], 3) * matrix[i][1] + 
					v[2] * Math.exp(v[3] * matrix[i][2]) * (1 + Math.cos(v[4] * matrix[i][3])) +
					v[5] * matrix[i][3] * matrix[i][4] * matrix[i][4] - y[i];
		}
		double[] grad = new double[vector.getDimension()];
		grad[0] = getGrad1(error);
		grad[1] = getGrad2(error);
		grad[2] = getGrad3(error, v);
		grad[3] = getGrad4(error, v);
		grad[4] = getGrad5(error, v);
		grad[5] = getGrad6(error);
		return new ArrayRealVector(grad).unitVector();
	}

	@Override
	public RealMatrix hesseMatrix(RealVector vector) {
		int size = y.length;
		double[] error = new double[size];
		double[] v = vector.toArray();
		for(int i=0; i < size; i++) {
			error[i] = v[0]*matrix[i][0] + v[1] * Math.pow(matrix[i][0], 3) * matrix[i][1] + 
					v[2] * Math.exp(v[3] * matrix[i][2]) * (1 + Math.cos(v[4] * matrix[i][3])) +
					v[5] * matrix[i][3] * matrix[i][4] * matrix[i][4] - y[i];
		}
		
		double dada = 0.0;
		for(int i=0; i < size; i++) {
			dada += matrix[i][0] * matrix[i][0];
		}
		dada = 2*dada;
		
		double dadb = 0.0;
		for(int i=0; i < size; i++) {
			dadb += Math.pow(matrix[i][0], 4) * matrix[i][1];
		}
		dadb = 2*dadb;
		
		double dadc = 0.0;
		for(int i=0; i < size; i++) {
			dadc += matrix[i][0] * Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3]));
		}
		dadc = 2*dadc;
		
		double dadd = 0.0;
		for(int i=0; i < size; i++) {
			dadd += matrix[i][0] * matrix[i][2] * v[2] * 
					Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3]));
		}
		dadd = 2*dadd;
		
		double dade = 0.0;
		for(int i=0; i < size; i++) {
			dade += matrix[i][0] * matrix[i][3] * v[2] * 
					Math.exp(v[3] * matrix[i][2]) * 
					Math.sin(v[4] * matrix[i][3]);
		}
		dade = (-2)*dade;
		
		double dadf = 0.0;
		for(int i=0; i < size; i++) {
			dadf += matrix[i][0] * matrix[i][3] * 
					matrix[i][4] * matrix[i][4];
		}
		dadf = 2*dadf;
		
		double dbdb = 0.0;
		for(int i=0; i < size; i++) {
			dbdb += Math.pow(matrix[i][0], 6) * 
					matrix[i][1] * matrix[i][1];
		}
		dbdb = 2*dbdb;
		
		double dbdc = 0.0;
		for(int i=0; i < size; i++) {
			dbdc += matrix[i][0] * matrix[i][0] * 
					matrix[i][0] * matrix[i][1] * 
					Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3]));
		}
		dbdc = 2*dbdc;
		
		double dbdd= 0.0;
		for(int i=0; i < size; i++) {
			dbdd += matrix[i][0] * matrix[i][0] * 
					matrix[i][0] * matrix[i][1] * 
					Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3])) * 
					v[2] * matrix[i][2];
		}
		dbdd = 2*dbdd;
		
		double dbde = 0.0;
		for(int i=0; i < size; i++) {
			dbde += Math.pow(matrix[i][0], 3) * 
					matrix[i][1] * matrix[i][3] * 
					v[2] * Math.exp(v[3] * matrix[i][2]) * 
					Math.sin(v[4] * matrix[i][3]);
		}
		dbde = (-2)*dbde;
		
		double dbdf = 0.0;
		for(int i=0; i < size; i++) {
			dbdf += Math.pow(matrix[i][0], 3) * 
					matrix[i][1] * matrix[i][3] * 
					matrix[i][4] * matrix[i][4];
		}
		dbdf = 2*dbdf;
		
		double dcdc = 0.0;
		for(int i=0; i < size; i++) {
			dcdc += Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3])) * 
					Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3]));
		}
		dcdc = 2*dcdc;
		
		double dcdd = 0.0;
		for(int i=0; i < size; i++) {
			dcdd += matrix[i][2] * Math.exp(v[3] * 
					matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3])) * 
					(v[2] * Math.exp(v[3] * matrix[i][2]) * 
							(1 + Math.cos(v[4] * matrix[i][3])) + error[i]);
		}
		dcdd = 2*dcdd;
		
		double dcde = 0.0;
		for(int i=0; i < size; i++) {
			dcde += matrix[i][3] * Math.exp(v[3] * 
					matrix[i][2]) * 
					Math.sin(v[4] * matrix[i][3]) * 
					(error[i] + v[2] * Math.exp(v[3] * 
							matrix[i][2]) * (1 + Math.cos(v[4] * matrix[i][3])));
		}
		dcde = (-2)*dcde;
		
		double dcdf = 0.0;
		for(int i=0; i < size; i++) {
			dcdf += matrix[i][3] * matrix[i][4] * matrix[i][4] * 
					Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3]));
		}
		dcdf = 2*dcdf;
		
		double dddd = 0.0;
		for(int i=0; i < size; i++) {
			dddd += v[2] * matrix[i][2] * matrix[i][2] * 
					Math.exp(v[3] * matrix[i][2]) *
					(1 + Math.cos(v[4] * matrix[i][3])) * 
					(error[i] + v[2] * Math.exp(v[3] * 
							matrix[i][2]) * (1 + Math.cos(v[4] * matrix[i][3])));
		}
		dddd = 2*dddd;
		
		double ddde = 0.0;
		for(int i=0; i < size; i++) {
			ddde += v[2]  * matrix[i][2] * matrix[i][3] * 
					Math.exp(v[3] * matrix[i][2]) * 
					Math.sin(v[4] * matrix[i][3]) * 
					(error[i] + v[2] * Math.exp(v[3] * 
							matrix[i][2]) * (1 + Math.cos(v[4] * 
									matrix[i][3])));
		}
		ddde = (-2)*ddde;
		
		double dddf = 0.0;
		for(int i=0; i < size; i++) {
			dddf += v[2] * matrix[i][2] * matrix[i][3] * 
					matrix[i][4] * matrix[i][4] * 
					Math.exp(v[3] * matrix[i][2]) * 
					(1 + Math.cos(v[4] * matrix[i][3]));
		}
		dddf = 2*dddf;
		
		double dede = 0.0;
		for(int i=0; i < size; i++) {
			dede += v[2] * matrix[i][3] * matrix[i][3] * 
					Math.exp(v[3] * matrix[i][2]) * 
					(Math.cos(v[4] * matrix[i][3]) * 
							error[i] - v[2] * Math.exp(v[3] * matrix[i][2]) * 
							Math.sin(v[4] * matrix[i][3]) * 
							Math.sin(v[4] * matrix[i][3])); 
		}
		dede = (-2)*dede;
		
		double dedf = 0.0;
		for(int i=0; i < size; i++) {
			dedf += matrix[i][3] * matrix[i][3] * 
					matrix[i][4] * matrix[i][4] * 
					v[2] * Math.exp(v[3] * matrix[i][2]) * 
					Math.sin(v[4] * matrix[i][3]);
		}
		dedf = (-2)*dedf;
		
		double dfdf = 0.0;
		for(int i=0; i < size; i++) {
			dfdf += matrix[i][3] * matrix[i][3] * 
					Math.pow(matrix[i][4], 4);
		}
		dfdf = 2*dfdf;
		
		double[][] hesse = new double[][]{
			    {dada,dadb,dadc,dadd,dade,dadf},
			    {dadb,dbdb,dbdc,dbdd,dbde,dbdf},
			     {dadc,dbdc,dcdc,dcdd,dcde,dcdf},
			    {dadd,dbdd,dcdd,dddd,ddde,dddf},
			    {dade,dbde,dcde,ddde,dede,dedf},
			   {dadf,dbdf,dcdf,dddf,dedf,dfdf}
			  };
		return new Array2DRowRealMatrix(hesse);
	}
	
	private double getGrad1(double[] error) {
		int size = y.length;
		double value = 0.0;
		for(int i=0; i < size; i++) {
			value += 2 * error[i] * matrix[i][0];
		}
		return value;
	}
	
	private double getGrad2(double[] error) {
		int size = y.length;
		double value = 0.0;
		for(int i=0; i < size; i++) {
			value += 2 * error[i] * Math.pow(matrix[i][0], 3) * matrix[i][1];
		}
		return value;
	}
	
	private double getGrad3(double[] error, double[] v) {
		int size = y.length;
		double value = 0.0;
		for(int i=0; i < size; i++) {
			value += 2 * error[i] * Math.exp(v[3] * matrix[i][2]) * (1 + Math.cos(v[4] * matrix[i][3]));
		}
		return value;
	}
	
	private double getGrad4(double[] error, double[] v) {
		int size = y.length;
		double value = 0.0;
		for(int i=0; i < size; i++) {
			value += 2 * error[i] * v[2] * matrix[i][2] * Math.exp(v[3] * matrix[i][2]) * (1 + Math.cos(v[4] * matrix[i][3]));
		}
		return value;
	}
	
	private double getGrad5(double[] error, double[] v) {
		int size = y.length;
		double value = 0.0;
		for(int i=0; i < size; i++) {
			value += 2 * error[i] * matrix[i][3] * v[2] * Math.exp(v[3] * matrix[i][2]) * Math.sin(v[4] * matrix[i][3]);
		}
		return (-1) * value;
	}
	
	private double getGrad6(double[] error) {
		int size = y.length;
		double value = 0.0;
		for(int i=0; i < size; i++) {
			value += 2 * error[i] * matrix[i][3] * matrix[i][4] * matrix[i][4];
		}
		return value;
	}
}
