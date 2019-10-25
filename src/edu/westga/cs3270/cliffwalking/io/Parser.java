package edu.westga.cs3270.cliffwalking.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Stores information for the file parser class
 * 
 * @author jsmit124
 * @version 2.0
 *
 */
public class Parser {
	private File file;
	private Scanner scan;
	
	/**
	 * Constructs a new parser object and sets this.file and this.scan to
	 * appropriate values
	 * 
	 * @param fileName
	 * 			file name to parse
	 */
	public Parser(String fileName) {
		if (fileName.equals("CliffWalking")) {
			this.file = new File("src/CliffWalkingWorld.txt");
		} else if (fileName.equals("Homework5")) {
			this.file = new File("src/Homework5World.txt");
		}
		
		try {
			this.scan = new Scanner(this.file);
		} catch(IOException err) {
			System.err.println("IO Error");
		}
	}
	
	/**
	 * Parses the world's rewards from the text file into the appropriate states
	 * and returns the hashmap of (state, reward)
	 * 
	 * @return
	 * 		the values of (state, reward)
	 */
	public HashMap<String, Integer> parseWorldRewards() {
		HashMap<String, Integer> output = new HashMap<String, Integer>();
		int heightCount = 3;
		
		while (this.scan.hasNext()) {
			String line = this.scan.nextLine();
			String[] rewardValues = line.split(" ");
			int widthCount = 0;
			
			for (String value : rewardValues) {
				int currReward = Integer.parseInt(value);
				String currState = String.format("(%d, %d)", widthCount, heightCount);
				
				output.put(currState, currReward);
				
				widthCount++;
			}
			heightCount--;
		}
		
		return output;
	}

}
