package it.polimi.ingsw.galaxytruckerproject.controller.interfaces;

import it.polimi.ingsw.galaxytruckerproject.model.GameState;

import java.rmi.RemoteException;

public interface Observer {
    void update(GameState gameState) throws RemoteException;
}
