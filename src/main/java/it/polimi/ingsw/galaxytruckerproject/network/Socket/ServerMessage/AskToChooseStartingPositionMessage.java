package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to ask the client to choose a starting position.
 * This message triggers the server to request the client to select a starting position in the game.
 */
public class AskToChooseStartingPositionMessage extends ServerMessage {

    /**
     * Constructs a new AskToChooseStartingPositionMessage.
     * This constructor is empty as the message does not carry any additional data.
     */
    public AskToChooseStartingPositionMessage() {}

    /**
     * Processes the ask to choose starting position message by invoking the appropriate method
     * on the server handler's view to request the client to choose a starting position.
     *
     * @param serverHandler The server handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().asksToChooseStartingPosition();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
