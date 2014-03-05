package hr.fer.zemris.optjava.dz5.part2;

public class Function implements IFunction {
	
	private Data data;
	
	public Function(Data data) {
		this.data = data;
	}
	
	@Override
	public double valueAt(int[] locationPermutation) {
		int[][] distances = this.data.getDistances();
		int[][] costs = this.data.getCosts();
		double totalCost = 0;
		int size = locationPermutation.length;
		for(int i=0; i < size; i++) {
			for(int j=0; j < size; j++) {
				totalCost += costs[i][j] * 
						distances[locationPermutation[i]][locationPermutation[j]];
			}
		}
		return totalCost;
	}

}
