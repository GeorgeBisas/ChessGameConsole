/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chess.pieces;

import com.mycompany.board.Board;
import com.mycompany.board.Location;
import com.mycompany.chess.ChessPiece;
import com.mycompany.chess.Color;

public class Knight extends ChessPiece {

	public Knight(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "N";
	}

	private boolean canMove(Location location) {
		ChessPiece p = (ChessPiece)getBoard().piece(location);
		return p == null || p.getColor() != getColor();
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Location p = new Location(0, 0);
		
		// up+up+right
		p.setValues(location.getRow() - 2, location.getColumn() + 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// up+up+left
		p.setValues(location.getRow() - 2, location.getColumn() - 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// right+right+up   
		p.setValues(location.getRow() - 1, location.getColumn() + 2);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// right+right+down
		p.setValues(location.getRow() + 1, location.getColumn() + 2);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// down+down+right
		p.setValues(location.getRow() + 2, location.getColumn() + 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// down+down+left
		p.setValues(location.getRow() + 2, location.getColumn() - 1);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// left+left+up
		p.setValues(location.getRow() - 1, location.getColumn() - 2);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		// left+left+down
		p.setValues(location.getRow() + 1, location.getColumn() - 2);
		if (board.locationExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
			
		return matrix;
        }
}