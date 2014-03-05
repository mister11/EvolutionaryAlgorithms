package hr.fer.zemris.optjava.dz2;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Jednostavno {

	private static final int LOW = -5;
	private static final int HIGH = 5;
	
	public static void main(String[] args) {
		
		if(args.length != 2 && args.length != 4) {
			System.err.println("Wrong number of parameters.");
			return;
		}
		
		int maxIters = 0;
		try {
			maxIters = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			System.err.println("Second argument must be an integer.");
		}
		
		double[] point = new double[2];
		Random r = new Random();
		if(args.length == 4) {
			try {
				point[0] = Double.parseDouble(args[2]);
				point[1] = Double.parseDouble(args[3]);
			} catch (NumberFormatException ex) {
				System.err.println("Thrid and fourth arguments must be double.");
			}
		} else {
			point[0] = LOW + r.nextDouble()*(HIGH - LOW + 1);
			point[1] = LOW + r.nextDouble()*(HIGH - LOW + 1);
		}
		
		final String alg = args[0];
		if(alg.equals("1a")) {
			runAlg1a(maxIters, point);
		} else if(alg.equals("1b")) {
			runAlg1b(maxIters, point);
		} else if(alg.equals("2a")) {
			runAlg2a(maxIters, point);
		} else if(alg.equals("2b")) {
			runAlg2b(maxIters, point);
		} else {
			System.err.println("Wrong type of parameter.");
			return;
		}
		
		
	}

	private static void runAlg1a(int maxIters, double[] point) {
		RealVector vector = new ArrayRealVector(point);
		GradientDescent1a gd = new GradientDescent1a();
		NumOptAlgorithms.gradientDescent(gd, maxIters, vector, false);
	}

	private static void runAlg1b(int maxIters, double[] point) {
		RealVector vector = new ArrayRealVector(point);
		Newton1b newton = new Newton1b();
		NumOptAlgorithms.newtonMethod(newton, maxIters, vector, false);
	}

	private static void runAlg2a(int maxIters, double[] point) {
		RealVector vector = new ArrayRealVector(point);
		GradientDescent2a gd = new GradientDescent2a();
		NumOptAlgorithms.gradientDescent(gd, maxIters, vector, false);
	}

	private static void runAlg2b(int maxIters, double[] point) {
		RealVector vector = new ArrayRealVector(point);
		Newton2b newton = new Newton2b();
		NumOptAlgorithms.newtonMethod(newton, maxIters, vector, false);
	}

}
