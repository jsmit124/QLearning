package edu.westga.cs3270.cliffwalking.interfaces;

import java.util.Set;

import edu.westga.cs3270.cliffwalking.model.SuccessorState;

public interface ActionProbability {
	
	double calculate(State targetState, State currentState, Action action);
	Set<SuccessorState> determineSuccessorState(State state, Action action);

}
