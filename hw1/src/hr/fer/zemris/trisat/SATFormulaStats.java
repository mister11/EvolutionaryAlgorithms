package hr.fer.zemris.trisat;

public class SATFormulaStats {

	public static final int NUM_OF_BEST = 2;
	private static final double PER_UP = 0.01;
	private static final double PER_DOWN = 0.1;
	private static final int PER_UNIT_AMOUNT = 50;

	private SATFormula formula;
	private BitVector assignment;
	private int satisfied;
	private boolean isSatisfied;
	private double[] post;

	public SATFormulaStats(SATFormula formula) {
		this.formula = formula;
		this.post = new double[this.formula.getNumberOfClauses()];
	}

	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		this.satisfied = 0;
		this.assignment = assignment;
		int numOfClause = this.formula.getNumberOfClauses();
		for (int i = 0; i < numOfClause; i++) {
			if (this.formula.getClause(i).isSatisfied(assignment)) {
				this.satisfied++;
				if(updatePercentages) {
					post[i] += (1-post[i])*PER_UP;
				}
			} else {
				if(updatePercentages) {
					post[i] += (0-post[i])*PER_DOWN;
				}
			}
		}
		this.isSatisfied = this.satisfied == this.formula.getNumberOfClauses();
	}

	public int getNumberOfSatisfied() {
		return this.satisfied;
	}

	public boolean isSatisfied() {
		return this.isSatisfied;
	}

	public double getPercentageBonus() {
		int z = this.satisfied;
		int numOfClauses = this.formula.getNumberOfClauses();
		for(int i=0; i < numOfClauses; i++) {
			if(this.formula.getClause(i).isSatisfied(this.assignment)) {
				z += PER_UNIT_AMOUNT * (1-post[i]);
			} else {
				z += -PER_UNIT_AMOUNT * (1-post[i]);
			}
		}
		return z;
	}

	// procjena postotka
	public double getPercentage(int index) {
		return this.post[index];
	}
}