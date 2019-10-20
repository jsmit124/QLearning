package edu.westga.cs3270.cliffwalking.model;

public interface PerformAction {
	
	void perform(World world, State state, Action action);
	double determineCost(World world, State state, Action action);
	boolean isPossible(World world, State state, Action action);
}