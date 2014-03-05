package hr.fer.zemris.optjava.dz7;

public class Main {

	public static void main(String[] args) {
		
		int[] layers = {3, 2, 2};
		ITransferFunction[] fun = {new SigmoidTransferFunction(), new SigmoidTransferFunction()};
		FFANN f = new FFANN(layers, fun);
		
		double[] weights = new double[f.getWeightsCount()];
		for(int i=0; i < weights.length; i++) {
			weights[i] = 0.1;
		}
		
		f.calcOutputs(new double[] {1, 5, 6, 7}, weights);
	}

}
