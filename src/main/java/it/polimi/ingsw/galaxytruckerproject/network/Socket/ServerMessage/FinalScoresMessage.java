package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinalScoresMessage extends  ServerMessage {

    private Map<String,Integer> scores;

    public FinalScoresMessage(Map<String,Integer> scores) {
        this.scores = scores;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showScores(scores);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
