package hr.fer.zemris.optjava.dz8;


public class Evaluation {

	private INeuralNetwork network;
	private Data data;
	private IErrorFunction errorFun;
	private double[][] outputs;
	
	public Evaluation(INeuralNetwork network, Data data, IErrorFunction errorFun) {
		this.network = network;
		this.data = data;
		this.errorFun = errorFun;
		this.outputs = new double[data.getTrainingSetSize()][data.getOutput(0).length];
	}

	public double evaluate(double[] weights) {
		int size = data.getTrainingSetSize();
		double[] context = getInitContext(weights);
		network.setContext(context);
		for(int i=0; i < size; i++) {
			Sample sample = data.getSample(i);
			this.outputs[i] = network.calcOutputs(sample.input, weights);
		}
		return 1.0 / data.getTrainingSetSize() * errorFun.errorAt(data, outputs);
	}
	
	public double[][] getOutputs() {
		return outputs;
	}
	
	private double[] getInitContext(double[] weights) {
		double[] context = new double[network.getLayers()[1]];
		int index = 0;
		for(int i=weights.length - network.getLayers()[1]; i < weights.length; i++) {
			context[index++] = weights[i];
		}
		return context;
	}
}
