package com.ipol.lbarsample.items;

public class HighScoreItem {
	private int mSequence;
	private String mName;
	private int mTotalGoal;
	private int mTotalInOneMatch;
	private float mPunk;
	public int getSequence() {
		return mSequence;
	}
	public void setSequence(int sequence) {
		mSequence = sequence;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public int getTotalGoal() {
		return mTotalGoal;
	}
	public void setTotalGoal(int totalGoal) {
		mTotalGoal = totalGoal;
	}
	public int getTotalInOneMatch() {
		return mTotalInOneMatch;
	}
	public void setTotalInOneMatch(int totalInOneMatch) {
		mTotalInOneMatch = totalInOneMatch;
	}
	public float getPunk() {
		return mPunk;
	}
	public void setPunk(float punk) {
		mPunk = punk;
	}

}
