package com.games.mygames.chess;

import com.games.mygames.chess.piece.PieceAbstract;

public class Box {
	public int x;
	public int y;
	public PieceAbstract p;
	public Box(int x,int y, PieceAbstract p) {
		this.x = x;
		this.y = y;
		this.p = p;
	}

}
