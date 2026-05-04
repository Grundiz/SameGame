package view;

import model.SameGameModel;

public class ConsoleView implements GameObserver {

    @Override
    public void update(SameGameModel model) {
        System.out.println("Board:");

        int[][] board = model.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}