package hr.fer.zemris.optjava.dz2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;

public class Prijenosna {

	private static final int LOW = -5;
	private static final int HIGH = 5;
	private static final int NUM_OF_VARS = 6;

	private static double[][] matrix;
	private static double[] y;

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Wrong number of params.");
			return;
		}
		int maxIter = 0;
		try {
			maxIter = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			System.err.println("Second parameter must be an integer");
		}

		parse(args[2]);

		if (args[0].equals("grad")) {
			runGradDescent(maxIter);
		} else if (args[0].equals("newton")) {
			runNewtonMethod(maxIter);
		} else {
			System.err.println("Wrong first param!");
		}
	}

	private static void runGradDescent(int maxIters) {
		Problem4 gd = new Problem4(matrix, y);
		ArrayRealVector vector = generateRandomVector();
		NumOptAlgorithms.gradientDescent(gd, maxIters, vector, true);
	}

	private static void runNewtonMethod(int maxIters) {
		Problem4 newton = new Problem4(matrix, y);
		ArrayRealVector vector = generateRandomVector();
		NumOptAlgorithms.newtonMethod(newton, maxIters, vector, true);
	}

	private static ArrayRealVector generateRandomVector() {
		Random r = new Random();
		double[] vector = new double[NUM_OF_VARS];
		for (int i = 0; i < vector.length; i++) {
			vector[i] = LOW + r.nextDouble() * (HIGH - LOW + 1);
		}
		return new ArrayRealVector(vector);
	}

	private static void parse(String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(filename))));
			int numOfLines = 0;
			int numOfCols = 0;
			while (true) {
				String s = br.readLine();
				if (s == null) {
					break;
				} else if (s.startsWith("#")) {
					continue;
				} else {
					if(numOfCols == 0) {
						numOfCols = s.trim().split(",").length - 1;
					}
					numOfLines++;
				}
			}
			matrix = new double[numOfLines][numOfCols];
			y = new double[numOfLines];
			if (br != null) {
				br.close();
			}
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(filename)),
					"UTF-8"));
			int col = 0;
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				} else if (line.startsWith("#")) {
					continue;
				} else {
					String lineNums = line.trim().substring(1,
							line.length() - 1);
					String[] nums = lineNums.split(",");
					int size = nums.length;
					for (int i = 0; i < size - 1; i++) {
						matrix[col][i] = Double.parseDouble(nums[i].trim());
					}
					y[col] = Double.parseDouble(nums[size - 1].trim());
					col++;
				}
			}
		} catch (IOException e) {
			System.err.println("Error opening file " + filename);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("Error closing reader!");
				}
			}
		}
	}
}
