package it.polimi.ingsw.galaxytruckerproject.model;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private final String gameName;
    private final GameMode gameMode;
    private final int maxPlayerCount;
    private final int currentPlayerCount;

    public GameInfo(String gameName, GameMode gameMode, int maxPlayerCount, int currentPlayerCount) {
        this.gameName = gameName;
        this.gameMode = gameMode;
        this.maxPlayerCount = maxPlayerCount;
        this.currentPlayerCount = currentPlayerCount;
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
        return gameName + " GameMode: " + gameMode + " " + maxPlayerCount + "/" + currentPlayerCount;
    }
}
