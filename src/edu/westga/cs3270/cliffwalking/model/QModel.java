package edu.westga.cs3270.cliffwalking.model;

import edu.westga.cs3270.cliffwalking.interfaces.Action;
import edu.westga.cs3270.cliffwalking.interfaces.State;
import edu.westga.cs3270.cliffwalking.interfaces.World;

public class QModel {
	
	private final World world;
	private double learningRate;
	private double discountRate;
	
	public QModel(World world, double learningRate, double discountRate) {
		this.world = world;
		this.learningRate = learningRate;
		this.discountRate = discountRate;
	}
	
	public void learn(State s1, Action a1, State s2, Action a2) {
		double q1 = this.world.getPolicyValue(s1, a1);
		double q2 = this.world.getPolicyValue(s2, a2);
		double r = s1.getReward();
		double d = q1 + this.learningRate * (r + this.discountRate * q2 - q1);
		
		this.world.setPolicyValue(s1, a1, d);
	}

}
