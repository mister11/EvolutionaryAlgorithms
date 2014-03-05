package hr.fer.zemris.optjava.dz5.part1;

public class Function implements IFunction {

	@Override
	public double valueAt(boolean[] solution) {
		int size = solution.length;
		int k = 0;
		for(int i=0; i < size; i++) {
			if(solution[i]) {
				k++;
			}
		}
		if(k <= 0.8 * size) {
			return (double)k / size;
		}
		if(k > 0.8 * size && k <= 0.9 * size) {
			return 0.8;
		}
		return (2*(double)k / size) - 1;
	}

}
