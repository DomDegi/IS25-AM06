package it.polimi.ingsw.galaxytruckerproject.lightmodel;

public class LightPlayer {
    private int position; //position on the board
    private LightShipBoard shipboard;
    private int rank;
    private String playerName;
    private boolean landed = false;

    @Override
    public String toString() {
        return playerName+" position:"+position+" rank:"+rank;
    }


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
    public LightShipBoard getShipBoard() {
        return shipboard;
    }
    public void setShipboard(LightShipBoard shipboard) {
        this.shipboard = shipboard;
    }
}
