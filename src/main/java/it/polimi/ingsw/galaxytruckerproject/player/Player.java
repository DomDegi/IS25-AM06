package it.polimi.ingsw.galaxytruckerproject.player;

import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;

public class Player {
    int playerRanking;
    String playerName;
    int playerPosition;
    PlayersColor playerColor;
    int credit;
    ShipBoard playerShip;

    public Player(String playerName, PlayersColor playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerRanking = 0;
        this.playerPosition = 0;
        this.credit = 0;
        this.playerShip = new ShipBoard(this);
    }

    //GETTER METHODS
    public String getPlayerName() {return playerName;}
    public int getPlayerPosition() {return playerPosition;}
    public int getPlayerRanking() {return playerRanking;}
    public void setPlayerRanking(int playerRanking) {this.playerRanking = playerRanking;}
    public void setPlayerPosition(int playerPosition) {this.playerPosition = playerPosition;}
    public PlayersColor getPlayerColor() {return playerColor;}
    public int getCredit() {return credit;}



}
