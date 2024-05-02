/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.board;

public class Board {
    
    private int rows;
    private int columns;
    private Piece[][] pieces;
    
    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        if (!locationExists(row, column)) {
            throw new BoardException("Location not on the board");    
        }
        return pieces[row][column];
    }
    
    public Piece piece(Location location) {
        if (!locationExists(location)) {
            throw new BoardException("Location not on the board");    
        }
        return pieces[location.getRow()][location.getColumn()];
    }
    
    public void placePiece(Piece piece, Location location) {
        if (thereIsAPiece(location)) {
            throw new BoardException("There is already a piece on location " + location);
        }
        pieces[location.getRow()][location.getColumn()] = piece;
        piece.location = location;
    }
    
    public Piece removePiece(Location location) {
        if (!locationExists(location)) {
            throw new BoardException("Location not on the board");
        }
        if (piece(location) == null) {
            return null;
        }
        Piece aux = piece(location);
        aux.location = null;
        pieces[location.getRow()][location.getColumn()] = null;
        return aux;
    }
    
    private boolean locationExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
    
    public boolean locationExists(Location location) {
        return locationExists(location.getRow(), location.getColumn());
    }
    
    public boolean thereIsAPiece(Location location) {
        if (!locationExists(location)) {
            throw new BoardException("Location not on the board");    
        }
        return piece(location) != null;
    }
    
}

