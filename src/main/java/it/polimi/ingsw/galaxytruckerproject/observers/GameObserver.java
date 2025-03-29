package it.polimi.ingsw.galaxytruckerproject.observers;

import it.polimi.ingsw.galaxytruckerproject.GameState;

public interface GameObserver {
    void notifyChanges(GameState newState);
}
