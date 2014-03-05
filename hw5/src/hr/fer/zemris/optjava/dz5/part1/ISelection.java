package hr.fer.zemris.optjava.dz5.part1;

import java.util.List;
import java.util.Random;

public interface ISelection {
	
	Kromosom select(List<Kromosom> population, int k, Random r);

}
