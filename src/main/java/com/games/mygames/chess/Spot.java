package com.games.mygames.chess;

public class Spot {
	public int x;
	public int y;
	
	public Spot(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (x+","+y).toString();
	}

}
