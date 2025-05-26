package it.polimi.ingsw.galaxytruckerproject.controller.interfaces;

import it.polimi.ingsw.galaxytruckerproject.model.GameState;

import java.rmi.RemoteException;

/**
 * Observer interface for receiving updates about the current game state.
 * Implemented by client-side components that need to react to changes
 * in the {@link GameState}, typically in a remote RMI-based environment.
 */
public interface Observer {

    /**
     * Notifies the observer of a new {@link GameState}.
     * Called by the observable (e.g., GameController) to propagate updates.
     *
     * @param gameState the updated state of the game
     * @throws RemoteException if communication with the observer fails
     */
    void update(GameState gameState) throws RemoteException;
}
