enum GameEvent {
    MOVE,
    PIECES_CAPTURED,
    GAME_WON,
    ARROW_UP,
    ARROW_DOWN,
    ARROW_LEFT,
    ARROW_RIGHT
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
            case MOVE:
                Sounds.playSound("place.wav");
                break;

            case PIECES_CAPTURED:
                Sounds.playSound("click.wav");
                break;

            case GAME_WON:
                Sounds.playSound("winner.wav");
                break;
                
            case ARROW_UP:
                System.out.println("Move up");
                break;
                
            case ARROW_DOWN:
                System.out.println("Move down");
                break;
                
            case ARROW_LEFT:
                System.out.println("Move left");
                break;
                
            case ARROW_RIGHT:
                System.out.println("Move right");
                break;
        }
    }
}
