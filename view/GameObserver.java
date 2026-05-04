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
