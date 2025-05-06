package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class ShowDiceRollMessage extends ServerMessage{

    private int diceRoll;

    public ShowDiceRollMessage(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showDiceRoll(diceRoll);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
