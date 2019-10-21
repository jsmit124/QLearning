package edu.westga.cs3270.cliffwalking.model;

import edu.westga.cs3270.cliffwalking.interfaces.State;
import edu.westga.cs3270.cliffwalking.interfaces.World;

public class MarkovDecisionProcess {
	
	private final World world; 
	private final State goal;
	
	public MarkovDecisionProcess(World theWorld) {
		this.world = theWorld;
		this.goal = theWorld.getGoals().get(0);
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @return the goal
	 */
	public State getGoal() {
		return goal;
	}

}
