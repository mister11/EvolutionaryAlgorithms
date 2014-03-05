package hr.fer.zemris.optjava.dz3;

public interface IOptAlgorithm<T extends SingleObjectiveSolution> {
	
	T run();
}
