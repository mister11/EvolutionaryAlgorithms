package hr.fer.zemris.optjava.dz5.part2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Data {

	private int[][] distances;
	private int[][] costs;
	private int n;
	
	public Data(String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(filename))));
			String line = br.readLine().trim();
			int n = Integer.parseInt(line);
			if(!br.readLine().isEmpty()) {
				System.err.println("Wrong data structure");
				System.exit(-1);
			}
			this.n = n;
			this.distances = new int[n][n];
			this.costs = new int[n][n];
			for(int i=0; i < n; i++) {
				String[] lineParts = br.readLine().trim().split("\\s+");
				for(int j=0; j < n; j++) {
					this.costs[i][j] = Integer.parseInt(lineParts[j]);
				}
			}
			if(!br.readLine().isEmpty()) {
				System.err.println("Wrong data structure");
				System.exit(-1);
			}
			for(int i=0; i < n; i++) {
				String[] lineParts = br.readLine().trim().split("\\s+");
				for(int j=0; j < n; j++) {
					this.distances[i][j] = Integer.parseInt(lineParts[j]);
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

	public int[][] getDistances() {
		return distances;
	}
	
	public int[][] getCosts() {
		return costs;
	}
	
	public int getN() {
		return n;
	}
}
