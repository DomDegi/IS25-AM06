package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private final String gameName;
    private final GameMode gameMode;
    private final int maxPlayerCount;
    private final int currentPlayerCount;

    public GameInfo(GameController gameController) {
        this.gameName = gameController.getGameName();
        this.gameMode = gameController.getGame().getMode();
        this.maxPlayerCount = gameController.getGame().getPlayerCount();
        this.currentPlayerCount = gameController.getPlayersViewMap().size();
    }

    public String getGameName() {
        return gameName;
    }
    public GameMode getGameMode() {
        return gameMode;
    }
    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }
    public  int getCurrentPlayerCount() {
        return currentPlayerCount;
    }
    @Override
    public String toString(){
        return gameName + " GameMode: " + gameMode + " " + currentPlayerCount + "/" + maxPlayerCount;
    }
}
