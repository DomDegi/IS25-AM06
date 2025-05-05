package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ShowJoinableGamesMessage extends ServerMessage{

    ArrayList<GameInfo> joinableGames;

    public ShowJoinableGamesMessage(ArrayList<GameInfo> joinableGames) {
        this.joinableGames = joinableGames;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showJoinableGamesList(joinableGames);
        } catch (RemoteException ignored) {
        }
    }
}
