package com.games.mygames.player;


public class Player {

	public Player(String name){
		this.name = name;
		
	}
	private String name;
	private boolean isSkipped;
	
	
	public boolean isSkipped() {
		return isSkipped;
	}

	public void setSkipped(boolean isSkipped) {
		this.isSkipped = isSkipped;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
