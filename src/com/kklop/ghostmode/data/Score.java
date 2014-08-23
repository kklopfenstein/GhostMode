package com.kklop.ghostmode.data;

public class Score {
	private int id;
	private int score;
	private String name;
	
	public Score(int id, int score, String name) {
		super();
		this.id = id;
		this.score = score;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
