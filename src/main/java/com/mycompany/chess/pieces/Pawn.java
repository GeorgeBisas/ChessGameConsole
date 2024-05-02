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

public class Pawn extends ChessPiece {

	private ChessMatch chessMatch;
	
	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Location p = new Location(0, 0);

		if (getColor() == Color.WHITE) {
			p.setValues(location.getRow() - 1, location.getColumn());
			if (getBoard().locationExists(p) && !getBoard().thereIsAPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(location.getRow() - 2, location.getColumn());
			Location p2 = new Location(location.getRow() - 1, location.getColumn());
			if (getBoard().locationExists(p) && !getBoard().thereIsAPiece(p) && getBoard().locationExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(location.getRow() - 1, location.getColumn() - 1);
			if (getBoard().locationExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}			
			p.setValues(location.getRow() - 1, location.getColumn() + 1);
			if (getBoard().locationExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}	
			
			// #specialmove en passant white
			if (location.getRow() == 3) {
				Location left = new Location(location.getRow(), location.getColumn() - 1);
				if (getBoard().locationExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					matrix[left.getRow() - 1][left.getColumn()] = true;
				}
				Location right = new Location(location.getRow(), location.getColumn() + 1);
				if (getBoard().locationExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					matrix[right.getRow() - 1][right.getColumn()] = true;
				}
			}
		}
		else {
			p.setValues(location.getRow() + 1, location.getColumn());
			if (getBoard().locationExists(p) && !getBoard().thereIsAPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(location.getRow() + 2, location.getColumn());
			Location p2 = new Location(location.getRow() + 1, location.getColumn());
			if (getBoard().locationExists(p) && !getBoard().thereIsAPiece(p) && getBoard().locationExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(location.getRow() + 1, location.getColumn() - 1);
			if (getBoard().locationExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}			
			p.setValues(location.getRow() + 1, location.getColumn() + 1);
			if (getBoard().locationExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			
			// #specialmove en passant black
			if (location.getRow() == 4) {
				Location left = new Location(location.getRow(), location.getColumn() - 1);
				if (getBoard().locationExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					matrix[left.getRow() + 1][left.getColumn()] = true;
				}
				Location right = new Location(location.getRow(), location.getColumn() + 1);
				if (getBoard().locationExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					matrix[right.getRow() + 1][right.getColumn()] = true;
				}
			}			
		}
		return matrix;
	}
	
	@Override
	public String toString() {
		return "P";
	}
	
}