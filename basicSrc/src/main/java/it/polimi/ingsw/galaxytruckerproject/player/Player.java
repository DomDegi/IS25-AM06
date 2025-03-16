package it.polimi.ingsw.galaxytruckerproject.player;

import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;

import java.util.ArrayList;

public class Player {
    private String playerName;
    private int playerPosition;
    private PlayersColor playerColor;
    private int credit;
    private ShipBoard playerShip;
    private int numCrew;

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
    public ShipBoard getPlayerShip() {return playerShip;}

    public float getCannonStrenght(){
        return playerShip.getCannonStrenght();
    }
    public int getEngineStrenght(){
        return playerShip.getEngineStrenght();
    }
    public ArrayList<Coverage> getShipCoverage(){
        return playerShip.getCoverageShields();
    }
    public int getNumCrew() {return numCrew;}


    //IT REMOVES A CREW MEMBER FROM THE EQUIP CABIN OF THE COORDINATES GIVEN TO THE METHOD
    public void loseCrew(Coordinates coordinates) {
        playerShip.chooseCrewtoRemove(coordinates);
    }

    //IT ADDS CREDIT
    public void gainCredit(int credit) {
        this.credit += credit;
    }

    //THIS METHOD RECEIVES THE COORDINATES OF THE SHIELD THAT WANTS TO BE ACTIVED AND THE COORDINATE OF THE BATTERYCOMPONENTS
    //FROM WHICH IT'S GOING TO BE USED THE ONE BATTERY NECESSSARY TO POWER THE SHIELD
    public Coverage useShield(Coordinates shieldCoordinates, Coordinates batteryCoordinates) {
        return playerShip.chooseShields(shieldCoordinates,batteryCoordinates);
    }














}
