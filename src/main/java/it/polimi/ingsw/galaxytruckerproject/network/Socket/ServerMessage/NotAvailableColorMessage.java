package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the server to notify the client that certain player colors are unavailable.
 * This message includes a list of player colors that are no longer available for selection.
 */
public class NotAvailableColorMessage extends ServerMessage {

    /**
     * A list of player colors that are unavailable for selection.
     */
    private ArrayList<PlayersColor> color;

    /**
     * Constructs a new NotAvailableColorMessage with the specified list of unavailable player colors.
     *
     * @param color A list of unavailable player colors.
     */
    public NotAvailableColorMessage(ArrayList<PlayersColor> color) {
        this.color = color;
    }

    /**
     * Processes the not available color message by invoking the appropriate method on the client controller
     * to notify the client about the unavailable player colors.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getClientController().colorsNotAvailable(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
