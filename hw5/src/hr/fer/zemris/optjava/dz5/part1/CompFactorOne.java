package hr.fer.zemris.optjava.dz5.part1;


public class CompFactorOne implements ICompFactor {

	private double startingFactor;
	private double step;
	
	public CompFactorOne(double startingFactor, double initStep, double step) {
		this.startingFactor = (startingFactor == 0) ? initStep : startingFactor;
		this.step = step;
	}
	
	@Override
	public double getNextFactor() {
		this.startingFactor += this.startingFactor*step;
		if(this.startingFactor > 0.9999) {
			this.startingFactor = 1;
		}
		return this.startingFactor;
	}

}
