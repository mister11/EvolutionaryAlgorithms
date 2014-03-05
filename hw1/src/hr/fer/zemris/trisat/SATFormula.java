package hr.fer.zemris.trisat;

import java.util.List;

public class SATFormula {
	
	private int numberOfVariable;
	private List<Clause> clauses;
	
	public SATFormula(int numberOfVariables, List<Clause> clauses) {
		this.numberOfVariable = numberOfVariables;
		this.clauses = clauses;
	}

	public int getNumberOfVariables() {
		return this.numberOfVariable;
	}

	public int getNumberOfClauses() {
		return this.clauses.size();
	}

	public Clause getClause(int index) {
		return this.clauses.get(index);
	}

	public boolean isSatisfied(BitVector assignment) {
		for(Clause c : this.clauses) {
			if(!c.isSatisfied(assignment)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return null;
	}
}