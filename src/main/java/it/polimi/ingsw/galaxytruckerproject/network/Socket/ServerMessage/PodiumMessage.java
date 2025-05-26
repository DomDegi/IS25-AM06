package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a message sent by the server to notify the client about the podium of the game.
 * This message includes the list of players ranked in the podium.
 */
public class PodiumMessage extends ServerMessage {

    /**
     * A list of players ranked in the podium, typically containing the top players in the game.
     */
    private ArrayList<Player> players;

    /**
     * Constructs a new PodiumMessage with the specified list of players.
     *
     * @param players The list of players ranked in the podium.
     */
    public PodiumMessage(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Processes the podium message by invoking the appropriate method on the server's view
     * to notify the client about the players ranked in the podium.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyPodium(players);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
