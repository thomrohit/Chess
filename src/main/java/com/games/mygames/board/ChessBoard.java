package com.games.mygames.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.games.mygames.chess.Box;
import com.games.mygames.chess.Spot;
import com.games.mygames.chess.piece.Bishop;
import com.games.mygames.chess.piece.Empty;
import com.games.mygames.chess.piece.King;
import com.games.mygames.chess.piece.Knight;
import com.games.mygames.chess.piece.Pawn;
import com.games.mygames.chess.piece.Queen;
import com.games.mygames.chess.piece.Rook;

/**
 * Business logic and initialization for Chess is present here
 * 1.Handles movement of all pieces
 * 2.Check condition
 * 3.CheckMate condition
 * @author Rohit Thomas
 *
 */
public class ChessBoard {
	public ChessBoard(){
		initalize();
		//displayBoard();
	}

	public Box layout [][] = new Box [8][8];

	public Box[][] displayBoard() {
		/*// Uncomment to view Board Layout in console
		  for(int i = 0; i < 8; i++) {
    		for(int j = 0; j < 8; j++) {
    			System.out.print(layout[i][j].p.pieceName.substring(0, 1));
    		}
    		System.out.println();
    		}*/
		return layout;
	}



	void initalize() {
		String color = "";
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				layout[i][j] = new Box(i,j,new Empty(""));

				if(i == 0 || i == 7) {
					if (i == 0)
						color = "BLACK";
					else
						color = "WHITE";
					if(j == 0 || j == 7)
						layout[i][j] = new Box(i,j,new Rook(color));
					if(j == 1 || j == 6)
						layout[i][j] = new Box(i,j,new Knight(color));
					if(j == 2 || j == 5)
						layout[i][j] = new Box(i,j,new Bishop(color));
					if(j == 3)
						layout[i][j] = new Box(i,j,new Queen(color));
					if(j == 4)
						layout[i][j] = new Box(i,j,new King(color));
				}

				if(i == 1 || i==6) {
					if (i == 1)
						color = "BLACK";
					else
						color = "WHITE";
					layout[i][j] = new Box(i,j,new Pawn(color));
				}
			}
		}
	}

	public List<Spot>allPossibilities(Box b,int srcX,int srcY) {
		List<Spot> spots = new ArrayList<Spot>();
		String piece = b.p.pieceName;
		if(piece.equals("Pawn")) {
			spots = getPawnPossibility(srcX, srcY);

		}
		else if(piece.equals("Rook")) {
			spots = getRookPossibility(srcX, srcY);

		}
		else if(piece.equals("Knight")) {
			spots = getKnightPossibility(srcX, srcY);

		}
		else if(piece.equals("Bishop")) {
			spots = getBishopPossibility(srcX, srcY);

		}
		else if(piece.equals("Queen")) {
			spots = getQueenPossibility(srcX, srcY);

		}
		else if(piece.equals("King")) {
			spots = getKingPossibility(srcX, srcY);

		}

		return spots;

	}



	private List<Spot> getKingPossibility(int srcX, int srcY) {
		// 8 possible moves -- ALL directions
		String currColor = layout[srcX][srcY].p.color;
		List<Spot> possibleMoves = Arrays.asList(new Spot(1,1),new Spot(1,-1),new Spot(-1,1),new Spot(-1,-1),//Diagonal checks
				new Spot(1,0),new Spot(-1,0),new Spot(0,-1),new Spot(0,1));//up,down,left,right

		List<Spot> spots = getValidSpots4KingKinght(srcX, srcY, possibleMoves, currColor);
		
		return preventCheckSpots(layout[srcX][srcY],spots);
	}


	private List<Spot> preventCheckSpots(Box originalBox, List<Spot> spots) {
		List<Spot> notValidSpots = new ArrayList<Spot>();
		Spot orignialSpot = new Spot(originalBox.x, originalBox.y);
		String currColor = originalBox.p.color;
		for(Spot move:spots) {
			if(isEmpty(move))
				move(originalBox,move);
			else 
				continue;
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					Box box = layout[i][j];
					if(!currColor.equals(box.p.color) && !box.p.pieceName.equals("Empty") && !box.p.pieceName.equals("King")) {
						List<Spot> possibilities = allPossibilities(box,i,j);
						for(Spot spot:possibilities) {
							if(isKill(spot,box.p.color) && isCheck(spot, box.p.color)) 
								notValidSpots.add(move);
						}
					}
				}
			}
			move(originalBox,orignialSpot);
		}
		if(!notValidSpots.isEmpty()) {
			spots.removeAll(notValidSpots);
		}
		return spots;
	}

	private void move(Box b, Spot newSpot) {
		Box temp = new Box(b.x, b.y, new Empty(""));
		b.x = newSpot.x;
		b.y = newSpot.y;
		layout[b.x][b.y] = b;
		layout[temp.x][temp.y] = temp;
	}


	private List<Spot> getQueenPossibility(int srcX, int srcY) {
		// ALL directions
		String currColor = layout[srcX][srcY].p.color;
		List<Spot> possibleMoves = Arrays.asList(new Spot(1,1),new Spot(1,-1),new Spot(-1,1),new Spot(-1,-1),//Diagonal checks
				new Spot(1,0),new Spot(-1,0),new Spot(0,-1),new Spot(0,1));//up,down,left,right

		return getValidSpots4QueenRookBishop(srcX, srcY, possibleMoves, currColor);
	}

	private List<Spot> getBishopPossibility(int srcX, int srcY) {
		// ALL diagonal directions
		String currColor = layout[srcX][srcY].p.color;
		List<Spot> possibleMoves = Arrays.asList(new Spot(1,1),new Spot(1,-1),new Spot(-1,1),new Spot(-1,-1));//Diagonal checks

		return getValidSpots4QueenRookBishop(srcX, srcY, possibleMoves, currColor);
	}



	private List<Spot> getKnightPossibility(int srcX, int srcY) {
		String currColor = layout[srcX][srcY].p.color;
		// 8 possible moves -- all direction/mirror of L 
		List<Spot> possibleMoves = Arrays.asList(new Spot(2,1),new Spot(2,-1),new Spot(-2,1),new Spot(-2,-1),
				new Spot(1,2),new Spot(1,-2),new Spot(-1,2),new Spot(-1,-2));

		return getValidSpots4KingKinght(srcX, srcY, possibleMoves, currColor);
	}



	private List<Spot> getRookPossibility(int srcX, int srcY) {
		String currColor = layout[srcX][srcY].p.color;
		// 4 directions
		List<Spot> possibleMoves = Arrays.asList(new Spot(1,0),new Spot(-1,0),new Spot(0,-1),new Spot(0,1));//up,down,left,right

		return getValidSpots4QueenRookBishop(srcX, srcY, possibleMoves, currColor);
	}



	private List<Spot> getPawnPossibility(int srcX, int srcY) {
		// 8 possible moves -- ALL directions
		List<Spot> spots = new ArrayList<Spot>();
		String currColor = layout[srcX][srcY].p.color;
		List<Spot> possibleMoves =  new ArrayList<Spot>();
		if(layout[srcX][srcY].p.color.equals("WHITE")) {
			possibleMoves = new ArrayList<Spot>(Arrays.asList(new Spot(-1,1),new Spot(-1,-1),//Diagonal checks
					new Spot(-1,0)));//up--reverse for White
			if(srcX == 6)
				possibleMoves.add(new Spot(-2,0)); // Added ArrayList for initialization, as Arrays.asList returns a fixed array which cannot add/remove an object
		}
		else {
			possibleMoves = new ArrayList<Spot>(Arrays.asList(new Spot(1,1),new Spot(1,-1),//Diagonal checks
					new Spot(1,0)));//up-- forward for Black
			if(srcX == 1)
				possibleMoves.add(new Spot(2,0));
		}

		int killSpots = 2; // we should not add the spots for Kill moves if it's empty,Ensure that we add kill spots first in possibleMoves list
		for(Spot move:possibleMoves) {
			int newPosX = srcX + move.x;
			int newPosY = srcY + move.y;
			if(validPos(newPosX) && validPos(newPosY)){
				Spot spot = new Spot(newPosX,newPosY);
				if((isEmpty(spot) && killSpots <= 0) || (isKill(spot,currColor) && killSpots >0)) //To avoid adding kill spots if it is empty
					spots.add(spot);
				else if (!isEmpty(spot) && killSpots <= 0 )
					break;
			}
			killSpots--;
		}

		return spots;

	}

	private List<Spot> getValidSpots4KingKinght(int srcX, int srcY, List<Spot> possibleMoves, String currColor) {
		List<Spot> spots = new ArrayList<Spot>();
		
		for(Spot move:possibleMoves) {
			int newPosX = srcX + move.x;
			int newPosY = srcY + move.y;
			if(validPos(newPosX) && validPos(newPosY)){
				Spot spot = new Spot(newPosX,newPosY);
				if(isEmpty(spot) || isKill(spot,currColor))
					spots.add(spot);
			}
		}

		return spots;
	}

	private List<Spot> getValidSpots4QueenRookBishop(int srcX, int srcY, List<Spot> possibleMoves, String currColor) {
		List<Spot> spots = new ArrayList<Spot>();
		
		for(Spot move:possibleMoves) {
			int count = 1;
			boolean flg = Boolean.TRUE;
			int newPosX = srcX;
			int newPosY = srcY;
			while(count != 8 && flg) {
				newPosX = newPosX + move.x;
				newPosY = newPosY + move.y;
				if(validPos(newPosX) && validPos(newPosY)){
					Spot spot = new Spot(newPosX,newPosY);
					if(isEmpty(spot))
						spots.add(spot);
					else {
						if(isKill(spot,currColor))
							spots.add(spot);
						flg = Boolean.FALSE;
					}
				}
				count++;
			}

		}

		return spots;
	}
	
	private boolean validPos(int pos) {
		return pos < 8 && pos > -1;
	}

	public boolean isKill(Spot spot, String currColor) {
		return !layout[spot.x][spot.y].p.pieceName.equals("Empty") && !layout[spot.x][spot.y].p.color.equals(currColor);
	}



	public boolean isEmpty(Spot spot) {
		return layout[spot.x][spot.y].p.pieceName.equals("Empty");
	}


	public boolean isVaild(List<Spot> possibilities, int destX, int destY) {
		boolean flg = Boolean.FALSE;
		for(Spot spot:possibilities)
			if(spot.x == destX && spot.y == destY){
				flg = Boolean.TRUE;
				break;
			}
		return flg;
	}

	public boolean isCheck(Spot spot, String currColor) {
		return layout[spot.x][spot.y].p.pieceName.equals("King") && !layout[spot.x][spot.y].p.color.equals(currColor);
	}


	public boolean isCheck( String currColor) {

		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Box box = layout[i][j];
				if(currColor.equals(box.p.color) && !box.p.pieceName.equals("Empty")) {
					List<Spot> possibilities = allPossibilities(box,i,j);
					for(Spot spot:possibilities) {
						if(isKill(spot,currColor) && isCheck(spot, currColor)) // if the spot is king then check
								return Boolean.TRUE;
						/*else if (isKill(spot,currColor)) // if the spot is players piece-->blocking the king's path for check, continue with other pieces 
								break;*/
					}
						
				}
			}
		}
		return Boolean.FALSE;
	}

	public HashMap<Box,List<Spot>> checkMate(String currColor) {
		String oppColor = currColor.equals("WHITE") ? "BLACK" : "WHITE";
		HashMap<Box,List<Spot>> map = new HashMap<Box,List<Spot>>();
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Box box = layout[i][j];
				if(currColor.equals(box.p.color) && !box.p.pieceName.equals("Empty")) {
					List<Spot> possibilities = allPossibilities(box,i,j);
					List<Spot> saveKingPossibilities = new ArrayList<Spot>();
					if(!possibilities.isEmpty()) {
						for(Spot spot:possibilities){
							Box temp = layout[spot.x][spot.y];
							layout[spot.x][spot.y] = box;
							layout[i][j] = new Box(i,j,new Empty(""));
							if(!isCheck(oppColor))
								saveKingPossibilities.add(spot);
							layout[spot.x][spot.y] = temp;
							layout[i][j] = box;
						}
						if(!saveKingPossibilities.isEmpty())
							map.put(box, saveKingPossibilities);
					}
				}
			}
		}
		return map;
	}

}
