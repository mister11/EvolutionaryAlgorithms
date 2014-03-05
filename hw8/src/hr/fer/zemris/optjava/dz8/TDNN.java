package hr.fer.zemris.optjava.dz8;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Feed-forward artificial neural network
 * @author Sven Vidak
 *
 */
public class TDNN implements INeuralNetwork {
	
	private int[] layers;
	private ITransferFunction[] layerFunction;
	
	public TDNN(int[] layers, ITransferFunction[] layerFunction) {
		this.layers = layers;
		this.layerFunction = layerFunction;
	}
	
	@Override
	public double[] calcOutputs(double[] inputs, double[] weights) {
		RealMatrix result = new Array2DRowRealMatrix(inputs);
		int size = this.layers.length;
		int index = 0;//for traversing weights array
		for(int i=0; i < size - 1; i++) {
			int weightsRowsNeeded = this.layers[i+1];
			int weightsColsNeeded = this.layers[i] + 1;
			RealMatrix weightsMatrix = getWeightMatrix(weights, index, weightsRowsNeeded, weightsColsNeeded);
			index += weightsRowsNeeded * weightsColsNeeded;
			result = weightsMatrix.multiply(result);
			result = layerFunction[i].valueAt(result);
			if(i < size - 2) {
				result = addOnes(result);//dodajem 1 zbog istog razloga kao i prije, ali ne i na kraju
			}
		}
		return result.getColumn(0);
	}
	
	private RealMatrix addOnes(RealMatrix result) {
		double[][] res = result.getData();
		int rows = res.length;
		int cols = res[0].length;
		double[][] newRes = new double[rows+1][cols];
		for(int i=0; i < cols; i++) {
			newRes[0][i] = 1;
		}
		for(int i=1; i < rows+1; i++) {
			for(int j=0; j < cols; j++) {
				newRes[i][j] = res[i-1][j];
			}
		}
		return new Array2DRowRealMatrix(newRes);
	}

	private RealMatrix getWeightMatrix(double[] weights, int index, int weightsRowsNeeded, int weightsColsNeeded) {
		double[][] matrix = new double[weightsRowsNeeded][weightsColsNeeded];
		int x = index;
		for(int i=0; i < weightsRowsNeeded; i++) {
			for(int j=0; j < weightsColsNeeded; j++) {
				matrix[i][j] = weights[x++];
			}
		}
		return new Array2DRowRealMatrix(matrix);
	}

	@Override
	public int getWeightsCount() {
		int cnt = 0;
		int size = this.layers.length;
		for(int i=0; i < size - 1; i++) {
			cnt += this.layers[i] * this.layers[i+1];
			if(i > 0) {
				cnt += this.layers[i];
			}
		}
		cnt += this.layers[this.layers.length - 1];
		return cnt;
	}

	@Override
	public int[] getLayers() {
		return this.layers;
	}

	@Override
	public void setContext(double[] context) {
	}
	@Override
	public double[] getContext() {
		return null;
	}
}
