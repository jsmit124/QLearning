package edu.westga.cs3270.cliffwalking.controller;

//Sources cited: https://github.com/cvhu/CliffWalking

import java.util.*;
import edu.westga.cs3270.cliffwalking.model.State;

/**
 * Stores controlling information for the Cliff Walking Q-Learning exercise
 * 
 * @author jsmit124
 * @version a lot
 *
 */
public class QLearning {	
	private int width;
	private int length;

	/**
	 * Constructs a cliff walking object of specified height and width
	 * 
	 * @param width
	 * 		width of the world
	 * @param length
	 * 		length of the world
	 */
	public QLearning(int width, int length) {
		this.width = width;
		this.length = length;
	}

	//handles the greedy policy
	private String greedyPolicy(HashMap<String, Double> qa, double eta) {
		int maxCount = 0;
		int totalCount = 0;
		Double maxValue = Double.NEGATIVE_INFINITY;
		Random rand = new Random();
		LinkedList<String> actions = new LinkedList<String>();
		LinkedList<Double> probabilities = new LinkedList<Double>();
		
		for (String action : qa.keySet()) {
			totalCount++;
			Double currValue = qa.get(action);
			int compare = currValue.compareTo(maxValue);
			if (compare > 0) {
				maxValue = currValue;
				maxCount = 1;
			} else if (compare == 0) {
				maxCount++;
			}
		}
		
		Double oldProbability = 0.0;
		Double exploreProbability = eta / totalCount;
		Double greedyProbability = (1.0 - eta) / maxCount + exploreProbability;
		
		for (String action : qa.keySet()) {
			Double currValue = qa.get(action);
			if (currValue.compareTo(maxValue) == 0) {
				oldProbability += greedyProbability;
			} else {
				oldProbability += exploreProbability;
			}
			
			actions.add(action);
			probabilities.add(oldProbability);
		}
		
		double random = rand.nextDouble();
		
		for (int i = 0; i < totalCount; i++) {
			if (random < probabilities.get(i)) {
				return actions.get(i);
			}
		}
		
		return actions.get(totalCount - 1);
	}

	/**
	 * Compares two q values and returns the maximum of the two
	 * 
	 * @param qa
	 * 		the current q value
	 * 
	 * @return
	 * 		the maximum q value
	 */
	public Double getMaxQValue(HashMap<String, Double> qa) {
		Double maxValue = Double.NEGATIVE_INFINITY;
		
		for (String action : qa.keySet()) {
			Double currValue = qa.get(action);
			int compare = currValue.compareTo(maxValue);
			if (compare > 0) {
				maxValue = currValue;
			}
		}
		return maxValue;
	}

	/**
	 * Performs the Q learning algorithm
	 * 
	 * @param epsilon
	 * 		the greedy policy
	 * @param alpha
	 * 		the learning rate
	 * @param gamma
	 * 		discount rate of the reward
	 * @param amountOfIterations
	 * 		number of times for the algorithm to run
	 * 
	 * @return
	 * 		the current qValues of the states at the end of the number of iterations
	 */
	public HashMap<String, HashMap<String, Double>> qLearning(double epsilon, double alpha, double gamma, int amountOfIterations) {		
		HashMap<String, HashMap<String, Double>> qValues = new HashMap<String, HashMap<String, Double>>();
		State cliffState = new State(this.width, this.length);

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.length; j++) {
				HashMap<String, Double> moves = new HashMap<String, Double>();
				moves.put("down", 0.5);
				moves.put("up", 0.5);
				moves.put("right", 0.5);
				moves.put("left", 0.5);
				qValues.put(String.format("(%d, %d)", i, j), moves);
			}
		}

		for (int i = 0; i < amountOfIterations; i++) {
			cliffState.startNewEpisode();
			String state = cliffState.getState();	
			
			while (!cliffState.terminate()) {
				String action = this.greedyPolicy(qValues.get(state), epsilon);
				cliffState.action(action);
				int reward = cliffState.getReward();
				String newState = cliffState.getState();				
				HashMap<String, Double> qa = qValues.get(state);
				
				Double currQValue = qa.get(action) + alpha * (reward + gamma * (this.getMaxQValue(qValues.get(newState))) - qa.get(action));
				qa.put(action, currQValue);
				qValues.put(state, qa);
				state = newState;
				System.out.printf("%d: %s-%s %f" + System.lineSeparator(), i, state, action, currQValue);
			}			
		}
		return qValues;
	}

	/**
	 * Prints the policy in format of width x length
	 * 
	 * @param qValues
	 * 		map containing the qValues
	 */
	public void formatEpisodePolicy(HashMap<String, HashMap<String, Double>> qValues){
		HashMap<String, String> trajectory = new HashMap<String, String>();
		State currentState = new State(this.width, this.length);
		currentState.startNewEpisode();
		
		while (!currentState.terminate()) {
			String state = currentState.getState();
			String action = this.greedyPolicy(qValues.get(state), 0.0);
			trajectory.put(state, action.substring(0, 1));
			currentState.action(action);
		}
		System.out.printf(System.lineSeparator() + ".");
		for (int i = 0; i < this.width; i++) {
			System.out.printf("_");
		}
		System.out.printf("." + System.lineSeparator());
		
		for (int j = (this.length - 1); j > - 1; j--) {
			System.out.printf("|");
			for (int i = 0; i < this.width; i++) {
				String direction = trajectory.get(String.format("(%d, %d)", i, j));
				if (direction != null) {
					System.out.printf("%s", direction);
				} else {
					System.out.printf(" ");
				}
			}
			System.out.printf("|" + System.lineSeparator());
		}
		System.out.printf(".");
		
		for (int i = 0; i < this.width; i++) {
			System.out.printf("_");
		}
		System.out.printf("." + System.lineSeparator());
	}

	/**
	 * The main entry point for the program
	 * 
	 * @param args
	 * 		the arguments
	 */
	public static void main(String[] args) {
		QLearning qLearner = new QLearning(12, 4);
		
		double alpha = 0.1;
		double gamma = 0.9;
		double epsilon = 0.1;
		
		HashMap<String, HashMap<String, Double>> output = qLearner.qLearning(epsilon, alpha, gamma, 1000);		

		qLearner.formatEpisodePolicy(output);
	}

}