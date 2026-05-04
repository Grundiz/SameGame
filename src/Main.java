import model.SameGameModel;
import view.ConsoleView;
import view.SwingView;

public class Main {
    public static void main(String[] args) {

        SameGameModel model = new SameGameModel(10, 10);

        ConsoleView console = new ConsoleView();
        SwingView swing = new SwingView(model);

        model.addObserver(console);
        model.addObserver(swing);

        model.newGame();
    }
}