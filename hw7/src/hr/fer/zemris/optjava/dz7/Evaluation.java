package hr.fer.zemris.optjava.dz7;

public class Evaluation {

	private FFANN network;
	private Data data;
	private IErrorFunction errorFun;
	private double[][] outputs;
	
	public Evaluation(FFANN network, Data data, IErrorFunction errorFun) {
		this.network = network;
		this.data = data;
		this.errorFun = errorFun;
		this.outputs = new double[data.getTrainingSetSize()][data.getOutput(0).length];
	}

	public double evaluate(double[] weights) {
		int size = data.getTrainingSetSize();
		for(int i=0; i < size; i++) {
			Sample sample = data.getSample(i);
			this.outputs[i] = network.calcOutputs(sample.input, weights);
		}
		return 1.0 / data.getTrainingSetSize() * errorFun.errorAt(data, outputs);
	}
	
	public double[][] getOutputs() {
		return outputs;
	}
}
