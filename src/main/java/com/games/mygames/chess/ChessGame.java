package com.games.mygames.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.games.mygames.Platform;
import com.games.mygames.board.ChessBoard;
import com.games.mygames.chess.piece.Empty;
import com.games.mygames.player.Player;

public class ChessGame {
	ChessBoard chess = new ChessBoard();
	Platform platform;
	LinkedList<Player> players;
	Map <String,List<Box>> fallenPieces;
	public ChessGame(Platform platform, LinkedList<String> players) {
		this.platform = platform;
		this.players = new LinkedList<Player>();
		this.fallenPieces = new HashMap<String,List<Box>>();

		for(String player:players) {
			this.players.add(new Player(player.toString()));
		}
	}
	public static void main(String[] args) {
		LinkedList<String> players = new LinkedList<String>();
		players.add("WHITE");
		players.add("BLACK");
		ChessGame g = new ChessGame(null,players);
		g.start();
	}

	public Box[][] board() {
		return chess.displayBoard();
	}
	public void start() {
		boolean checkmate = Boolean.FALSE;
		boolean isCheck = Boolean.FALSE; //Handle check condition
		HashMap<Box,List<Spot>> saveKingMap = new HashMap<Box,List<Spot>>();
		List<Spot> saveKingPiecemoves = new ArrayList<Spot>();
		while(!checkmate) {
			for(Player player:players) {
				//platform.play(player.getName());
				boolean isMoveDone = Boolean.FALSE;
				while(!isMoveDone) {
					if(isCheck){
						status("-1");
						saveKingMap = chess.checkMate(player.getName());
						if(saveKingMap.isEmpty()){
							checkmate = Boolean.TRUE;
							break;
						}
						else {
							saveKingPossibility(saveKingMap);
						}
					}
					play(player);
					String pos = getPos(-1,-1);
					int arr[] = Arrays.asList(pos.split(",")).stream().mapToInt(Integer::parseInt).toArray();
					int srcX = arr[0];
					int srcY = arr[1];

					Box b = chess.layout[srcX][srcY];
					
					if(!saveKingMap.isEmpty()) {
						if(!saveKingMap.containsKey(b)){
							status("Cannot move piece");
						continue;
						}
						else {
							saveKingPiecemoves = saveKingMap.get(b);
						}
					}

					if(player.getName().equals(b.p.color)){
						List<Spot> possibilities;
						if(!saveKingPiecemoves.isEmpty()) {
							possibilities = saveKingPiecemoves;
						}else {
						 possibilities = chess.allPossibilities(b, srcX, srcY);
						}
						if(possibilities.isEmpty()) {
							status("Cannot move piece");
							continue;
						}
						status("All Possible scenarios");
						allPossibilities(possibilities);
						status("Move this piece ");//Move this piece ? add yes/No
						pos = getPos(srcX,srcY);
						arr = Arrays.asList(pos.split(",")).stream().mapToInt(Integer::parseInt).toArray();
						int destX = arr[0];
						int destY = arr[1];
						if(chess.isVaild(possibilities,destX,destY)) {
							Spot newSpot = new Spot(destX,destY);
							if(chess.isEmpty(newSpot) || chess.isKill(newSpot, b.p.color)) {
								move(b,newSpot);
								isCheck = chess.isCheck(player.getName());
								isMoveDone = Boolean.TRUE;
								if(!saveKingPiecemoves.isEmpty()) {
									saveKingMap.clear();
									saveKingPiecemoves.clear();
								}
							}
						}
						else
							status("Not a valid move !!!");
					}
					else
						status("Cannot select opponents/Empty piece");

				}
				chess.displayBoard();
			}
		}
		status("0"); //CheckMate
	}
	
	private void saveKingPossibility(HashMap<Box, List<Spot>> map) {
		platform.saveKingPossibilities(map);
		/*System.out.println("All possibilities to save King");
		for(Entry<Box,List<Spot>> entry:map.entrySet()) {
			Box b = entry.getKey();
			System.out.print(b.p.pieceName + " (" + b.x +","+ b.y + ")" + ": ");
			for(Spot spot:entry.getValue()) {
				System.out.print(spot+" ");
			}
			System.out.println();
		}*/
		
	}
	
	private void move(Box b, Spot newSpot) {
		Box fallenPiece = chess.layout[newSpot.x][newSpot.y];
		addPiece(this.fallenPieces,fallenPiece.p.color,fallenPiece);
		Box temp = new Box(b.x, b.y, new Empty(""));
		b.x = newSpot.x;
		b.y = newSpot.y;
		chess.layout[b.x][b.y] = b;
		chess.layout[temp.x][temp.y] = temp;

	}
	private void addPiece(Map<String, List<Box>> map, String color, Box fallenPiece) {

		if(!fallenPiece.p.pieceName.contentEquals("Empty")) {
			List<Box> pieces = map.get(color);
			if(pieces == null)
				pieces = new ArrayList<Box>();
			pieces.add(fallenPiece);
			map.put(color, pieces);
		}
	}
	
	//Interactive methods
	private void status(String message) {
		platform.status(message);
	}

	public boolean play(Player player) {
		return platform.play(player.getName());
	}
	
	public String getPos(int srcX, int srcY) {
		 return  platform.move(srcX,srcY);
	}
	
	private void allPossibilities(List<Spot> possibilities) {
		platform.getPossibilities(possibilities);
		
	}
}
