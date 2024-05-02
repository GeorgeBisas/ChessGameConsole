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

public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];
		
		Location p = new Location(0, 0);
		
		// left
		p.setValues(location.getRow(), location.getColumn() - 1);
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// right
		p.setValues(location.getRow(), location.getColumn() + 1);
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// up
		p.setValues(location.getRow() - 1, location.getColumn());
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// down
		p.setValues(location.getRow() + 1, location.getColumn());
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		return matrix;
	}
}