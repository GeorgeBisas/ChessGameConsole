/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.board;

import com.mycompany.chess.Color;

public abstract class Piece {
	
	protected Location location;
	protected Board board;

	public Piece(Board board) {
		this.board = board;
		this.location = null;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	protected Board getBoard() {
		return board;
	}
	
	public abstract boolean[][] possibleMoves();
	
	public boolean possibleMove(Location location) {
		return possibleMoves()[location.getRow()][location.getColumn()];
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] matrix = possibleMoves();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j]) {
					return true;
				}
			}
		}
		return false;
	}

}
