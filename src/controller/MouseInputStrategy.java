package controller;

public class MouseInputStrategy extends JPanel {
    private int row, col;

public mouseClicked(int row, int col) {
        this.row = row;   //ska checka vilken box man klickar på
        this.col = col;
        
        addMouseListener(new MouseAdapter() {   //Gör så att den kollar efter musen
          public void mouseClicked(MouseEvent e) {
          public void mouseClicked(MouseEvent e) {

        GameController.removeBlocks(); // ska 

         }
       );
    }
 }
