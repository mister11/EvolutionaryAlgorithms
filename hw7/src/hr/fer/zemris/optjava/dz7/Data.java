package hr.fer.zemris.optjava.dz7;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class Data {
	
	private List<Sample> trainingSet;
	
	public Data(String filename) {
		this.trainingSet = new ArrayList<Sample>();
		parseFile(filename);
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
				String[] inAndOut = line.split(":");
				double[] input = processInput(inAndOut[0]);
				double[] output = processOutput(inAndOut[1]);
				trainingSet.add(new Sample(input, output));
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

	private double[] processOutput(String outString) {
		String data = outString.substring(1, outString.length() - 1);
		String[] parts = data.split(",");
		double[] input = new double[parts.length];
		int i = 0;
		for(String s : parts) {
			input[i++] = Double.parseDouble(s);
		}
		return input;
	}

	private double[] processInput(String inputString) {
		String data = inputString.substring(1, inputString.length() - 1);
		String[] parts = data.split(",");
		double[] input = new double[parts.length + 1];
		input[0] = 1; //da mogu raditi vektorizaciju s matricama
		//thete će biti veličine (broj neurona u slijedećem sloju x broj neurona u trentunom sloju + 1)
		
		//input će originalno biti 1 x broj ulaza, broj ulaza = 4 u našem slučaju
		
		//broj neurona u prvom sloju će zato biti 4, ali pošto dodajemo 1 kod thete, onda moramo
		//nekako posložiti to da se može izmnožiti pa dodajemo prvu komponentu 1 jer ona neće ništa
		//promijeniti
		int i = 1;
		for(String s : parts) {
			input[i++] = Double.parseDouble(s);
		}
		return input;
	}
	
	public static void main(String[] args) {
		SigmoidTransferFunction s = new SigmoidTransferFunction();
		double[][] a = {{3,4,5},{1,7,9},{5,1,3}};
		System.out.println(Arrays.toString(s.valueAt(new Array2DRowRealMatrix(a)).getRow(0)));
		System.out.println(Arrays.toString(s.valueAt(new Array2DRowRealMatrix(a)).getRow(1)));
		System.out.println(Arrays.toString(s.valueAt(new Array2DRowRealMatrix(a)).getRow(2)));
	}
}
