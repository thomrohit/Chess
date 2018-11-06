package com.games.mygames;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.games.mygames.chess.Box;
import com.games.mygames.chess.Spot;

public class ChessApp implements Platform{

	static Scanner sc = new Scanner(System.in);
	
	@Override
	public boolean play(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void status(String status) {
		// TODO Auto-generated method stub
	}

	@Override
	public String move(int srcX, int srcY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getPossibilities(List<Spot> possibilities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveKingPossibilities(HashMap<Box, List<Spot>> map) {
		// TODO Auto-generated method stub
		
	}

}
