package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to trigger the action of turning the hourglass.
 * This message instructs the server to handle the action of turning the hourglass, typically related
 * to time management in the game.
 */
public class TurnHourglassMessage extends ClientMessage {

    /**
     * Constructs a new TurnHourglassMessage.
     * This constructor is empty as the message does not carry any additional data.
     */
    public TurnHourglassMessage() {
        ;
    }

    /**
     * Processes the turn hourglass message by invoking the appropriate method on the client
     * handler's controller to perform the action of turning the hourglass.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().turnHourglass();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
