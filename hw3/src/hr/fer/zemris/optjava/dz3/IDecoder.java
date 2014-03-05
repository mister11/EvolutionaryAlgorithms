package hr.fer.zemris.optjava.dz3;

public interface IDecoder<T extends SingleObjectiveSolution> {

	double[] decode(T vector);
	void decode(T vector, double[] realVector);
	
}
