/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chess.pieces;

import com.mycompany.board.Board;
import com.mycompany.board.Location;
import com.mycompany.chess.ChessMatch;
import com.mycompany.chess.ChessPiece;
import com.mycompany.chess.Color;

public class King extends ChessPiece{
	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove(Location location) {
		ChessPiece p = (ChessPiece)board.piece(location);
		return p == null || p.getColor() != getColor() ;
	}
	
	private boolean testRookCastling(Location location) {
		ChessPiece p = (ChessPiece)getBoard().piece(location);
		return p != null && p instanceof Rook && p.getColor() == getColor() & p.getMoveCount() == 0;
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];
		
		Location p = new Location(0, 0);
		
		// left
		p.setValues(location.getRow(), location.getColumn() - 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// left+up
		p.setValues(location.getRow() - 1, location.getColumn() - 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// up
		p.setValues(location.getRow() - 1, location.getColumn());
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// up+right
		p.setValues(location.getRow() - 1, location.getColumn() + 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// right
		p.setValues(location.getRow(), location.getColumn() + 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// right+down
		p.setValues(location.getRow() + 1, location.getColumn() + 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// down
		p.setValues(location.getRow() + 1, location.getColumn());
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// down+left
		p.setValues(location.getRow() + 1, location.getColumn() - 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// #specialmove castling
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			// #specialmove castling kingside rook
			Location posT1 = new Location(location.getRow(), location.getColumn() + 3);
			if (testRookCastling(posT1)) {
				Location p1 = new Location(location.getRow(), location.getColumn() + 1);
				Location p2 = new Location(location.getRow(), location.getColumn() + 2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					matrix[location.getRow()][location.getColumn() + 2] = true;
				}
			}
			// #specialmove castling queenside rook
			Location posT2 = new Location(location.getRow(), location.getColumn() - 4);
			if (testRookCastling(posT2)) {
				Location p1 = new Location(location.getRow(), location.getColumn() - 1);
				Location p2 = new Location(location.getRow(), location.getColumn() - 2);
				Location p3 = new Location(location.getRow(), location.getColumn() - 3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					matrix[location.getRow()][location.getColumn() - 2] = true;
				}
			}
		}
		
		return matrix;
	}
}