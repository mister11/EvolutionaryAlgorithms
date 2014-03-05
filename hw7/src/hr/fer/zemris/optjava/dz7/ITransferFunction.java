package hr.fer.zemris.optjava.dz7;

import org.apache.commons.math3.linear.RealMatrix;

public interface ITransferFunction {

	RealMatrix valueAt(RealMatrix z);
}
