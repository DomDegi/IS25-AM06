package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to choose a player color in the game.
 * This message includes the color chosen by the player.
 */
public class ChooseColorMessage extends ClientMessage {

    /**
     * The color chosen by the player.
     */
    private PlayersColor color;

    /**
     * Constructs a new ChooseColorMessage with the specified player color.
     *
     * @param color The color chosen by the player.
     */
    public ChooseColorMessage(PlayersColor color) {
        this.color = color;
    }

    /**
     * Processes the choose color message by invoking the appropriate method on the
     * client handler's controller, which handles the action of choosing a player color.
     *
     * @param clientHandler The client handler that processes the message.
     */
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().chooseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
