package hr.fer.zemris.optjava.dz7;

public class RootMeanSquareError implements IErrorFunction {
	
	@Override
	public double errorAt(Data data, double[][] outputs) {
		int size = data.getTrainingSetSize();
		double error = 0.0;
		for(int i=0; i < size; i++) {
			double[] realOutput = data.getOutput(i);
			int outSize = outputs[0].length;
			if(outSize != realOutput.length) {
				System.err.println("Fatal error");
				System.exit(-1);
			}
			for(int j=0; j < outSize; j++) {
				double x = realOutput[j];
				double y = outputs[i][j];
				error += (x - y) * (x - y);
			}
		}
		return error;
	}

}
