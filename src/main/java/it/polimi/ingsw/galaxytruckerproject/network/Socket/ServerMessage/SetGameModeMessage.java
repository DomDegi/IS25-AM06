package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class SetGameModeMessage extends  ServerMessage{

    private GameMode gameMode;

    public SetGameModeMessage(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().setGameMode(this.gameMode);
        serverHandler.getClientController().setGameMode(this.gameMode);
    }
}
