package view;

import javax.swing.*;
import java.awt.*;
import model.SameGameModel;

public class SwingView extends JFrame implements GameObserver {

    private SameGameModel model;
    private BoardPanel panel;

    public SwingView(SameGameModel model) {
        this.model = model;

        panel = new BoardPanel();
        panel.addMouseListener(new MouseInputStrategy(model)); // Gör det möjligt att använda musen
        add(panel);

        setTitle("SameGame");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void update(SameGameModel model) {
        this.model = model;
        panel.repaint();
    }

    class BoardPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int[][] board = model.getBoard();

            int rows = board.length;
            int cols = board[0].length;

            int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

            int offsetX = (getWidth() - cols * cellSize) / 2;
            int offsetY = (getHeight() - rows * cellSize) / 2;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {

                    int value = board[i][j];

                    if (value == 0) g.setColor(Color.RED);
                    if (value == 1) g.setColor(Color.BLUE);
                    if (value == 2) g.setColor(Color.GREEN);

                    g.fillRect(
                        offsetX + j * cellSize,
                        offsetY + i * cellSize,
                        cellSize,
                        cellSize
                    );
                }
            }
        }
    }
}
