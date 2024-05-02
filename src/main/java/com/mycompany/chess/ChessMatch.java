/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.board.Board;
import com.mycompany.board.Piece;
import com.mycompany.board.Location;
import com.mycompany.chess.pieces.Bishop;
import com.mycompany.chess.pieces.King;
import com.mycompany.chess.pieces.Knight;
import com.mycompany.chess.pieces.Pawn;
import com.mycompany.chess.pieces.Queen;
import com.mycompany.chess.pieces.Rook;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class ChessMatch {

    

	public int turn;
	public Color currentPlayer;
	public Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
        
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	public List<Piece> capturedPieces = new ArrayList<>();
       
	
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();              
                // Initialize captured pieces list
                capturedPieces = new ArrayList<>();
                check = false;
                checkMate = false;      
                promoted = null;
  }
	
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public ChessPiece[][] getPieces() {
		ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				matrix[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return matrix;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Location location = sourcePosition.toPosition();
		validateSourcePosition(location);
		return board.piece(location).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Location source = sourcePosition.toPosition();
		Location target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// #specialmove promotion
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		// #specialmove en passant
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece)capturedPiece;
	}

	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
			return promoted;
		}
		
		Location pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	private Piece makeMove(Location source, Location target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Location sourceT = new Location(source.getRow(), source.getColumn() + 3);
			Location targetT = new Location(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Location sourceT = new Location(source.getRow(), source.getColumn() - 4);
			Location targetT = new Location(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}		
		
		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Location pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Location(target.getRow() + 1, target.getColumn());
				}
				else {
					pawnPosition = new Location(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Location source, Location target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Location sourceT = new Location(source.getRow(), source.getColumn() + 3);
			Location targetT = new Location(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Location sourceT = new Location(source.getRow(), source.getColumn() - 4);
			Location targetT = new Location(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Location pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Location(3, target.getColumn());
				}
				else {
					pawnPosition = new Location(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}
	
	private void validateSourcePosition(Location location) {
		if (!board.thereIsAPiece(location)) {
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(location)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		if (!board.piece(location).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Location source, Location target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	private boolean testCheck(Color color) {
		Location kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) {
						Location source = ((ChessPiece)p).getChessPosition().toPosition();
						Location target = new Location(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
        
         public boolean isGameInProgress() {
                return turn > 1; // Returns true if the turn number is greater than 1
        }
	
	private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}

    public void replaceGame(ChessMatch loadedGame) { 
        // Update the board
        this.board = new Board(loadedGame.board.getRows(), loadedGame.board.getColumns());
            for (int i = 0; i < loadedGame.board.getRows(); i++) {
                for (int j = 0; j < loadedGame.board.getColumns(); j++) {
                    Piece piece = loadedGame.board.piece(i, j); // Get the piece at position (i, j) from the loaded game
                    if (piece != null) { // If there is a piece at position (i, j)
                        // Create a new piece of the same type and color and place it on the board
                        Piece newPiece = null;
                        Color color = ((ChessPiece) piece).getColor(); // Get the color of the piece
                        if (piece instanceof King) {
                            newPiece = new King(this.board, color, loadedGame);
                        } else if (piece instanceof Queen) {
                            newPiece = new Queen(this.board, color);
                        } else if (piece instanceof Rook) {
                            newPiece = new Rook(this.board, color);
                        } else if (piece instanceof Bishop) {
                            newPiece = new Bishop(this.board, color);
                        } else if (piece instanceof Knight) {
                            newPiece = new Knight(this.board, color);
                        } else if (piece instanceof Pawn) {
                            newPiece = new Pawn(this.board, color, loadedGame);
                        }
                        if (newPiece != null) {
                            this.board.placePiece(newPiece, new Location(i, j)); // Place the new piece on the board.
                        }
                    }
                }
            }
            // Update other attributes
            this.turn = loadedGame.turn; // Update the turn number
            this.currentPlayer = loadedGame.currentPlayer; // Update the current player
            this.check = loadedGame.check; // Update check status
            this.checkMate = loadedGame.checkMate; // Update checkmate status
            this.enPassantVulnerable = loadedGame.enPassantVulnerable; // Update en passant status
            this.promoted = loadedGame.promoted; // Update promoted piece
        }



        public void saveGame(String fileName) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            // Write game state information
            writer.write("" + turn + "\n"); // Save turn number
            writer.write("" + currentPlayer + "\n"); // Save current player

            // Save board configuration
            for (int i = 0; i < board.getRows(); i++) {
              for (int j = 0; j < board.getColumns(); j++) {
                Piece piece = board.piece(i, j);
                if (piece instanceof ChessPiece) {
                  writer.write(((ChessPiece) piece).getColor() + "" + piece.getClass().getSimpleName() + "," + i + "," + j + "\n");
                } else {
                  writer.write("-" + "," + i + "," + j + "\n"); // Empty space on board
                }
              }
            }

            // Save captured pieces
            if (!capturedPieces.isEmpty()) {
              writer.write("Captured Pieces:\n");
              for (Piece capturedPiece : capturedPieces) {
                 if (capturedPiece instanceof ChessPiece) {
                      writer.write(((ChessPiece) capturedPiece).getColor() + "" + capturedPiece.getClass().getSimpleName() + "\n");
                           } else {
                      throw new IllegalStateException("Found non-ChessPiece object in capturedPieces list");
                      }
                writer.flush(); // Flush after each captured piece
              }
            }

        writer.close();
      } 
     

   
   
        public Collection<? extends Piece> getCapturedPieces() {
        // Iterate through all board locations
        for (int row = 0; row < board.getRows(); row++) {
          for (int col = 0; col < board.getColumns(); col++) {
            Piece piece = board.piece(row, col);
            // Check if piece is null (empty square) or not a ChessPiece
            if (piece == null || !(piece instanceof ChessPiece)) {
              continue;
            }
            // Check if piece is on the opponent's side of the board
            if (((ChessPiece) piece).getColor() != currentPlayer) {
              capturedPieces.add((ChessPiece) piece); // Add captured piece to the list
            }
          }
        }
        // Explicitly cast capturedPieces to Collection<ChessPiece> before returning
        return Collections.unmodifiableCollection((Collection<Piece>) capturedPieces);
      }



    
public static ChessMatch loadGame(String fileName) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(fileName));

    int turn = Integer.parseInt(reader.readLine());
    String currentPlayerStr = reader.readLine();
    Color currentPlayer = "-".equals(currentPlayerStr) ? null : Color.valueOf(currentPlayerStr);

    Board board = new Board(8, 8);
    List<Piece> capturedPieces = new ArrayList<>(); // Initialize the list of captured pieces

    for (int i = 0; i < 8; i++) {
        String line = reader.readLine();
     //   System.out.println(line); // Print board row
        String[] pieces = line.split(",");
        for (int j = 0; j < pieces.length && j + 4 <= pieces.length; j += 4) {
            String pieceInfo = pieces[j];
            if (!pieceInfo.equals("-")) {
                char colorChar = pieceInfo.charAt(0);
                Color color;
                if (colorChar == 'W') {
                    color = Color.WHITE;
                } else if (colorChar == 'B') {
                    color = Color.BLACK;
                } else {
                    throw new IllegalArgumentException("Invalid color code: " + colorChar);
                }

                String pieceType = pieceInfo.substring(1, pieceInfo.indexOf(',') != -1 ? pieceInfo.indexOf(',') : pieceInfo.length());

                int row = Integer.parseInt(pieces[j + 2]);
                int col = Integer.parseInt(pieces[j + 3]);
                Piece piece;

                switch (pieceType) {
                    case "King" ->  piece = new King(board, color, null);
                    case "Queen" -> piece = new Queen(board, color);
                    case "Rook" -> piece = new Rook(board, color);
                    case "Bishop" -> piece = new Bishop(board, color);
                    case "Knight" -> piece = new Knight(board, color);
                    case "Pawn" -> piece = new Pawn(board, color, null);
                    default -> throw new IOException("Invalid piece type in save file");
                }
                // Create a Location object from the row and column integers
                Location location = new Location(row, col);
                // Call the placePiece method with the Piece object and the Location object
                board.placePiece(piece, location);
            }
        }
    }

    //System.out.println("Captured Pieces:"); // Print captured pieces header
    String capturedPieceLine;
    while ((capturedPieceLine = reader.readLine()) != null) {
       // System.out.println(capturedPieceLine); // Print captured piece line
        String pieceColorStr = capturedPieceLine.substring(0, 5); // Take the first 5 characters for the color
        Color color;
        if ("-".equals(pieceColorStr)) {
            char colorChar = pieceColorStr.charAt(0);

            if (colorChar == 'W') {
                color = Color.WHITE;
            } else if (colorChar == 'B') {
                color = Color.BLACK;
            } else {
                throw new IllegalArgumentException("Invalid color code: " + colorChar);
            }
            String pieceType = capturedPieceLine.substring(5).trim(); // Take the rest of the text for the piece type
            // Create the appropriate piece object based on pieceType and color
            ChessPiece capturedPiece;
            switch (pieceType) {
                case "King" -> capturedPiece = new King(board, color, null);
                case "Queen" -> capturedPiece = new Queen(board, color);
                case "Rook" -> capturedPiece = new Rook(board, color);
                case "Bishop" -> capturedPiece = new Bishop(board, color);
                case "Knight" -> capturedPiece = new Knight(board, color);
                case "Pawn" -> capturedPiece = new Pawn(board, color, null);
                
                default -> throw new IOException("Invalid piece type in save file");
            }
            capturedPieces.add(capturedPiece); // Add the captured piece to the list
        }
    }

    ChessMatch loadedGame = new ChessMatch();
    loadedGame.board = board;
    loadedGame.turn = turn;
    loadedGame.currentPlayer = currentPlayer;
    loadedGame.capturedPieces = capturedPieces; // Set the captured pieces list

    reader.close();
    return loadedGame;
}

}