package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to set the flight board position.
 * This message includes the new position to be set on the flight board.
 */
public class SetFlightBoardPositionMessage extends ClientMessage {

    /**
     * The position to be set on the flight board.
     */
    private int position;

    /**
     * Constructs a new SetFlightBoardPositionMessage with the specified position.
     *
     * @param position The position to be set on the flight board.
     */
    public SetFlightBoardPositionMessage(int position) {
        this.position = position;
    }

    /**
     * Processes the set flight board position message by invoking the appropriate method on the client
     * handler's controller to update the position on the flight board.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().setPosition(position);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
