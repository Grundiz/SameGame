import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import javax.swing.*;

enum GameEvent {
    MOVE_PLACED,
    PIECES_CAPTURED,
    GAME_WON
}

interface GameObserver {
    void onGameUpdated(GameEvent event);
}
class GameModel {
    private List<GameObserver> observers = new ArrayList<>();

    public void addObserver(GameObserver o) {
        observers.add(o);
    }

    public void removeObserver(GameObserver o) {
        observers.remove(o);
    }

    public void notifyObservers(GameEvent event) {
        for (GameObserver o : observers) {
            o.onGameUpdated(event);
        }
    }
}

class SoundObserver implements GameObserver {

    public void onGameUpdated(GameEvent event) {
        switch (event) {
            case MOVE_PLACED:
                CellPanel.playSound("place.wav");
                break;

            case PIECES_CAPTURED:
                CellPanel.playSound("click.wav");
                break;

            case GAME_WON:
                CellPanel.playSound("winner.wav");
                break;
        }
    }
}

public class Othello {

public static JTextField textField;
public static JTextField scoreField;
public static int players = 4;
public static int preplaced = 0;
public static GameModel model; // För observers

public static CellPanel[][] board;


    public static void main(String[] args) {

model = new GameModel(); //Observer stuff
model.addObserver(new SoundObserver());


        JFrame frame = new JFrame("Othello demo");
        frame.setSize(400, 400);
        int rows = 5*players;
        int cols = 5*players;

JButton howToPlayButton = new JButton("How to play");      
JButton extraButton = new JButton("Extra");  
textField = new JTextField(10);
textField.setText("Blacks turn to play!");
scoreField = new JTextField(20);
scoreField.setText("Score...");

JPanel gridPanel = new JPanel(new GridLayout(rows, cols));   // automatisk grid med knappar
JPanel topPanel = new JPanel(new GridLayout(1,4)); //
topPanel.add(textField);
topPanel.add(scoreField);
topPanel.add(howToPlayButton);
topPanel.add(extraButton);

board = new CellPanel[rows][cols];   // Lägger till så att vi kan edita boarden

for (int r = 0; r < rows; r++) {
    for (int c = 0; c < cols; c++) {
        board[r][c] = new CellPanel(r, c);
        gridPanel.add(board[r][c]);
    }
}
/* STANDARD START AV OLIKA SPELARE SKA FLYTTAS IN EN EN ANNAN JAVA FIL SENARE ÄR TANKEN*/
int midR = cols / 2;
int midC = rows / 2;
switch(players){
case 2:
// standard startposition
board[midR - 1][midC - 1].setBlack();
board[midR][midC].setBlack();
board[midR - 1][midC].setRed();
board[midR][midC - 1].setRed();
preplaced = 4;
break;
case 3:
// center  battle
board[midR][midC].setBlack();
board[midR - 3][midC].setRed();
board[midR - 1][midC].setRed();
board[midR + 1][midC].setGreen();
board[midR + 3][midC].setGreen();
board[midR][midC+1].setBlack();

board[midR - 1][midC+1].setRed();
board[midR + 1][midC+1].setGreen();
board[midR][midC-1].setBlack();
board[midR][midC-3].setBlack();
board[midR - 1][midC-1].setRed();
board[midR + 1][midC-1].setGreen();
preplaced = 12;
break;
case 4:
// BLACK (top)
board[midR - 2][midC].setBlack();
board[midR - 3][midC].setBlack();
board[midR - 2][midC - 1].setBlack();
board[midR - 2][midC + 1].setBlack();
board[midR - 1][midC].setBlack();
board[0][rows-1].setBlack();

// RED (right)
board[midR][midC + 2].setRed();
board[midR][midC + 3].setRed();
board[midR - 1][midC + 2].setRed();
board[midR + 1][midC + 2].setRed();
board[midR][midC + 1].setRed();
board[cols-1][rows-1].setRed();

// GREEN (bottom)
board[midR + 2][midC].setGreen();
board[midR + 3][midC].setGreen();
board[midR + 2][midC - 1].setGreen();
board[midR + 2][midC + 1].setGreen();
board[midR + 1][midC].setGreen();
board[cols-1][0].setGreen();

// BLUE (left)
board[midR][midC - 2].setBlue();
board[midR][midC - 3].setBlue();
board[midR - 1][midC - 2].setBlue();
board[midR + 1][midC - 2].setBlue();
board[midR][midC - 1].setBlue();
board[0][0].setBlue();
preplaced = 24;
break;
}
CellPanel.playSound("Notstolen.wav");
/* SLUTET PÅ DET SOM BORDE FLYTTAS */
CellPanel.score();
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH); // högst upp
        frame.add(gridPanel, BorderLayout.CENTER); // mitten
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
class CellPanel extends JPanel { //denna klass har allt som har med spelet att göra
private static int player_turn = 0;
private boolean Cirkel;
private boolean Cirkel1;
private boolean Cirkel2;
private boolean Cirkel3;

private int row;
private int col;
private static int wincon = -1;
private static int check_wincon = Othello.board.length * Othello.board[0].length;;

    public CellPanel(int row, int col) {
    this.row = row;
    this.col = col;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));  //gör kanterna till svart så att griden visas

        addMouseListener(new MouseAdapter() {   //Gör så att den kollar efter musen
            public void mouseClicked(MouseEvent e) {
            if (Cirkel || Cirkel1 || Cirkel2 || Cirkel3) {
            return;
                }
            int r = CellPanel.this.row;
            int c = CellPanel.this.col;
            System.out.println("Clicked: " + r + ", " + c);
            


switch (player_turn) {
    case 0:
        Cirkel = true;
        repaint();
        Othello.model.notifyObservers(GameEvent.MOVE_PLACED);
        Othello.textField.setText("Reds turn to play");
        CellPanel.capture(r, c, player_turn);
        CellPanel.score();
        player_turn = 1;
        break;

    case 1:
        Cirkel1 = true;
        repaint();
        Othello.model.notifyObservers(GameEvent.MOVE_PLACED);
        CellPanel.capture(r, c, player_turn);
        CellPanel.score();
        if(Othello.players >= 3){
        player_turn = 2;
        Othello.textField.setText("Greens turn to play");
        }
        else{
        Othello.textField.setText("Blacks turn to play");
        player_turn = 0;
        }
        break;

    case 2:
        Cirkel2 = true;
        repaint();
        Othello.model.notifyObservers(GameEvent.MOVE_PLACED);
        CellPanel.capture(r, c, player_turn);
        CellPanel.score();
        if(Othello.players == 4){
        Othello.textField.setText("Blues turn to play");
        player_turn = 3;
        }
        else{
        Othello.textField.setText("Blacks turn to play");
        player_turn = 0;
        }
        break;

    case 3:
        Cirkel3 = true;
        repaint();
        Othello.model.notifyObservers(GameEvent.MOVE_PLACED);
        CellPanel.capture(r, c, player_turn);
        CellPanel.score();
        Othello.textField.setText("Blacks turn to play");
        player_turn = 0;
        break;
}
       
       }
       }
       );
    }
    public void paintComponent(Graphics g) {  
       super.paintComponent(g);  //utan denna verkar det som att den inte tar bort saker korrekt.

        if (Cirkel) {
            g.setColor(Color.BLACK);
            int padding = 10;
            g.fillOval(padding, padding,
                       getWidth() - 2 * padding,
                       getHeight() - 2 * padding);
        }
                if (Cirkel1) {
            g.setColor(Color.RED);
            int padding = 10;
            g.fillOval(padding, padding,
                       getWidth() - 2 * padding,
                       getHeight() - 2 * padding);
        }
                        if (Cirkel2) {
            g.setColor(Color.GREEN);
            int padding = 10;
            g.fillOval(padding, padding,
                       getWidth() - 2 * padding,
                       getHeight() - 2 * padding);
        }
                        if (Cirkel3) {
            g.setColor(Color.BLUE);
            int padding = 10;
            g.fillOval(padding, padding,
                       getWidth() - 2 * padding,
                       getHeight() - 2 * padding);
        }
    }
    public void setBlack() {
    Cirkel = true;
    repaint();
}

public void setRed() {
    Cirkel1 = true;
    repaint();
}

public void setGreen() {
    Cirkel2 = true;
    repaint();
}

public void setBlue() {
    Cirkel3 = true;
    repaint();
}
public boolean isEmpty() {
    return !Cirkel && !Cirkel1 && !Cirkel2 && !Cirkel3;
}
public static void playSound(String path) {
    try {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public int getOwner() {
     if (Cirkel) return 0; 
     if (Cirkel1) return 1; 
     if (Cirkel2) return 2; 
     if (Cirkel3) return 3; 
     return -1; 
     }
public static void capture(int row, int col, int player){
    int c = col + 1;
    // höger
while (c < Othello.board[0].length) {

    if (Othello.board[row][c].isEmpty()) {
        break;
    }

    if (Othello.board[row][c].getOwner() == player) {
        if ((c - (col)) > 1) 
        Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for(int true1 = col+1; true1 < c; true1++){
                    if (player == 0) {
                Othello.board[row][true1].Cirkel = true;
                Othello.board[row][true1].Cirkel1 = false;
                Othello.board[row][true1].Cirkel2 = false;
                Othello.board[row][true1].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[row][true1].Cirkel = false;
                Othello.board[row][true1].Cirkel1 = true;
                Othello.board[row][true1].Cirkel2 = false;
                Othello.board[row][true1].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[row][true1].Cirkel = false;
                Othello.board[row][true1].Cirkel1 = false;
                Othello.board[row][true1].Cirkel2 = true;
                Othello.board[row][true1].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[row][true1].Cirkel = false;
                Othello.board[row][true1].Cirkel1 = false;
                Othello.board[row][true1].Cirkel2 = false;
                Othello.board[row][true1].Cirkel3 = true;
            }

            Othello.board[row][true1].repaint();
            
        }
        break;
        }

        c++;
    }


    // vänster
    c = col - 1;
while (c >= 0) {

    if (Othello.board[row][c].isEmpty()) {
        break;
    }

    if (Othello.board[row][c].getOwner() == player) {
        if ((col-c) > 1) 
        Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for(int true1 = c; true1 < col; true1++){
                    if (player == 0) {
                Othello.board[row][true1].Cirkel = true;
                Othello.board[row][true1].Cirkel1 = false;
                Othello.board[row][true1].Cirkel2 = false;
                Othello.board[row][true1].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[row][true1].Cirkel = false;
                Othello.board[row][true1].Cirkel1 = true;
                Othello.board[row][true1].Cirkel2 = false;
                Othello.board[row][true1].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[row][true1].Cirkel = false;
                Othello.board[row][true1].Cirkel1 = false;
                Othello.board[row][true1].Cirkel2 = true;
                Othello.board[row][true1].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[row][true1].Cirkel = false;
                Othello.board[row][true1].Cirkel1 = false;
                Othello.board[row][true1].Cirkel2 = false;
                Othello.board[row][true1].Cirkel3 = true;
            }

            Othello.board[row][true1].repaint();
            
        }
        break;
        }

        c--;
    }

    // ner
    int r = row + 1;
while (r < Othello.board.length) {

    if (Othello.board[r][col].isEmpty()) {
        break;
    }

    if (Othello.board[r][col].getOwner() == player) {
    if ((r - row) > 1)
        Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for (int true1 = row + 1; true1 < r; true1++) {

            if (player == 0) {
                Othello.board[true1][col].Cirkel = true;
                Othello.board[true1][col].Cirkel1 = false;
                Othello.board[true1][col].Cirkel2 = false;
                Othello.board[true1][col].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[true1][col].Cirkel = false;
                Othello.board[true1][col].Cirkel1 = true;
                Othello.board[true1][col].Cirkel2 = false;
                Othello.board[true1][col].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[true1][col].Cirkel = false;
                Othello.board[true1][col].Cirkel1 = false;
                Othello.board[true1][col].Cirkel2 = true;
                Othello.board[true1][col].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[true1][col].Cirkel = false;
                Othello.board[true1][col].Cirkel1 = false;
                Othello.board[true1][col].Cirkel2 = false;
                Othello.board[true1][col].Cirkel3 = true;
            }

            Othello.board[true1][col].repaint();
            
        }
break;
    }

    r++;
}
    // upp
r = row - 1;

while (r >= 0) {

    if (Othello.board[r][col].isEmpty()) {
        break;
    }

    if (Othello.board[r][col].getOwner() == player) {
    if ((row - r) > 1)
        Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for (int true1 = row - 1; true1 > r; true1--) {

            if (player == 0) {
                Othello.board[true1][col].Cirkel = true;
                Othello.board[true1][col].Cirkel1 = false;
                Othello.board[true1][col].Cirkel2 = false;
                Othello.board[true1][col].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[true1][col].Cirkel = false;
                Othello.board[true1][col].Cirkel1 = true;
                Othello.board[true1][col].Cirkel2 = false;
                Othello.board[true1][col].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[true1][col].Cirkel = false;
                Othello.board[true1][col].Cirkel1 = false;
                Othello.board[true1][col].Cirkel2 = true;
                Othello.board[true1][col].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[true1][col].Cirkel = false;
                Othello.board[true1][col].Cirkel1 = false;
                Othello.board[true1][col].Cirkel2 = false;
                Othello.board[true1][col].Cirkel3 = true;
            }

            Othello.board[true1][col].repaint();
            
        }
        
break;
        
    }

    r--;
}
c = col + 1;
r = row + 1;

while (r < Othello.board.length && c < Othello.board[0].length) {

    if (Othello.board[r][c].isEmpty()) 
    break;

    if (Othello.board[r][c].getOwner() == player) {
    if (Math.min(r - row, c - col) > 1)   
    Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for (int true1 = 1; true1 < Math.min(r - row, c - col); true1++) {
            int rr = row + true1;
            int cc = col + true1;

            if (player == 0) {
                Othello.board[rr][cc].Cirkel = true;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = true;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = true;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = true;
            }

            Othello.board[rr][cc].repaint();
        }

        break;
    }

    r++;
    c++;
}
c = col - 1;
r = row + 1;

while (r < Othello.board.length && c >= 0) {

    if (Othello.board[r][c].isEmpty()) 
    break;

    if (Othello.board[r][c].getOwner() == player) {
        if (Math.min(r - row, col - c) > 1)
            Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for (int true1 = 1; true1 < Math.min(r - row, col - c); true1++) {
            int rr = row + true1;
            int cc = col - true1;

            if (player == 0) {
                Othello.board[rr][cc].Cirkel = true;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = true;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = true;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = true;
            }

            Othello.board[rr][cc].repaint();
        }
        break;
    }

    r++;
    c--;
}
c = col + 1;
r = row - 1;

while (r >= 0 && c < Othello.board[0].length) {

    if (Othello.board[r][c].isEmpty()) 
    break;

    if(Othello.board[r][c].getOwner() == player) {
     if (Math.min(row - r, c - col) > 1)
    Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for (int true1 = 1; true1 < Math.min(row - r, c - col); true1++) {
            int rr = row - true1;
            int cc = col + true1;

            if (player == 0) {
                Othello.board[rr][cc].Cirkel = true;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = true;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = true;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = true;
            }

            Othello.board[rr][cc].repaint();
        }
        break;
    }

    r--;
    c++;
}
c = col - 1;
r = row - 1;

while (r >= 0 && c >= 0) {

    if (Othello.board[r][c].isEmpty())
     break;

    if (Othello.board[r][c].getOwner() == player) {
    if (Math.min(row - r, col - c) > 1)
    Othello.model.notifyObservers(GameEvent.PIECES_CAPTURED);
        for (int true1 = 1; true1 < Math.min(row - r, col - c); true1++) {
            int rr = row - true1;
            int cc = col - true1;

            if (player == 0) {
                Othello.board[rr][cc].Cirkel = true;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 1) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = true;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 2) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = true;
                Othello.board[rr][cc].Cirkel3 = false;
            }

            if (player == 3) {
                Othello.board[rr][cc].Cirkel = false;
                Othello.board[rr][cc].Cirkel1 = false;
                Othello.board[rr][cc].Cirkel2 = false;
                Othello.board[rr][cc].Cirkel3 = true;
            }

            Othello.board[rr][cc].repaint();
        }
        break;
    }
    r--;
    c--;
}
}
public static void score(){ // Håller kol på scoren och visar upp nuvarande score på skärmen och wincons
    int black = 0;
    int red = 0;
    int green = 0;
    int blue = 0;
    wincon++;
    for(int row = 0; row < Othello.board.length; row++){
        for(int col = 0; col < Othello.board[0].length; col++){
            if (Othello.board[row][col].Cirkel)
             black++;
            if (Othello.board[row][col].Cirkel1) 
            red++;
            if (Othello.board[row][col].Cirkel2) 
            green++;
            if (Othello.board[row][col].Cirkel3) 
            blue++;
        }
    }
    switch(Othello.players){
    case 2:
           Othello.scoreField.setText("Black: " + black + " Red: " + red);
           break;
    case 3:    
           Othello.scoreField.setText("Black: " + black + " Red: " + red + " Green: " + green);
           break;
    case 4: 
           Othello.scoreField.setText("Black: " + black + " Red: " + red + " Green: " + green + " Blue: " + blue );   
           break;
        }
        
      if(wincon == check_wincon-Othello.preplaced){
    int max = black;
    String winner = "Black";

    if (red > max) {
        max = red;
        winner = "Red";
    }
    if (Othello.players >= 3 && green > max) {
        max = green;
        winner = "Green";
    }
    if (Othello.players == 4 && blue > max) {
        max = blue;
        winner = "Blue";
    }

    Othello.scoreField.setText("Winner: " + winner + " (" + max + ")");
    Othello.model.notifyObservers(GameEvent.GAME_WON);
}
}
}


