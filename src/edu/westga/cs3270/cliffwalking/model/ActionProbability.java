package edu.westga.cs3270.cliffwalking.model;

import java.util.Set;

public interface ActionProbability {
	
	double calculate(State targetState, State currentState, Action action);
	Set<SuccessorState> determineSuccessorState(State state, Action action);

}
