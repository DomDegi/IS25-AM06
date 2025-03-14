package it.polimi.ingsw.galaxytruckerproject.player;

import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;

import java.util.ArrayList;

public class Player {
    String playerName;
    int playerPosition;
    PlayersColor playerColor;
    int credit;
    ShipBoard playerShip;

    public Player(String playerName, PlayersColor playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerPosition = 0;
        this.credit = 0;
        this.playerShip = new ShipBoard(this);
    }

    //GETTER METHODS
    public String getPlayerName() {return playerName;}
    public int getPlayerPosition() {return playerPosition;}
    public PlayersColor getPlayerColor() {return playerColor;}
    public int getCredit() {return credit;}


    public float getCannonStrenght(){
        return playerShip.getCannonStrenght();
    }
    public int getEngineStrenght(){
        return playerShip.getEngineStrenght();
    }
    public ArrayList<Coverage> getShipCoverage(){
        return playerShip.getCoverageShields();
    }
    public





}
