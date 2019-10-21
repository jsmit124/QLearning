package edu.westga.cs3270.cliffwalking.interfaces;

public interface WorldAgent {
	
	State getCurrentState();
	void setCurrentState(State s);
	AgentPolicy getPolicy();
	void setAgentPolicy(AgentPolicy policy);
	void tick();
	World getWorld();
	void setWorld(World world);

}