/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.mycompany.chess.ChessPosition;
import com.mycompany.chess.Color;
import com.mycompany.chess.ChessMatch;
import com.mycompany.chess.ChessPiece;

public class Board_UI {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void clearScreen() {
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}
	
	public static ChessPosition[] readChessPositions(Scanner sc) {
            try {
                String[] positions = sc.nextLine().split(" ");
                if (positions.length != 2) {
                throw new InputMismatchException("Invalid input. Please enter two positions separated by a space.");
            }
            ChessPosition[] result = new ChessPosition[2];
            result[0] = parseChessPosition(positions[0]);
            result[1] = parseChessPosition(positions[1]);
            return result;
            } catch (InputMismatchException e) {
                throw e;
            } catch (RuntimeException e) {
                throw new InputMismatchException("Error reading ChessPositions. Valid values are from a1 to h8.");
            }
        }
	
	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		printCapturedPieces(captured);
		System.out.println();
		System.out.println("Turn : " + chessMatch.getTurn());
		if (!chessMatch.getCheckMate()) {
			System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
			if (chessMatch.getCheck()) {
				System.out.println("CHECK!");
			}
		} else {
			System.out.println("CHECKMATE!");
			System.out.println("Winner: " + chessMatch.getCurrentPlayer());
		}
	}
	
	public static void printBoard(ChessPiece[][] pieces) {
		System.out.println(ANSI_CYAN + "   _________________" + ANSI_RESET);
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + ANSI_CYAN + " | " + ANSI_RESET);
			for (int j = 0; j < pieces[i].length; j++) {
				printPiece(pieces[i][j], false);
			}
			System.out.println(ANSI_CYAN + "|" + ANSI_RESET);
		}
		System.out.println(ANSI_CYAN + "   _________________" + ANSI_RESET);
		System.out.println("    a b c d e f g h");
	}

	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
		System.out.println(ANSI_CYAN + "   _________________" + ANSI_RESET);
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + ANSI_CYAN + " | " + ANSI_RESET);
			for (int j = 0; j < pieces[i].length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j]);
			}
			System.out.println(ANSI_CYAN + "|" + ANSI_RESET);
		}
		System.out.println(ANSI_CYAN + "   _________________" + ANSI_RESET);
		System.out.println("    a b c d e f g h");
	}
	
	
	private static void printPiece(ChessPiece piece, boolean background) {
		if (background) {
			System.out.print(ANSI_BLUE_BACKGROUND);
		}
		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + piece + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
			}	
		}
		System.out.print(" ");
	}
	
	private static void printCapturedPieces(List<ChessPiece> captured) {
		List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
		List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());
		System.out.println("Captured pieces:");
		System.out.print("White: ");
		System.out.print(ANSI_WHITE);
		System.out.println(Arrays.toString(white.toArray()));
		System.out.print(ANSI_RESET);
		System.out.print("Black: ");
		System.out.print(ANSI_YELLOW);
		System.out.println(Arrays.toString(black.toArray()));
		System.out.print(ANSI_RESET);
	}

        public static ChessPosition parseChessPosition(String position) {
            if (position.length() != 2) {
                throw new IllegalArgumentException("Invalid position format. Please use the format 'a1' to 'h8'.");
            }
            char column = position.charAt(0);
            int row = Integer.parseInt(position.substring(1));
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                throw new IllegalArgumentException("Invalid position. Valid values are from a1 to h8.");
            }
            if (position.length() != 2) {
                throw new IllegalArgumentException("Invalid position format. Please use the format 'a1' to 'h8'.");
            }      
            // Έλεγχος αν η θέση περιέχει μόνο γράμματα ή μόνο αριθμούς
            if (!Character.isLetter(column) || !Character.isDigit(position.charAt(1))) {
                throw new IllegalArgumentException("Invalid position format. Please use the format 'a1' to 'h8'.");
            }
                    return new ChessPosition(column, row);
        }

   
}
