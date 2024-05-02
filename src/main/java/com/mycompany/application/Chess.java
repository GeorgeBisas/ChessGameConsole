/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.mycompany.chess.ChessException;
import com.mycompany.chess.ChessMatch;
import com.mycompany.chess.ChessPiece;
import com.mycompany.chess.ChessPosition;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chess {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while (!chessMatch.getCheckMate()) {
            try {
                Board_UI.clearScreen();
                Board_UI.printMatch(chessMatch, captured);
                System.out.println();

                // Prompting the user to enter the move
                System.out.print("Enter your move (source and target squares, e.g., a2a4) or press :help for instructions: ");
                String moveString = sc.nextLine();

                // Handling user commands
                if (moveString.startsWith(":")) {
                    switch (moveString.substring(1).toLowerCase()) {
                        case "save":
                            saveGame(chessMatch);
                            break;
                        case "load":
                            openGame(chessMatch, captured);
                            break;
                        case "exit":
                            if (exitGame(chessMatch)) {
                                return; // Exit the program
                            }
                            break;
                        case "help":
                            printHelp();
                            break;
                        default:
                            System.out.println("Invalid command. Enter ':help' for options.");
                    }
                } else {
                    // Validate and parse move using regex
                    String regex = "([a-h][1-8])([a-h][1-8])"; // Matches two squares (a letter and a number)
                    Matcher matcher = Pattern.compile(regex).matcher(moveString);
                        if (matcher.find()) {
                    String source = matcher.group(1);
                    String target = matcher.group(2);

                    // Extract row and column from source and target strings
                    char sourceRow = source.charAt(0);
                    int sourceCol = Integer.parseInt(source.substring(1));
                    char targetRow = target.charAt(0);
                    int targetCol = Integer.parseInt(target.substring(1));


                    // Create ChessPosition objects using extracted values
                    ChessPosition sourcePosition = new ChessPosition(sourceRow, sourceCol);
                    ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);

                    // Proceed with processing the source and target positions...
                    boolean[][] possibleMoves = chessMatch.possibleMoves(sourcePosition);
                    Board_UI.clearScreen();
                    Board_UI.printBoard(chessMatch.getPieces(), possibleMoves);
                    ChessPiece capturedPiece = chessMatch.performChessMove(sourcePosition, targetPosition);
                    if (capturedPiece != null) {
                        captured.add(capturedPiece);
                    }


                        // Checking for promotion
                        if (chessMatch.getPromoted() != null) {
                            System.out.print("Enter piece for promotion (B/N/R/Q): ");
                            String type = sc.nextLine().toUpperCase();
                            while (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
                                System.out.println("Invalid value! Enter piece for promotion (B/N/R/Q): ");
                                type = sc.nextLine().toUpperCase();
                            }
                            chessMatch.replacePromotedPiece(type);
                        }
                    } else {
                        throw new ChessException("Invalid input. Please enter source and target squares together (e.g., a2a4).");
                    }
                }
            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine(); // Clear invalid input
            }
        }

        // After the game loop ends, print the final state of the game
        Board_UI.clearScreen();
        Board_UI.printMatch(chessMatch, captured);
    }
                               
                  


    private static void saveGame(ChessMatch chessMatch) {
        try {
            System.out.print("Enter file name to save the game: ");
            String fileName = sc.nextLine();

            chessMatch.saveGame(fileName);

            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    private static void openGame(ChessMatch chessMatch, List<ChessPiece> captured) throws IOException {
        if (!chessMatch.isGameInProgress()) {
            System.out.print("Do you want to interrupt the current game and load a saved one? (y/n): ");
            String response = sc.nextLine().toLowerCase();
            if (response.equals("y")) {
                System.out.print("Enter file name to load the game: ");
                String fileName = sc.nextLine();
                ChessMatch loadedGame = ChessMatch.loadGame(fileName);
                //Replace the current game with the loaded game
                chessMatch.replaceGame(loadedGame);
                // Update the captured pieces with those from the loaded game
                captured.clear(); // Clear the current captured pieces
                captured.addAll((Collection<? extends ChessPiece>) loadedGame.getCapturedPieces()); // Add captured pieces from loaded game
            }
        }
    }

    
     
        private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println(":save - Save the current game");
        System.out.println(":load - Load a saved game");
        System.out.println(":exit - Exit the program");
        System.out.println(":help - Display available commands");
    }
        
    
       private static  boolean exitGame(ChessMatch chessMatch) throws IOException{
           // Save the game state before exiting
           saveGame(chessMatch);
           // Close any resources, such as the Scanner
           sc.close();
           // Print a farewell message
           System.out.println("Exiting the game... Goodbye!");
        return true; // Indicate that the game should exit
    }
}
     