package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class CreateGameMessage extends ClientMessage {

    private String gameName;
    private int playerCount;
    private GameMode mode;

    public CreateGameMessage(String gameName, int playerCount, GameMode mode) {
        this.gameName = gameName;
        this.playerCount = playerCount;
        this.mode = mode;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().createGame(gameName, playerCount, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
