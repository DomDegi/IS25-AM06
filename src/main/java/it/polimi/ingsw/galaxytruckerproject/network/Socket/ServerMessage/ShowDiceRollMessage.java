package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client about the result of a dice roll.
 * This message includes the value of the dice roll.
 */
public class ShowDiceRollMessage extends ServerMessage {

    /**
     * The value of the dice roll.
     */
    private int diceRoll;

    /**
     * Constructs a new ShowDiceRollMessage with the specified dice roll result.
     *
     * @param diceRoll The value of the dice roll to be shown to the client.
     */
    public ShowDiceRollMessage(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    /**
     * Processes the show dice roll message by invoking the appropriate method on the server's view
     * to display the dice roll result to the client.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showDiceRoll(diceRoll);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
