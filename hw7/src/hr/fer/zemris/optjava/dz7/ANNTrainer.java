package hr.fer.zemris.optjava.dz7;

import java.util.Arrays;

import hr.fer.zemris.optjava.dz7.clonalg.ClonAlg;
import hr.fer.zemris.optjava.dz7.pso.GlobalNeighborhood;
import hr.fer.zemris.optjava.dz7.pso.LocalNeighborhood;
import hr.fer.zemris.optjava.dz7.pso.Neighborhood;
import hr.fer.zemris.optjava.dz7.pso.PSO;
import hr.fer.zemris.optjava.dz7.pso.Solution;


public class ANNTrainer {

	public static void main(String[] args) {
		
		Data d = new Data(args[0]);
		int[] layers = {d.getInputSize() - 1, 5, 3, d.getOutputSize()}; //-1 zbog dodanih jedinica
		ITransferFunction[] fun = {new SigmoidTransferFunction(), new SigmoidTransferFunction(), new SigmoidTransferFunction()};
		FFANN f = new FFANN(layers, fun);
		Evaluation e = new Evaluation(f, d, new RootMeanSquareError());
		
		String algorithm = args[1];
		int n = Integer.parseInt(args[2]);
		double minError = Double.parseDouble(args[3]);
		int maxIters = Integer.parseInt(args[4]);
		
		if(algorithm.startsWith("pso")) {
			String[] parts = algorithm.split("-");
			if(parts.length == 2) {
				Neighborhood neighborhood = new GlobalNeighborhood();
				Solution sol = new PSO(e, n, f.getWeightsCount(), minError, maxIters, neighborhood).run();
				evaluatePSO(sol, e, d);
			} else if(parts.length == 3) {
				int localSize = Integer.parseInt(parts[2]);
				Neighborhood neighborhood = new LocalNeighborhood(localSize);
				Solution sol = new PSO(e, n, f.getWeightsCount(), minError, maxIters, neighborhood).run();
				evaluatePSO(sol, e, d);
			}
		} else if(algorithm.startsWith("clonalg")) {
				hr.fer.zemris.optjava.dz7.clonalg.Solution sol = new ClonAlg(e, n, f.getWeightsCount(), minError, maxIters).run();
				evaluateClonAlg(sol, e, d);
		} else {
			System.err.println("Unsupported algorithm type.");
			System.exit(-1);
		}
		
	}

	private static void evaluateClonAlg(hr.fer.zemris.optjava.dz7.clonalg.Solution sol, Evaluation e, Data d) {
		e.evaluate(sol.weights);
		double[][] outputs = e.getOutputs();
		int size = d.getTrainingSetSize();
		int misses = 0;
		for(int i=0; i < size; i++) {
			boolean isSame = true;
			double[] output = round(outputs[i]);
			double[] realOut = d.getOutput(i);
			int length = output.length;
			for(int j=0; j < length; j++) {
				if(output[j] != realOut[j]) {
					isSame = false;
				}
			}
			if(!isSame) {
				misses++;
			}
			System.out.print("Out: " + Arrays.toString(output)
					+ " Real: " + Arrays.toString(realOut)
					+ "\t" + (isSame ? "is same" : "not same") + "\n");
			
		}
		System.out.println("Misses: " + misses);
	}

	private static void evaluatePSO(Solution sol, Evaluation e, Data d) {
		e.evaluate(sol.weights);
		double[][] outputs = e.getOutputs();
		int size = d.getTrainingSetSize();
		int misses = 0;
		for(int i=0; i < size; i++) {
			boolean isSame = true;
			double[] output = round(outputs[i]);
			double[] realOut = d.getOutput(i);
			int length = output.length;
			for(int j=0; j < length; j++) {
				if(output[j] != realOut[j]) {
					isSame = false;
				}
			}
			if(!isSame) {
				misses++;
			}
			System.out.print("Out: " + Arrays.toString(output)
					+ " Real: " + Arrays.toString(realOut)
					+ "\t" + (isSame ? "is same" : "not same") + "\n");
			
		}
		System.out.println("Misses: " + misses);
	}

	private static double[] round(double[] d) {
		double[] out = new double[d.length];
		for(int i=0; i < d.length; i++) {
			out[i] = d[i] < 0.5 ? 0 : 1;
		}
		return out;
	}

}
