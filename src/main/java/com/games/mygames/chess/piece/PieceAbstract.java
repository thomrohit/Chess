package com.games.mygames.chess.piece;

public abstract class PieceAbstract {

	public String color;
	public String pieceName;
	public PieceAbstract(String color) {
		this.color = color;
		this.pieceName = this.getClass().getSimpleName();
	}
}
