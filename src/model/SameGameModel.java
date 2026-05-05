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

    private int countGroup(int row, int col, int target, boolean[][] visited){
        // utanför
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return 0;
        }
        //redan besökt
        if (visited[row][col]) return 0;
        //fel färg
        if (board[row][col] != target) return 0;

        visited[row][col] = true;

        return 1
        + countGroup(row + 1, col, target, visited)
        + countGroup(row - 1, col, target, visited)
        + countGroup(row, col + 1, target, visited)
        + countGroup(row, col - 1, target, visited);
    }

    public void removeBlock(int row, int col) { 
    int target = board[row][col];

    if (target == -1) return;

    boolean[][] visited = new boolean[board.length][board[0].length];
    int size = countGroup(row, col, target, visited);

    if (size < 2) return;

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
