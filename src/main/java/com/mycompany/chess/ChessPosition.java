/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chess;

import com.mycompany.board.Location;

public class ChessPosition {

                public static int length;

		private char column;
		private int row;
		
		public ChessPosition(char column, int row) {
			if (column < 'a' || column > 'h' || row < 1 || row > 8) {
				throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
			}
			this.column = column;
			this.row = row;
		}

		public char getColumn() {
			return column;
		}

		public int getRow() {
			return row;
		}

		protected Location toPosition() {
			return new Location(8 - row, column - 'a');
		}
		
		protected static ChessPosition fromPosition(Location location) {
			return new ChessPosition((char)('a' + location.getColumn()), 8 - location.getRow());
		}
		
		@Override
		public String toString() {
			return "" + column + row;
		}
}