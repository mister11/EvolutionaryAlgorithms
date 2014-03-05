package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<SingleObjectiveSolution> {

	private IDecoder<T> decoder;
	private INeighborhood<T> neighborhood;
	private T startWith;
	private IFunction function;
	private ITempSchedule tempSchedule;
	private boolean minimize;
	private Random rand;

	public SimulatedAnnealing(IDecoder<T> decoder,
			INeighborhood<T> neighborhood, T startWith, IFunction function,
			ITempSchedule tempSchedule, boolean minimize) {
		super();
		this.decoder = decoder;
		this.neighborhood = neighborhood;
		this.startWith = startWith;
		this.function = function;
		this.tempSchedule = tempSchedule;
		this.minimize = minimize;
		this.rand = new Random();
	}


	@Override
	public SingleObjectiveSolution run() {
		int inner = tempSchedule.getInnerLoopCounter();
		int outer = tempSchedule.getOuterLoopCounter();
		T optim = this.startWith;
		optim.fitness = this.function.valueAt(this.decoder.decode(startWith));
		for(int i=0; i < outer; i++) {
			double t = tempSchedule.getNextTemperature();
			for(int j=0; j < inner; j++) {
				
				double[] start = this.decoder.decode(startWith);
				T neighbor = this.neighborhood.randomNeighbor(startWith);
				double[] neigh = this.decoder.decode(neighbor);
				double fStart = this.function.valueAt(start);
				double fNeigh = this.function.valueAt(neigh);
				double delta;
				if(minimize) {
					delta = fNeigh - fStart;
				} else {
					delta = fStart - fNeigh;
				}
				if(delta <= 0) {
					this.startWith = neighbor;
				} else {
					double p = Math.exp(-delta / t);
					this.startWith = (rand.nextDouble() <= p) ? neighbor : startWith;
				}
				this.startWith.fitness = this.function.valueAt(this.decoder.decode(startWith));
				if(this.startWith.compareTo(optim) < 0) {
					optim = this.startWith;
				}
			}
		}
		System.out.println("Found solution:\n");
		System.out.println(Arrays.toString(this.decoder.decode(optim)));
		return optim;
	}

}
