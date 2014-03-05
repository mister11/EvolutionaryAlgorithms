package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.de.DifferentialEvolution;
import hr.fer.zemris.optjava.dz8.de.ExponentialCrossover;
import hr.fer.zemris.optjava.dz8.de.Solution;
import hr.fer.zemris.optjava.dz8.de.UniformCrossover;

import java.util.Arrays;



public class ANNTrainer {

	private static final int a = 200;
	
	
	public static void main(String[] args) {
		
		String action = args[1];
		String net = action.substring(action.indexOf("-") + 1);
		String[] strLayers = net.split("x");
		int[] layers = getLayers(strLayers);
		
		
		int n = Integer.parseInt(args[2]);
		double minError = Double.parseDouble(args[3]);
		int maxIters = Integer.parseInt(args[4]);
		
		if(action.startsWith("tdnn")) {
			Data d = new Data(args[0], layers[0], a);
			ITransferFunction[] fun = fillFuntions(layers.length - 1);
			INeuralNetwork nn = new TDNN(layers, fun);
			Evaluation e = new Evaluation(nn, d, new RootMeanSquareError());
			Solution s = new DifferentialEvolution(e, new UniformCrossover(), n, nn.getWeightsCount(), minError, maxIters).run();
			evaluate(s, nn, d, layers[0]);
		} else if(action.startsWith("elman")) {
			Data d = new Data(args[0], 1, a);
			ITransferFunction[] fun = fillFuntions(layers.length - 1);
			INeuralNetwork nn = new ElmansNN(layers, fun);
			Evaluation e = new Evaluation(nn, d, new RootMeanSquareError());
			Solution s = new DifferentialEvolution(e, new UniformCrossover(), n, nn.getWeightsCount(), minError, maxIters).run();
			evaluate(s, nn, d, layers[0]);
		} else {
			System.err.println("You should really enter some algorithm thta is supported");
			System.exit(-1);
		}		
		
	}

	private static void evaluate(Solution sol, INeuralNetwork nn, Data d, int inputSize) {
		//uzmi zadnjih l, od sizeOfTrainSet
		//pukni ih u NN s danim weightovima
		//ispiši output
		//dodaj kao novi ulaz
		//nastavi petljat
		int size = d.getTrainingSetSize();
		double[] input = d.getInput(size - 1);
		double[] output = nn.calcOutputs(input, sol.weights);
		
		System.out.println("Output: " + Arrays.toString(output));
	}
	
	private static int[] getLayers(String[] strLayers) {
		int size = strLayers.length;
		int[] layers = new int[size];
		for(int i=0; i < size; i++) {
			layers[i] = Integer.parseInt(strLayers[i]);
		}
		return layers;
	}

	private static ITransferFunction[] fillFuntions(int n) {
		ITransferFunction[] fun = new ITransferFunction[n];
		for(int i=0; i < n; i++) {
			fun[i] = new HyperbolicTangentFunction();
		}
		return fun;
	}

	

}
