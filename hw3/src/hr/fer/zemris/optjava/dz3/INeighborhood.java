package hr.fer.zemris.optjava.dz3;

public interface INeighborhood<T extends SingleObjectiveSolution> {
	
	T randomNeighbor(T vector);
	
}
