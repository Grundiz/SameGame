package view;

import model.SameGameModel;

public interface GameObserver {
    void update(SameGameModel model);
}