package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Represents a message sent by the server to notify the client of the final scores.
 * This message includes a map containing the players' names and their corresponding final scores.
 */
public class FinalScoresMessage extends ServerMessage {

    /**
     * A map of players' names and their corresponding final scores.
     */
    private Map<String, Integer> scores;

    /**
     * Constructs a new FinalScoresMessage with the specified scores.
     *
     * @param scores A map of player names and their final scores.
     */
    public FinalScoresMessage(Map<String, Integer> scores) {
        this.scores = scores;
    }

    /**
     * Processes the final scores message by invoking the appropriate method on the server's view
     * to display the final scores to the client.
     *
     * @param serverHandler The server handler that processes the message.
     * @throws RuntimeException if an error occurs while displaying the scores.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showScores(scores);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
