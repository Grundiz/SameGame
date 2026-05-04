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
    public void removeBlock(int rows, int cols) { // tar bort bock, edita sen för att ta bort alla runt platsen
    if (board[rows][cols] != -1) {   
        board[rows][cols] = -1;
        notifyObservers();
    }
}
    // test
    public void newGame() {
        generateBoard();
        notifyObservers();
    }
}
