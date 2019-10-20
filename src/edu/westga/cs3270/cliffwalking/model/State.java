package edu.westga.cs3270.cliffwalking.model;

public interface State {
	
	void setProperty(String key, Object value);
	Object getProperty(String key);
	double getReward();
	void setReward(double r);
	double[] getPolicyValue();
	void setAllPolicyValues(double d);
	void setPolicyValueSize(int size);
	boolean wasVisited();
	void setVisited(int i);
	int getVisited();
	void increaseVisited();

}
