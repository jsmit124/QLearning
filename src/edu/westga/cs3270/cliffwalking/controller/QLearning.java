package edu.westga.cs3270.cliffwalking.controller;

public class QLearning {
	
    private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future
    private final double epsilon = 0.1; // Greedy policy 

    private final int mazeWidth = 12;
    private final int mazeHeight = 4;
    private final int statesCount = mazeHeight * mazeWidth;

    private final int reward = -1;
    private final int penalty = -100;
    private final int goalReward = 0;

    private char[][] maze;  // Maze read from file
    private int[][] R;       // Reward lookup
    private double[][] Q;    // Q learning
    
    String[] actions = {"up", "down", "left", "right"};
    boolean restart = false;
    
    
    public QLearning() {
    	
    }
    
    public void setupWorld() {
    	
    }
    
    

}
