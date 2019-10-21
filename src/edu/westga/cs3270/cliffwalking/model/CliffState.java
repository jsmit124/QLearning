package edu.westga.cs3270.cliffwalking.model;

public class CliffState {
	private int x;
	private int y;
	private int length;
	private int width;
	private int reward;
	private int returnPolicy;
	
	
	public CliffState(int width, int length) {
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