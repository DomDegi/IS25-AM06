package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Shields;

public class LightPlayer {
    private int position; //position on the board
    private int rank;
    private String playerName;
    private boolean landed = false;

    public String getPlayerName() {
        return playerName;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public boolean isLanded() {
        return landed;
    }
    public void setLanded(boolean landed) {
        this.landed = landed;
    }
}
