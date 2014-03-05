package hr.fer.zemris.optjava.dz8;


public interface INeuralNetwork {
	
	public double[] calcOutputs(double[] inputs, double[] weights);
	
	public int getWeightsCount();
	
	public int[] getLayers();
	
	public void setContext(double[] context);
	
	public double[] getContext();
}
