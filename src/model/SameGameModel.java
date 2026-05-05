package model;

import java.util.ArrayList;
import java.util.List;

import view.GameObserver;

public class SameGameModel {

    private List<GameObserver> observers = new ArrayList<>();

    private int[][] board;

    public SameGameModel(int rows, int cols) {
        board = new int[rows][cols];
        generateBoard();
    }

    private void generateBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = (int)(Math.random() * 3); // 3 färger
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public void addObserver(GameObserver o) {
        observers.add(o);
    }

    public void notifyObservers() {
        for (GameObserver o : observers) {
            o.update(this);
        }
    }

    private void removeGroup(int row, int col, int target) {

        // utanför grid
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }

        // annan färg eller redan borttagen
        if (board[row][col] != target) {
            return;
        }

        // ta bort
        board[row][col] = -1;

        // kolla grannar 
        removeGroup(row + 1, col, target); // ner
        removeGroup(row - 1, col, target); // upp
        removeGroup(row, col + 1, target); // höger
        removeGroup(row, col - 1, target); // vänster
    }

    public void removeBlock(int row, int col) { 
    int target = board[row][col];

    if (target == -1) return;

    // ta bort hela gruppen
    removeGroup(row, col, target);

    notifyObservers();
}
    // test
    public void newGame() {
        generateBoard();
        notifyObservers();
    }
}
