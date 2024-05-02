/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chess.pieces;

/**
 *
 * @author george
 */
import com.mycompany.board.Board;
import com.mycompany.board.Location;
import com.mycompany.chess.ChessMatch;
import com.mycompany.chess.ChessPiece;
import com.mycompany.chess.Color;

public class Bishop extends ChessPiece{

	public Bishop(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "B";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];
		
		Location p = new Location(0, 0);
		
		// left+up
		p.setValues(location.getRow() - 1, location.getColumn() - 1);
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// up+right
		p.setValues(location.getRow() - 1, location.getColumn() + 1);
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// right+down
		p.setValues(location.getRow() + 1, location.getColumn() + 1);
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// down+left
		p.setValues(location.getRow() + 1, location.getColumn() - 1);
		while( board.locationExists(p) && !board.thereIsAPiece(p) ) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}
		if (board.locationExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		return matrix;
	}
}