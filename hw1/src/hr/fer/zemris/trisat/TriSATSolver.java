package hr.fer.zemris.trisat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TriSATSolver {
	
	private static final int NUM_OF_ITERS = 100000;

	private static int numOfVars = 0;
	private static List<Clause> clauses = new ArrayList<>();
	private static SATFormula formula = null;

	public static void main(String[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException(
					"Number of parameters must be 2. Number of algorith and file.");
		}
		parse(args[1]);
		formula = new SATFormula(numOfVars, clauses);
		int indexOfAlgorithm = Integer.parseInt(args[0]);

		switch (indexOfAlgorithm) {
		case 1:
			runAlg1();
			break;
		case 2:
			runAlg2();
			break;
		case 3:
			runAlg3();
			break;
		default:
			System.err.println("Index of algorithms should be 1, 2 or 3");
		}

	}

	private static void runAlg1() {
		boolean found = false;
		boolean[] bits = new boolean[numOfVars];
		boolean isEnd = false;
		
		BitVector vector = new BitVector(bits);
		if (formula.isSatisfied(vector)) {
			found = true;
			System.out.println(vector);
		}
		//npr. 0000 -> 0001 -> 0010 -> 0011...
		while (true) {
			isEnd = true;
			for (int i = numOfVars - 1; i >= 0; i--) {
				if (bits[i]) {
					bits[i] = false;
					continue;
				}
				isEnd = false;
				bits[i] = true;
				break;
			}
			if (isEnd) {
				break;
			}
			vector = new BitVector(bits);
			if (formula.isSatisfied(vector)) {
				found = true;
				System.out.println(vector);
			}
		}
		if (!found) {
			System.out.println("null");
		}
	}

	private static void runAlg2() {
		boolean found = false;
		BitVector bitVector = new BitVector(new Random(), numOfVars);
		SATFormulaStats stats = new SATFormulaStats(formula);
		for(int i=0; i < NUM_OF_ITERS; i++) {
			stats.setAssignment(bitVector, false);
			int fitness = stats.getNumberOfSatisfied();
			
			if(stats.isSatisfied()) {
				found = true;
				System.out.println(bitVector);
				break;
			}
			BitVectorNGenerator neighbor = new BitVectorNGenerator(bitVector);
			int maxFit = 0;
			for(MutableBitVector v : neighbor) {
				stats.setAssignment(v, false);
				int fit = stats.getNumberOfSatisfied();
				maxFit = fit > maxFit ? fit : maxFit;
			}
	
			if(maxFit < fitness) {
				System.out.println("Algorithm stuck in local optima.");
				found = true;
				break;
			}
			List<MutableBitVector> possibleSol = new ArrayList<>();
			for(MutableBitVector v : neighbor) {
				stats.setAssignment(v, false);
				int fit = stats.getNumberOfSatisfied();
				if(fit == maxFit) {
					possibleSol.add(v);
				}
			}
			if(possibleSol.size() == 1) {
				bitVector = possibleSol.get(0);
			} else {
				bitVector = possibleSol.get(new Random().nextInt(possibleSol.size()));
			}
		}
		if(!found) {
			System.out.println("null");
	
		}
	}
	
	private static void runAlg3() {
		boolean found = false;
		BitVector bitVector = new BitVector(new Random(), numOfVars);
		SATFormulaStats stats = new SATFormulaStats(formula);
		for(int i=0; i < NUM_OF_ITERS; i++) {
			stats.setAssignment(bitVector, true);
			
			if(stats.isSatisfied()) {
				found = true;
				System.out.println(bitVector);
				break;
			}
			BitVectorNGenerator neighbor = new BitVectorNGenerator(bitVector);
			//polje preko kojeg se dobiva najbolja korekcija
			double[] corrections = new double[numOfVars];
			//polje koje služi da bi se našao index susjeda koji ima najbolju korekciju
			double[] unsortedCorrections = new double[numOfVars];
			//spremim susjede da ih ne moram ponovo stvarati
			List<MutableBitVector> neighbors = new ArrayList<>();
			int x = 0;
			for(MutableBitVector v : neighbor) {
				neighbors.add(v);
				stats.setAssignment(v, false);
				corrections[x] = unsortedCorrections[x] = stats.getPercentageBonus();
				x++;				
			}
			Arrays.sort(corrections);
			int newIndex = new Random().nextInt(SATFormulaStats.NUM_OF_BEST);
			double newCorr = corrections[corrections.length - 1 - newIndex];
			int size = corrections.length - 1;
			//traženje indeksa susjeda koji ima najbolju korekciju
			for(int a=0; a <= size; a++) {
				if(Double.compare(unsortedCorrections[a], newCorr) == 0) {
					bitVector = neighbors.get(a);
					break;
				}
			}
		}
		if(!found) {
			System.out.println("null");
		}
	}

	

	private static void parse(String file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(
							new FileInputStream(file)), "UTF-8"));
			while (true) {
				String lineRead = br.readLine();
				String line = lineRead.trim();
				if (line.startsWith("c")) {
					continue;
				}
				if (line.startsWith("%")) {
					break;
				}
				if (line.startsWith("p")) {
					String[] splitted = line.split("\\s+");
					numOfVars = Integer.parseInt(splitted[2]);
				} else {
					String[] splitted = line.split("\\s+");
					int[] indices = new int[splitted.length - 1];
					int size = splitted.length - 1;
					for (int i = 0; i < size; i++) {
						indices[i] = Integer.parseInt(splitted[i]);
					}
					clauses.add(new Clause(indices));
				}

			}
		} catch (IOException e) {
			System.err.println("Error opening file " + file);
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
