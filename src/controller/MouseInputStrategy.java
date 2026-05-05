package controller;

import sounds.Sounds;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import model.SameGameModel;

public class MouseInputStrategy extends MouseAdapter {

    private SameGameModel model;

    public MouseInputStrategy(SameGameModel model) {
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        JPanel panel = (JPanel) e.getComponent();

        int x = e.getX();
        int y = e.getY();

        int[][] board = model.getBoard();
        int rows = board.length;
        int cols = board[0].length;

        int cellSize = Math.min(panel.getWidth() / cols, panel.getHeight() / rows);

        int offsetX = (panel.getWidth() - cols * cellSize) / 2;
        int offsetY = (panel.getHeight() - rows * cellSize) / 2;

        int col = (x - offsetX) / cellSize;
        int row = (y - offsetY) / cellSize;

        if (col >= 0 && col < cols && row >= 0 && row < rows) {

            if (board[row][col] != -1) {
                model.removeBlock(row, col);
                Sounds.playPlace();
            }
        }
    }
}