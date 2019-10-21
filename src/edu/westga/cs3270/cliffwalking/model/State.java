package edu.westga.cs3270.cliffwalking.model;

/**
 * Stores information for the current state in Cliff Walking
 * 
 * @author jsmit124
 * @version a lot
 *
 */
public class State {
	private int x;
	private int y;
	private int length;
	private int width;
	private int reward;
	
	/**
	 * Constructs a cliff state object
	 * 
	 * @param width
	 * 		the width of the world
	 * @param length
	 * 		the length of the world
	 */
	public State(int width, int length) {
		this.startNewEpisode();
		this.width = width;
		this.length = length;
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
	 * If y == 0 and x > 0, but not greater than this.width - 1, then you have reached the cliff
	 * 	and will have reward of -100
	 * 
	 * else, reward is -1
	 */
	public void reward() {
		if ((this.y == 0) && (this.x > 0) && (this.x < (this.width - 1))) {
			this.startNewEpisode();
			this.reward = -100;
		} else {
			this.reward = -1;
		}
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
	 * 		True if x > 0 && y == 0, false otherwise
	 */
	public boolean terminate() {
		return (this.x > 0 && (this.y == 0));
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