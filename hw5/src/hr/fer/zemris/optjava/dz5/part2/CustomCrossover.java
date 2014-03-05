package hr.fer.zemris.optjava.dz5.part2;

import java.util.Random;


/**
 * Ovo je neki crossover koji je radio i u prethodnoj zadaći koji nije u knjizi, ali se pokazao dobrim....
 * @author Sven Vidak
 *
 */
public class CustomCrossover implements ICrossover {

	@Override
	public Kromosom crossover(Kromosom parent1, Kromosom parent2, Random r) {
		int size = parent1.locationPermutation.length;
		int randIndex = r.nextInt(size);
		Kromosom child = new Kromosom(size);
		int[] parent2sol = getSolution(parent2);
		for(int i=0; i < randIndex; i++) {
			int sol = parent1.locationPermutation[i];
			child.locationPermutation[i] = sol;
			parent2sol = foundAndDelete(parent2sol, sol);
		}
		int a = randIndex;
		for(int i=0; i < size; i++) {
			if(parent2sol[i] == -1) {
				continue;
			}
			child.locationPermutation[a++] = parent2sol[i];
		}
		return child;
		
	}

	private static int[] foundAndDelete(int[] parent2sol, int a) {
		int size = parent2sol.length;
		for(int i=0; i < size; i++) {
			if(parent2sol[i] == a) {
				parent2sol[i] = -1;
				break;
			}
		}
		return parent2sol;
	}


	private static int[] getSolution(Kromosom parent) {
		int size = parent.locationPermutation.length;
		int[] sol = new int[size];
		for(int i=0; i < size; i++) {
			sol[i] = parent.locationPermutation[i];
		}
		return sol;
	}

}