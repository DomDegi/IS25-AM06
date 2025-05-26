package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a message sent by the server to notify the client about available joinable games.
 * This message includes a list of games that the client can join.
 */
public class ShowJoinableGamesMessage extends ServerMessage {

    /**
     * A list of games that are available for the client to join.
     */
    private ArrayList<GameInfo> joinableGames;

    /**
     * Constructs a new ShowJoinableGamesMessage with the specified list of joinable games.
     *
     * @param joinableGames A list of joinable games that are available for the client.
     */
    public ShowJoinableGamesMessage(ArrayList<GameInfo> joinableGames) {
        this.joinableGames = joinableGames;
    }

    /**
     * Processes the show joinable games message by invoking the appropriate methods on the client controller and view.
     * This updates the client's game info and displays the list of joinable games to the client.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            // Set the joinable games list in the client controller
            serverHandler.getClientController().setGameInfo(joinableGames);
            // Show the list of joinable games to the client
            serverHandler.getView().showJoinableGamesList(joinableGames);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
