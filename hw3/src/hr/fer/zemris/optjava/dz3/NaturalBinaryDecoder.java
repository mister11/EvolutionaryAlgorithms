package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;

public class NaturalBinaryDecoder implements IDecoder<BitVectorSolution> {

	private double[] mins;
	private double[] maxs;
	private int k;
	private int n;
	
	
	public NaturalBinaryDecoder(double[] mins, double[] maxs, int k, int n) {
		super();
		this.mins = mins;
		this.maxs = maxs;
		this.k = k;
		this.n = n;
	}
	
	public NaturalBinaryDecoder(double mins, double maxs, int k, int n) {
		super();
		this.mins = new double[n];
		this.maxs = new double[n];
		for(int i=0; i < n; i++) {
			this.mins[i] = mins;
			this.maxs[i] = maxs;
		}
		this.k = k;
		this.n = n;
	}
	
	
	@Override
	public double[] decode(BitVectorSolution vector) {
		double[] decoded = new double[this.n];
		for(int i=0; i < vector.bits.length - k + 1; i += k) {
			int x = getK(Arrays.copyOfRange(vector.bits, i, i+k));
			int idx = i / k;
			decoded[idx] = this.mins[idx] + x*(this.maxs[idx] - this.mins[idx]) / (Math.pow(2, k) - 1);
		}
		return decoded;
	}
	
	private int getK(byte[] vector) {
		int k = 0;
		for(int i=vector.length-1, x=0; i >= 0; i--, x++) {
			if(vector[i] == 1) {
				k += Math.pow(2, x);
			}
		}
		return k;
	}


	@Override
	public void decode(BitVectorSolution vector, double[] realVector) {
		for(int i=0; i < vector.bits.length - k + 1; i += k) {
			int x = getK(Arrays.copyOfRange(vector.bits, i, i+k));
			int idx = i / k;
			realVector[idx] = this.mins[idx] + x*(this.maxs[idx] - this.mins[idx]) / (Math.pow(2, k) - 1);
		}
	}
}
