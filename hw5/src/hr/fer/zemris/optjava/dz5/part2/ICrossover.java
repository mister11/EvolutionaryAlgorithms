package hr.fer.zemris.optjava.dz5.part2;

import java.util.Random;

public interface ICrossover {

	Kromosom crossover(Kromosom parent1, Kromosom parent2, Random r);
}
