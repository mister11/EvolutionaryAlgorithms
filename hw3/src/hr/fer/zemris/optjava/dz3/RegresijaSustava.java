package hr.fer.zemris.optjava.dz3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class RegresijaSustava {

	private static double[][] matrix;
	private static double[] y;
	
	public static void main(String[] args) {

		if (args.length != 2) {
			System.err.println("Wrong number of params.");
			return;
		}

		parse(args[0]);

		IFunction fun = new Function(matrix, y);
		ITempSchedule temp = new GeomTempSchedule(0.01, 100, 1000, 20000);
		
		
		String second = args[1];
		if(second.startsWith("decimal")) {
			DoubleArraySolution startsWith = new DoubleArraySolution(6);
			startsWith.randomize(new Random(), -10, 10);
			INeighborhood<DoubleArraySolution> neighborhood = new DoubleArrayNeighbor(10, 6);
			IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();
			SimulatedAnnealing<DoubleArraySolution> alg = new SimulatedAnnealing<DoubleArraySolution>(decoder, neighborhood, startsWith, fun, temp, true);
			SingleObjectiveSolution sol = alg.run();
			printStats(sol);
		} else if(second.startsWith("binary")) {
			int k = 0;
			try {
				k = Integer.parseInt(second.substring(7));
			} catch (NumberFormatException e) {
				System.err.println("Parameter should look like: binary:X where X is some integer");
			}
			BitVectorSolution startsWith = new BitVectorSolution(6 * k);
			startsWith.randomize(new Random());
			INeighborhood<BitVectorSolution> neighborhood = new BitVectorNeighbor();
			IDecoder<BitVectorSolution> decoder = new NaturalBinaryDecoder(-10, 10, k, 6);
			SimulatedAnnealing<BitVectorSolution> alg = new SimulatedAnnealing<BitVectorSolution>(decoder, neighborhood, startsWith, fun, temp, true);
			SingleObjectiveSolution sol = alg.run();
			printStats(sol);
		} else {
			System.err.println("Check you parameters.");
		}
		
		
	}

	private static void printStats(SingleObjectiveSolution sol) {
		System.out.println("Error is: " + sol.fitness);
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
