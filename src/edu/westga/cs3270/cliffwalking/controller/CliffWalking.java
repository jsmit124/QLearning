package edu.westga.cs3270.cliffwalking.controller;

//Example 6.6, Sutton & Barto
//https://github.com/cvhu/CliffWalking/blob/master/CliffWalking.java

import java.util.*;

class CliffState {
	private int x;
	private int y;
	private int length;
	private int width;
	private int reward;
	private int returnPolicy;
	
	
	CliffState(int width, int length) {
		this.reset();
		this.width = width;
		this.length = length;
	}

	
	public void reset() {
		this.x = 0;
		this.y = 0;
		this.reward = 0;
		this.returnPolicy = 0;
	}

	
	public void action(String action) {
		if (action.equals("up")) {
			this.up();
		} else if (action.equals("down")) {
			this.down();
		} else if (action.equals("left")) {
			this.left();
		} else if (action.equals("right")) {
			this.right();
		}
		
		this.reward();
	}

	
	public void up() {
		if (this.y < (this.length - 1)) {
			this.y++;
		}		
	}

	
	public void down() {
		if (this.y > 0) {
			this.y--;
		}
	}

	
	public void right() {
		if (this.x < (this.width - 1)) {
			this.x++;
		}		
	}

	
	public void left() {
		if (this.x > 0) {
			this.x--;
		}		
	}

	
	public void reward() {
		if ((this.y == 0) && (this.x > 0) && (this.x < (this.width - 1))) {
			this.reset();
			this.reward = -100;
		} else {
			this.reward = -1;
		}
		
		this.returnPolicy += this.reward;
	}

	
	public int getReward() {
		return this.reward;
	}

	
	public boolean terminate() {
		return (this.x > 0 && (this.y == 0));
	}

	
	public String getState() {
		return String.format("(%d, %d)", this.x, this.y);
	}
}


public class CliffWalking {	
	private int width;
	private int length;

	
	public CliffWalking(int width, int length) {
		this.width = width;
		this.length = length;
	}

	
	public String etaGreedy(HashMap<String, Double> qa, double eta) {
		Random rand = new Random();
		Double maxValue = Double.NEGATIVE_INFINITY;
		int maxCount = 0;
		int totalCount = 0;
		LinkedList<String> actions = new LinkedList<String>();
		LinkedList<Double> probabilities = new LinkedList<Double>();
		
		for (String action : qa.keySet()) {
			totalCount++;
			Double currValue = qa.get(action);
			// System.out.printf("%f vs. %f\n", currValue, maxValue);
			int compare = currValue.compareTo(maxValue);
			if (compare > 0) {
				maxValue = currValue;
				maxCount = 1;
			} else if (compare == 0) {
				// System.out.println("duplicate max");
				maxCount++;
			}
		}
		
		Double exploreProbability = eta / totalCount;
		Double greedyProbability = (1.0 - eta) / maxCount + exploreProbability;
		Double oldProbability = 0.0;
		// System.out.printf("eV: %f\n gV: %f\nmaxCount: %d\n max: %f\n", exploreValue, greedyValue, maxCount, max);
		
		for (String action : qa.keySet()) {
			Double currValue = qa.get(action);
			if (currValue.compareTo(maxValue) == 0) {
				oldProbability += greedyProbability;
			} else {
				oldProbability += exploreProbability;
			}
			
			// System.out.printf("%f\n", oldValue);
			probabilities.add(oldProbability);
			actions.add(action);
		}
		
		double random = rand.nextDouble();
		
		for (int i = 0; i < totalCount; i++) {
			if (random < probabilities.get(i)) {
				return actions.get(i);
			}
		}
		
		return actions.get(totalCount - 1);
	}

	
	public Double getMaxQAV(HashMap<String, Double> qa) {
		Double maxValue = Double.NEGATIVE_INFINITY;
		String maxAction = null;
		
		for (String action : qa.keySet()) {
			Double currValue = qa.get(action);
			int compare = currValue.compareTo(maxValue);
			if (compare > 0) {
				maxValue = currValue;
				maxAction = action;
			}
		}
		
		return maxValue;
	}

	
	public HashMap<String, HashMap<String, Double>> QLearning(double eta, double alpha, double gamma, int n) {		
		HashMap<String, HashMap<String, Double>> qValues = new HashMap<String, HashMap<String, Double>>();
		CliffState cliffState = new CliffState(this.width, this.length);

		// initialize Q arbitrarily
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.length; j++) {
				HashMap<String, Double> qa = new HashMap<String, Double>();
				qa.put("up", 0.5);
				qa.put("down", 0.5);
				qa.put("right", 0.5);
				qa.put("left", 0.5);
				qValues.put(String.format("(%d, %d)", i, j), qa);
			}
		}

		for (int i = 0; i < n; i++) {
			cliffState.reset();
			String state = cliffState.getState();			
			int stateReward = 0;
			
			while (!cliffState.terminate()) {
				String action = this.etaGreedy(qValues.get(state), eta);
				cliffState.action(action);
				int reward = cliffState.getReward();
				String newState = cliffState.getState();				
				HashMap<String, Double> qa = qValues.get(state);
				Double qav = qa.get(action) + alpha * (reward + gamma * (this.getMaxQAV(qValues.get(newState))) - qa.get(action));
				qa.put(action, qav);
				qValues.put(state, qa);
				state = newState;				
				stateReward += reward;
				// System.out.printf("%d: %s-%s %f\n", i, s, a, qav);
			}			
			
			if (i == (n - 1)) {
				System.out.printf("%d\n", stateReward);
			} else {
				System.out.printf("%d, ", stateReward);
			}			
		}
		
		return qValues;
	}

	public HashMap<String, HashMap<String, Double>> sarsa(double eta, double alpha, double gamma, int n) {		
		HashMap<String, HashMap<String, Double>> qValues = new HashMap<String, HashMap<String, Double>>();
		CliffState cliffState = new CliffState(this.width, this.length);

		// initialize Q arbitrarily
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.length; j++) {
				HashMap<String, Double> qa = new HashMap<String, Double>();
				qa.put("up", 0.5);
				qa.put("down", 0.5);
				qa.put("right", 0.5);
				qa.put("left", 0.5);
				qValues.put(String.format("(%d, %d)", i, j), qa);
			}
		}

		for (int i = 0; i < n; i++) {
			cliffState.reset();
			String state = cliffState.getState();
			String action = this.etaGreedy(qValues.get(state), eta);
			int stateReward = 0;
			
			while (!cliffState.terminate()) {
				cliffState.action(action);
				int reward = cliffState.getReward();
				String newState = cliffState.getState();
				String newAction = this.etaGreedy(qValues.get(newState), eta);
				HashMap<String, Double> qa = qValues.get(state);
				Double qav = qa.get(action) + alpha * (reward + gamma * (qValues.get(newState).get(newAction)) - qa.get(action));
				qa.put(action, qav);
				qValues.put(state, qa);
				state = newState;
				action = newAction;
				stateReward += reward;
				// System.out.printf("%d: %s-%s %f\n", i, s, a, qav);
			}			
			
			if (i == (n - 1)) {
				System.out.printf("%d\n", stateReward);
			} else {
				System.out.printf("%d, ", stateReward);
			}
		}
		
		return qValues;
	}

	
	public void printPolicy(HashMap<String, HashMap<String, Double>> q){
		HashMap<String, String> trajectory = new HashMap<String, String>();
		CliffState cliffState = new CliffState(this.width, this.length);
		cliffState.reset();
		
		while (!cliffState.terminate()) {
			String state = cliffState.getState();
			String action = this.etaGreedy(q.get(state), 0.0);
			trajectory.put(state, action.substring(0, 1));
			cliffState.action(action);
			// System.out.printf("%s-%s", s, a);
		}
		
		System.out.printf("\n.");
		
		for (int i = 0; i < this.width; i++) {
			System.out.printf("_");
		}
		
		System.out.printf(".\n");
		
		for (int j = (this.length - 1); j > - 1; j--) {
			System.out.printf("|");
			for (int i = 0; i < this.width; i++) {
				String a = trajectory.get(String.format("(%d, %d)", i, j));
				if (a != null) {
					System.out.printf("%s", a);
				} else {
					System.out.printf(" ");
				}
			}
		
			System.out.printf("|\n");
		}
		
		System.out.printf(".");
		
		for (int i = 0; i < this.width; i++) {
			System.out.printf("_");
		}
		
		System.out.printf(".\n");
		// System.out.printf("\n");
	}

	
	public static void main(String[] args) {		
		// CliffWalking cw = new CliffWalking(12, 4);
		// cw.Sarsa(0.1, 0.01, 1.0, 1000);
		// cw.Sarsa(0.1, 0.05, 1.0, 1000);
		// cw.Sarsa(0.1, 0.1, 1.0, 1000);
		// cw.Sarsa(0.1, 0.25, 1.0, 1000);
		// cw.Sarsa(0.1, 0.5, 1.0, 1000);

		// cw.Sarsa(0.01, 0.5, 1.0, 1000);
		// cw.Sarsa(0.1, 0.5, 1.0, 1000);
		// cw.Sarsa(0.25, 0.5, 1.0, 1000);
		// cw.Sarsa(0.5, 0.5, 1.0, 1000);

		// cw.Sarsa(0.01, 0.5, 0.01, 1000);
		// cw.Sarsa(0.01, 0.5, 0.1, 1000);
		// cw.Sarsa(0.01, 0.5, 0.5, 1000);
		// cw.Sarsa(0.01, 0.5, 1.0, 1000);

		// cw.QLearning(0.1, 0.01, 1.0, 1000);
		// cw.QLearning(0.1, 0.05, 1.0, 1000);
		// cw.QLearning(0.1, 0.1, 1.0, 1000);
		// cw.QLearning(0.1, 0.25, 1.0, 1000);
		// cw.QLearning(0.1, 0.5, 1.0, 1000);

		// cw.QLearning(0.01, 0.5, 1.0, 1000);
		// cw.QLearning(0.1, 0.5, 1.0, 1000);
		// cw.QLearning(0.25, 0.5, 1.0, 1000);
		// cw.QLearning(0.5, 0.5, 1.0, 1000);

		// cw.QLearning(0.01, 0.5, 0.01, 1000);
		// cw.QLearning(0.01, 0.5, 0.1, 1000);
		// cw.QLearning(0.01, 0.5, 0.5, 1000);
		// cw.QLearning(0.01, 0.5, 1.0, 1000);

		// CliffWalking cw = new CliffWalking(12, 4);
		// CliffWalking cw = new CliffWalking(4, 6);		
		CliffWalking cliffWalker = new CliffWalking(3, 6);
		HashMap<String, HashMap<String, Double>> qS = cliffWalker.sarsa(0.01, 0.5, 0.5, 1000);
		HashMap<String, HashMap<String, Double>> qQL = cliffWalker.QLearning(0.01, 0.5, 1.0, 1000);		

		cliffWalker.printPolicy(qS);
		cliffWalker.printPolicy(qQL);
	}

}