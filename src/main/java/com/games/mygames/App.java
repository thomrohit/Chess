package com.games.mygames;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.games.mygames.chess.Box;
import com.games.mygames.chess.ChessGame;
import com.games.mygames.chess.Spot;

/**
 * Hello world!
 *
 */
public class App  implements Platform
{
	static Scanner sc = new Scanner(System.in);
	public static void main( String[] args )
	{
		System.out.println( "Hello World!" );

			playChess();
	}

	private static void playChess() {
		LinkedList<String> players = new LinkedList<String>();
		System.out.println("Do you want to play as white ?");
		String yesNo = sc.next();
		if(yesNo.equalsIgnoreCase("y")) {
			players.add("WHITE");
			players.add("BLACK");
		}
		else{
			players.add("BLACK"); 
			players.add("WHITE");
		}
		ChessGame chess = new ChessGame(new App(),players);
		chess.start();

	}

	public boolean play(String name) {
		if(name.equals("bot")) {
			System.out.println("bot rolled the dice ");
			return true;
		}
		else {
			System.out.println(name+" roll the dice ?");
			String yesNo = sc.next();
			return yesNo.equals("y");
		}

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
