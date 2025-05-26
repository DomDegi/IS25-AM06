package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;

/**
 * Abstract class representing a client message in the communication between
 * the client and the server. It extends the {@link Message} class and
 * provides functionality for processing specific types of messages sent by the client.
 */
public abstract class ClientMessage extends Message {

    /**
     * The index associated with the client message.
     * This can be used to uniquely identify the message in a sequence of messages.
     */
    private int index;

    /**
     * Abstract method for processing the client message.
     * Subclasses must provide their own implementation of how the message is handled.
     *
     * @param clientHandler The client handler that processes the message.
     */
    public abstract void processMessage(ClientHandler clientHandler);

    /**
     * Sets the index for this client message.
     *
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the index associated with this client message.
     *
     * @return The index of this client message.
     */
    public int getIndex() {
        return index;
    }
}
