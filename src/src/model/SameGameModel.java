package model;

import Sounds.Sounds;
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

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }

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
        // redan besökt
        if (visited[row][col]) return 0;

        // fel färg
        if (board[row][col] != target) return 0;

        visited[row][col] = true;

        // räknar denna ruta + alla angränsande rutor med samma färg
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

    removeGroup(row, col, target);

    applyGravity();   //fall ner
    shiftLeft();      //flytta allt vänster

if (checkvictory()) { //dessa ska ersättas med observers /////
    System.out.println("You Win!");
    Sounds.playwin();
} 
else if (checkDefeat()) {
    System.out.println("Game Over!");
    Sounds.playdefeat();
}

    notifyObservers();
}

private void shiftLeft() {
    int rows = board.length;
    int cols = board[0].length;

    int writeCol = 0;

    for (int col = 0; col < cols; col++) {


        boolean hasBlock = false; // För att kolla om en kolumm har ett block kvar
        for (int row = 0; row < rows; row++) {
            if (board[row][col] != -1) {
                hasBlock = true;
                break;
            }
        }

        if (hasBlock) {
            if (col != writeCol) { 
                for (int row = 0; row < rows; row++) {
                board[row][writeCol] = board[row][col];
                board[row][col] = -1;
                }
            }

    writeCol++;

        }
    }
}

private void applyGravity() {
    for (int col = 0; col < board[0].length; col++) {

        int move = board.length - 1;

        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] != -1) {
                board[move][col] = board[row][col];
            if (move != row) {
            board[row][col] = -1;
                }

    move--;
    
            }
        }
    }
}

private boolean checkDefeat() {
    int rows = board.length;
    int cols = board[0].length;
    if(!checkvictory()){
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {

            int target = board[row][col];

            if (target == -1) continue;

            boolean[][] visited = new boolean[rows][cols];

            int size = countGroup(row, col, target, visited);

            if (size >= 2) {
                return false;
            }
          }
        }
    }

    return true; // inga grupper >= 2 finns
}

private boolean checkvictory() {
    for (int row = 0; row < board.length; row++) {
        for (int col = 0; col < board[0].length; col++) {
            if (board[row][col] != -1) {
                return false;
            }
        }
    }
    return true;
}

    public void newGame() {
        generateBoard();
        notifyObservers();
    }
}
