package edu.westga.cs3270.cliffwalking.model;

public class ValueIteration extends MarkovDecisionProcess {

	private double discountFactor;
	
	public ValueIteration(World theWorld, double theDiscountFactor) {
		super(theWorld);	
		this.discountFactor = theDiscountFactor;
	}
	
	public void calculateValue(State state) {
		double result = Double.NEGATIVE_INFINITY;
		if (!getWorld().isGoalState(state) ) {
			for (Action action : getWorld().getActions()) {
				double sum = 0;
				for (SuccessorState statePrime : this.getWorld()
						.getProbability()
						.determineSuccessorState(state, action)) {
					sum += statePrime.getProbability()
							* statePrime.getState().getPolicyValue()[0];
				}
				sum *= this.discountFactor;

				result = Math.max(result, sum);
			}

			state.getPolicyValue()[0] = result+state.getReward();
		} else {
			state.getPolicyValue()[0] = state.getReward();
		}
	}

	public void iteration() {
		for(State state: getWorld().getStates() ) {
			calculateValue(state);
		}
	}
	
}
