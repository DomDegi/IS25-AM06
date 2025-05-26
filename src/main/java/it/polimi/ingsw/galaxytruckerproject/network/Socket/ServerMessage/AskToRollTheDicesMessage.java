package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to ask the client to roll the dice.
 * This message triggers the server to prompt the client to roll the dice as part of the game mechanics.
 */
public class AskToRollTheDicesMessage extends ServerMessage {

    /**
     * Constructs a new AskToRollTheDicesMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public AskToRollTheDicesMessage() {}

    /**
     * Processes the ask to roll the dice message by invoking the appropriate method on the server
     * handler's view to prompt the client to roll the dice.
     *
     * @param serverHandler The server handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().asksToRollTheDices();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
