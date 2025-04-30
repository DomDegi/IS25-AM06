package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.PlayerInterface;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;

public class LightPlayer implements PlayerInterface {
    private int position; //position on the board
    private LightShipBoard shipboard;
    private int rank;
    private String playerName;
    private boolean landed = false;
    private PlayersColor color;
    private int credits;

    public LightPlayer(String playerName, PlayersColor color) {
        this.playerName = playerName;
        this.color = color;
    }
    public LightPlayer(Player player){
        this.playerName = player.getPlayerName();
        this.color = player.getPlayerColor();
    }
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
    public void bookTile(Tile tile) throws RemoteException {
       boolean mustBeTrue= shipboard.addBookedTile(tile);
       if(!mustBeTrue){
           System.out.println("error in tile boooking "+tile);
       }
    }

    @Override
    public PlayersColor getPlayerColor() {
        return color;
    }
    public void gainCredits(int credits) {
        this.credits+=credits;
    }
    public int getCredits() {
        return credits;
    }
    public void SetPlayerShip(LightShipBoard shipBoard){
        this.shipboard = shipBoard;
    }
}
