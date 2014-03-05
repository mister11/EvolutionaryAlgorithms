package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class NumOptAlgorithms {
	
	public static void gradientDescent(IFunction function, int maxIters, RealVector vector, boolean print) {
		boolean found = false;
		for(int i=0; i < maxIters; i++) {
			if(!print) {
				System.out.println(vector.toString());
			}
			if(isSolution(vector, function)) {
				if(print) {
					System.out.println("Found vector:");
					System.out.println(vector);
					System.out.println("\n\nError:");
					System.out.println(function.functionValue(vector));
					System.out.println("\n\nIteration:");
					System.out.println(i);
				}
				found = true;
				break;
			}
			RealVector grad = function.gradValue(vector);
			RealVector d = grad.mapMultiply(-1);
			int lambdaUpper = getLambdaUpper(vector, function, d);
			double lambda = getLambda(0, lambdaUpper, vector, function, d);
			RealVector newD = d.mapMultiply(lambda);
			vector = vector.add(newD);
		}
		if(!found) {
			//pošto se zahtjeva velika preciznost, ponekad ne nađe rješenje
			System.out.println("Not enough iterations");
			System.out.println("Best found vector: " + vector);
			System.out.println("Error is: " + function.functionValue(vector));
			
		}
	}

	private static double getLambda(double lambdaLower, double lambdaUpper, 
							RealVector vector, IFunction function, RealVector d) {
		while(true) {
			double lambdaMed = (lambdaLower + lambdaUpper) / 2;
			double value = getValue(lambdaMed, vector, function, d);
			if(Math.abs(value - 0) <= 10E-6) {
				return lambdaMed;
			} else if(value < 0) {
				lambdaLower = lambdaMed;
			} else {
				lambdaUpper = lambdaMed;
			}
		}
	}

	
	private static double getValue(double lambdaMed, 
			RealVector vector, IFunction function, RealVector d) {
		int size = function.numOfVariables();
		double res = 0.0;
		RealVector dLambda = d.mapMultiply(lambdaMed);
		RealVector forGrad = vector.add(dLambda);
		RealVector grad = function.gradValue(forGrad);
		for(int j=0; j < size; j++) {
			res += grad.getEntry(j) * d.getEntry(j);
		}
		return res;
		
	}

	private static int getLambdaUpper(RealVector vector, IFunction function, RealVector d) {
		for(int i=1;;i *= 2) {
			int size = function.numOfVariables();
			double res = 0.0;
			RealVector dLambda = d.mapMultiply(i);
			RealVector forGrad = vector.add(dLambda);
			RealVector grad = function.gradValue(forGrad);
			for(int j=0; j < size; j++) {
				res += grad.getEntry(j) * d.getEntry(j);
			}
			if(res >= 0) {
				return i;
			}
		}
	}

	private static boolean isSolution(RealVector vector, IFunction function) {
		RealVector grad = function.gradValue(vector);
		int size = grad.getDimension();
		for(int j=0; j < size; j++) {
			if(Math.abs(grad.getEntry(j)) > 10E-3) {
				return false;
			}
		}
		return true;
	}

	public static void newtonMethod(IHFunction function, int maxIters, RealVector vector, boolean print) {
		boolean found = false;
		for(int i=0; i < maxIters; i++) {
			System.out.println(i);
			if(!print) {
				System.out.println(vector.toString());
			}
			if(isSolution(vector, function)) {
				if(print) {
					System.out.println("Found vector:");
					System.out.println(vector);
					System.out.println("\n\nError:");
					System.out.println(function.functionValue(vector));
					System.out.println("\n\nIteration:");
					System.out.println(i);
				}
				found = true;
				break;
			}
			RealMatrix hesse = function.hesseMatrix(vector);
			RealVector grad = function.gradValue(vector);
			RealVector d = getTau(grad, hesse);
			int lambdaUpper = getLambdaUpper(vector, function, d);
			double lambda = getLambda(0, lambdaUpper, vector, function, d);
			RealVector newD = d.mapMultiply(lambda);
			vector = vector.add(newD);
		}
		if(!found) {
			System.out.println("Not enough iterations!");
			System.out.println("Best found vector: " + vector);
			System.out.println("Error is: " + function.functionValue(vector));
		}
	}

	private static RealVector getTau(RealVector grad, RealMatrix hesse) {
		RealMatrix inverse = new LUDecomposition(hesse).getSolver().getInverse();
		double[][] matrixGrad = {grad.toArray()};
		RealMatrix gradMatrix = new Array2DRowRealMatrix(matrixGrad);
		RealVector tau = inverse.multiply(gradMatrix.transpose()).getColumnVector(0);
		return tau.mapMultiply(-1);
	}

}
