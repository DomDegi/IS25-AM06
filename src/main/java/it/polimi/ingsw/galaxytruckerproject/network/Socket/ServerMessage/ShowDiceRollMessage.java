package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class ShowDiceRollMessage extends ServerMessage{

    private int diceRoll;

    public ShowDiceRollMessage(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getView().showDiceRoll(diceRoll);
    }
}
