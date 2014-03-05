package hr.fer.zemris.optjava.dz3;

public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

	
	@Override
	public double[] decode(DoubleArraySolution vector) {
		int size = vector.values.length;
		double[] decoded = new double[size];
		for(int i=0; i < size; i++) {
			decoded[i] = vector.values[i];
		}
		return decoded;
	} 

	@Override
	public void decode(DoubleArraySolution vector, double[] realVector) {
		int size = vector.values.length;
		for(int i=0; i < size; i++) {
			realVector[i] = vector.values[i];
		}
	}

	
}
