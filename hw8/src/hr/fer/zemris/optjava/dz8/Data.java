package hr.fer.zemris.optjava.dz8;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Data {
	
	private List<Sample> trainingSet;
	private List<Integer> data;
	private int min;
	private int max;
	//broj ulaznih vrijednosti
	private int l;
	//broj podataka koji iz danog seta koristimo za stvaranje training seta
	private int a;
	
	
	public Data(String filename, int l, int a) {
		this.trainingSet = new ArrayList<Sample>();
		this.data = new ArrayList<Integer>();
		this.min = Integer.MAX_VALUE;
		this.max = Integer.MIN_VALUE;
		this.l = l;
		parseFile(filename);
		this.a = a == -1 ? data.size() : a;
		createTrainingSet();
	}

	public int getTrainingSetSize() {
		return this.trainingSet.size();
	}
	
	public int getInputSize() {
		return this.trainingSet.get(0).input.length;
	}
	
	public int getOutputSize() {
		return this.trainingSet.get(0).output.length;		
	}
	
	public Sample getSample(int index) {
		return this.trainingSet.get(index);
	}
	
	public double[] getInput(int index) {
		return this.trainingSet.get(index).input;
	}
	
	public double[] getOutput(int index) {
		return this.trainingSet.get(index).output;
	}
	
	
	private void parseFile(String filename) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename)));
			while(true) {
				String line = reader.readLine();
				if(line == null || line.trim().isEmpty()) {
					break;
				}
				int input = Integer.parseInt(line.trim());
				if(input < this.min) {
					this.min = input;
				}
				if(input > this.max) {
					this.max = input;
				}
				data.add(input);				
			}
		} catch(IOException e) {
			System.err.println("Error happened during reading file " + filename);
			System.exit(-1);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("Error happened during closing input stream!");
					System.exit(-2);
				}
			}
		}
	}
	
	private void createTrainingSet() {
		for(int i=0; i < this.a - this.l; i++) {
			int index = 0;
			double[] inputs = new double[this.l];
			double range = this.max - this.min;
			int j;
			for(j=i; j < i + this.l; j++) {
				inputs[index++] = -1 + 2*(data.get(j) - this.min) / range;
			}
			inputs = processInput(inputs);
			double[] output = new double[1];
			output[0] = -1 + 2*(data.get(j) - this.min) / range;
			trainingSet.add(new Sample(inputs, output));
		}
	}


	private double[] processInput(double[] input) {
		double[] newInput = new double[input.length + 1];
		newInput[0] = 1; //da mogu raditi vektorizaciju s matricama
		//thete će biti veličine (broj neurona u slijedećem sloju x broj neurona u trentunom sloju + 1)
		
		//input će originalno biti 1 x broj ulaza, broj ulaza = 4 u našem slučaju
		
		//broj neurona u prvom sloju će zato biti 4, ali pošto dodajemo 1 kod thete, onda moramo
		//nekako posložiti to da se može izmnožiti pa dodajemo prvu komponentu 1 jer ona neće ništa
		//promijeniti
		int i = 1;
		for(Double s : input) {
			newInput[i++] = s;
		}
		return newInput;
	}
	
	public static void main(String[] args) {
		Data d = new Data("data.txt", 4, 500);
		double[][] a = {{3,4,5},{1,7,9},{5,1,3}};
		
	}
}
