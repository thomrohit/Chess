package com.games.mygames;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.games.mygames.chess.Box;
import com.games.mygames.chess.Spot;
import com.games.mygames.player.Player;

public interface Platform {

	 public boolean play(String name);
	 
	//Chess
	 public String move(int srcX, int srcY);
	 public void status(String status);
	 public void getPossibilities(List<Spot> possibilities);
	 public void saveKingPossibilities(HashMap<Box, List<Spot>> map);
}
