package hr.fer.zemris.optjava.dz3;

public class GeomTempSchedule implements ITempSchedule {

	private double alpha;
	private double init;
	private double curr;
	private int innerLimit;
	private int outerLimit;
	
	
	//da određivanje alfe bude bolje, ja sam predao početnu i min. temperaturu pa preko toga našao alfu
	//nije neka velika razlika, ali je dobro u slučaju puno iteracija, da min. temperatura nikad neće otići skroz u 0
	public GeomTempSchedule(double minTemp, double init, int innerLimit,
			int outerLimit) {
		super();
		this.alpha = Math.pow((minTemp / init), 1 / (outerLimit - 1));
		this.init = init;
		this.innerLimit = innerLimit;
		this.outerLimit = outerLimit;
		this.curr = this.init;
	}

	@Override
	public double getNextTemperature() {
		this.curr = alpha * this.curr;
		return this.curr;
	}

	@Override
	public int getInnerLoopCounter() {
		return this.innerLimit;
	}

	@Override
	public int getOuterLoopCounter() {
		return this.outerLimit;
	}
	
}
