package edu.westga.cs3270.cliffwalking.model;

import java.util.HashMap;

import edu.westga.cs3270.cliffwalking.io.Parser;

/**
 * Stores information for the current state in Cliff Walking
 * 
 * @author jsmit124
 * @version so many
 *
 */
public class State {
	private int x;
	private int y;
	private int length;
	private int width;
	private int reward;
	private HashMap<String, Integer> rewardList;
	
	/**
	 * Constructs a cliff state object
	 * 
	 * @param width
	 * 		the width of the world
	 * @param length
	 * 		the length of the world
	 */
	public State(int width, int length, String fileName) {
		this.startNewEpisode();
		this.width = width;
		this.length = length;
		this.reward = -1;
		Parser parser = new Parser(fileName);
		this.rewardList = parser.parseWorldRewards();
	}

	/**
	 * Starts a new episode
	 * @precondition none
	 * @postcondition this.x == 0 && this.y == 0
	 */
	public void startNewEpisode() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Performs actions to be taken by the 'player'
	 * 
	 * @param action
	 * 			the action to be taken by the 'player'
	 */
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

	/**
	 * Move to the state above the current state
	 * 
	 * @precondition none
	 * @postcondition none
	 */
	public void up() {
		if (this.y < (this.length - 1)) {
			this.y++;
		}		
	}

	/**
	 * Move to the state below the current state
	 * 
	 * @precondition none
	 * @postcondition none
	 */
	public void down() {
		if (this.y > 0) {
			this.y--;
		}
	}

	/**
	 * Move to the state to the right of the current state
	 * 
	 * @precondition none
	 * @postcondition none
	 */
	public void right() { 
		if (this.x < (this.width - 1)) {
			this.x++;
		}		
	}

	/**
	 * Move to the state to the left of the current state
	 * 
	 * @precondition none
	 * @postcondition none
	 */
	public void left() {
		if (this.x > 0) {
			this.x--;
		}		
	}

	/**
	 * Defines the rewards of each state
	 * 
	 * Gets current reward for the state from the list of rewards and sets this.reward to
	 * the current state's reward
	 */
	public void reward() {
		String state = this.getState();
		this.reward = this.rewardList.get(state);
	}

	/**
	 * Returns the current reward of the current state
	 * 
	 * @return
	 * 		the reward of the current state
	 */
	public int getReward() {
		return this.reward;
	}

	/**
	 * Terminates the episode when player has reached a goal state
	 * 
	 * @return
	 * 		True if reward == 0 (goal found) || reward == -100 (death), false otherwise
	 */
	public boolean terminate(int reward) {
		return reward == 0 || reward == -100;
	}

	/**
	 * Returns the current state of the player
	 * 
	 * @return
	 * 		the current state of the player
	 */
	public String getState() {
		return String.format("(%d, %d)", this.x, this.y);
	}
}