package hr.fer.zemris.optjava.dz5.part1;

import java.util.Random;

public class TwoBreakpointCrossover implements ICrossover {

	@Override
	public Kromosom crossover(Kromosom parent1, Kromosom parent2, Random r,
			IFunction function) {
		int size = parent1.solution.length;
		Kromosom child1 = new Kromosom(size);
		Kromosom child2 = new Kromosom(size);
		int firstIndex = r.nextInt(size);
		int secondIndex = r.nextInt(size);
		while(firstIndex == secondIndex) {
			secondIndex = r.nextInt(size);
		}
		for(int i=0; i < firstIndex; i++) {
			child1.solution[i] = parent1.solution[i];
			child2.solution[i] = parent2.solution[i];
		}
		for(int i=firstIndex; i < secondIndex; i++) {
			child1.solution[i] = parent2.solution[i];
			child2.solution[i] = parent1.solution[i];
		}
		for(int i=secondIndex; i < size; i++) {
			child1.solution[i] = parent1.solution[i];
			child2.solution[i] = parent2.solution[i];
		}
		return (function.valueAt(child1.solution) > function.valueAt(child2.solution)) ? child1 : child2;
	}

}
