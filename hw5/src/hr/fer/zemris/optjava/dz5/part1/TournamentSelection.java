package hr.fer.zemris.optjava.dz5.part1;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TournamentSelection implements ISelection {

	@Override
	public Kromosom select(List<Kromosom> population, int k, Random r) {
		int size = population.size();
		boolean[] pickedIndex = new boolean[size];
		Kromosom[] pool = new Kromosom[k];
		int i = 0;
		while(i < k) {
			int index = r.nextInt(size);
			if(pickedIndex[index]) {
				continue;
			}
			pickedIndex[index] = true;
			pool[i++] = population.get(index);
		}
		Arrays.sort(pool);
		return pool[pool.length - 1];
	}


}
