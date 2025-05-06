package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.util.ArrayList;

public class InGamePlayers extends ServerMessage {

    ArrayList<LightPlayer> inGamePlayers;

    public InGamePlayers(ArrayList<LightPlayer> inGamePlayers) {
        this.inGamePlayers =  inGamePlayers;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().getFlightBoard().addInLobbyPlayer(inGamePlayers);
    }
}
