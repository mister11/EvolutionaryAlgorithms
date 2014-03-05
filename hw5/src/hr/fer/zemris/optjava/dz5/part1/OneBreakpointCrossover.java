package hr.fer.zemris.optjava.dz5.part1;

import java.util.Random;

public class OneBreakpointCrossover implements ICrossover {

	@Override
	public Kromosom crossover(Kromosom parent1, Kromosom parent2, Random r, IFunction function) {
		int size = parent1.solution.length;
		int breakIndex = r.nextInt(size);
		Kromosom child1 = new Kromosom(size);
		Kromosom child2 = new Kromosom(size);
		for(int i=0; i < breakIndex; i++) {
			child1.solution[i] = parent1.solution[i];
			child2.solution[i] = parent2.solution[i];
		}
		for(int i=breakIndex; i < size; i++) {
			child1.solution[i] = parent2.solution[i];
			child2.solution[i] = parent1.solution[i];
		}
		return (function.valueAt(child1.solution) > function.valueAt(child2.solution)) ? child1 : child2;
	}

}
