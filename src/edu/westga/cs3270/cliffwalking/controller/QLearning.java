package edu.westga.cs3270.cliffwalking.controller;

//Sources cited: https://github.com/cvhu/CliffWalking
	
import java.util.*;
import edu.westga.cs3270.cliffwalking.model.State;

/**
 * Stores controlling information for the Cliff Walking Q-Learning exercise
 * 
 * @author jsmit124
 * @version so many
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

	//handles the greedy/exploration policy
	private String greedyPolicy(HashMap<String, Double> movesAndQValues, double epsilon) {
		Random rand = new Random();
		double explorationValue = rand.nextDouble();
		String move = "";
		double bestQValue = this.getMaxQValue(movesAndQValues);
		
		//finds best move and returns it if exploration not chosen
		for (String currMove : movesAndQValues.keySet()) {
			if (movesAndQValues.get(currMove) == bestQValue) {
				move = currMove;
			}
		}
		
		//if exploration is chosen, make copy of existing moves/qvalues,
		//then choose random number and return the random move
		if (explorationValue < epsilon) {
			HashMap<String, Double> newQA = new HashMap<String, Double>(movesAndQValues);
			newQA.remove(move);
			int randomChoice = rand.nextInt(3);
			Object[] moves = newQA.keySet().toArray();
			move = (String) moves[randomChoice];
		}
		
		return move;
	}

	/**
	 * Compares two q values and returns the maximum of the two
	 * 
	 * @param movesAndQValues
	 * 		the current move direction, q value pair
	 * 
	 * @return
	 * 		the maximum q value
	 */
	public Double getMaxQValue(HashMap<String, Double> movesAndQValues) {
		Double maxValue = Double.NEGATIVE_INFINITY;
		
		for (String action : movesAndQValues.keySet()) {
			Double currValue = movesAndQValues.get(action);
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
	public HashMap<String, HashMap<String, Double>> qLearning(double epsilon, double alpha, double gamma, int amountOfIterations, String fileName) {		
		HashMap<String, HashMap<String, Double>> qValues = new HashMap<String, HashMap<String, Double>>();
		State cliffState = new State(this.width, this.length, fileName);

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.length; j++) {
				HashMap<String, Double> moves = new HashMap<String, Double>();
				moves.put("up", 0.0);
				moves.put("down", 0.0);
				moves.put("right", 0.0);
				moves.put("left", 0.0);
				qValues.put(String.format("(%d, %d)", i, j), moves);
			}
		}

		for (int i = 0; i < amountOfIterations; i++) {
			cliffState.startNewEpisode();
			String state = cliffState.getState();
			cliffState.reward();
			
			while (!cliffState.terminate(cliffState.getReward())) {
				String action = this.greedyPolicy(qValues.get(state), epsilon);
				cliffState.action(action);

				int reward = cliffState.getReward();
				String newState = cliffState.getState();				
				HashMap<String, Double> qa = qValues.get(state);
				
				Double currQValue = qa.get(action) + alpha * (reward + gamma * (this.getMaxQValue(qValues.get(newState))) - qa.get(action));
				qa.put(action, currQValue);
				qValues.put(state, qa);
				state = newState;
				//System.out.printf("%d: %s-%s %f" + System.lineSeparator(), i, state, action, currQValue);
			}			
		}
		return qValues;
	}

	/**
	 * Prints the policy in format of width x length
	 * 
	 * @param qValues
	 * 		map containing the qValues in form of key=state, value=direction,qvalue
	 */
	public void formatEpisodePolicy(HashMap<String, HashMap<String, Double>> qValues, String fileName) {
		HashMap<String, String> trajectory = new HashMap<String, String>();
		State currentState = new State(this.width, this.length, fileName);
		
		while (!currentState.terminate(currentState.getReward())) {
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
		
		double alpha = 0.5;
		double gamma = 0.9;
		double epsilon = 0.1;
		
		HashMap<String, HashMap<String, Double>> output1 = qLearner.qLearning(epsilon, alpha, gamma, 1000, "CliffWalking");		
		qLearner.formatEpisodePolicy(output1, "CliffWalking");
	
		HashMap<String, HashMap<String, Double>> output2 = qLearner.qLearning(epsilon, alpha, gamma, 1000, "Homework5");
		qLearner.formatEpisodePolicy(output2, "Homework5");
	}

}