package edu.westga.cs3270.cliffwalking.model;

public interface AgentPolicy {
	
	Action determineNextAction(WorldAgent agent);
	World getWorld();

}
