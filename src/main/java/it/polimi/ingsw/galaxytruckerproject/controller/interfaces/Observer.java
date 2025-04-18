package it.polimi.ingsw.galaxytruckerproject.controller.interfaces;

import it.polimi.ingsw.galaxytruckerproject.model.GameState;

public interface Observer {
    void update(GameState gameState);
}
