package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Optional;

public class LightShipBoard {

    //Variable that saves the reference to the ShipBoard present in the model along with all the game logic
    private ShipBoard shipBoard;

    protected final Player player; //protected because it need to be called in StartingCabin (Tiles)
    private Optional<Tile>[][] tilesTable;
    private int numExposedConnectors;
    private int penalty;
    private ArrayList<Tile> bookedTiles;
    private int numBatteries;
    private float singleCannonPower;
    private ArrayList<Coordinates> DoubleCannon;

    //TO ENNIO: IF THERE'S A REASON TO NOT USE THIS SET UP, FEEL FREE TO RESET EVERYTHING AS IT WAS
    // private ArrayList<Coordinates> DoubleStraightCannon;
    // private ArrayList<Coordinates> DoubleSideCannon;
    //TO SOHEIL I FEAR RESETTING

    private int numSingleEngine;
    private ArrayList<Coordinates> DoubleEngine;
    private ArrayList<Coverage> shields;
    private ArrayList<Coordinates> batteryCoordinates;
    private ArrayList<Coordinates> crewCoordinates;
    private ArrayList<Coordinates> cargoHoldCoordinates;
    private int numBrownAliens;
    private int numPurpleAliens;
    private int numHumanCrew;


    public LightShipBoard(ShipBoard shipBoard) {
        this.player = shipBoard.getPlayer();
        this.shipBoard = shipBoard;
    }


    public void setTilesTable(Coordinates coordinates) {
        tilesTable = shipBoard.getTilesTable();
    }

    public void setNumSingleEngine() {
        this.numSingleEngine = shipBoard.getNumSingleEngine();
    }

    public void setDoubleEngine() {
        this.DoubleEngine = shipBoard.getDoubleEngine();
    }

    public void setNumBrownAliens() {
        this.numBrownAliens = shipBoard.getNumBrownAliens();
    }

    public void setNumPurpleAliens() {
        this.numPurpleAliens = shipBoard.getNumPurpleAliens();
    }

    public void setNumHumanCrew() {
        this.numHumanCrew = shipBoard.getNumHumanCrew();
    }

    public void setNumBattery() {
        this.numBatteries = shipBoard.getNumBatteries();
    }

    public void setSingleCannonPower() {
        this.singleCannonPower = shipBoard.getSingleCannonPower();
    }

    public void setDoubleCannon() {
        this.DoubleCannon = shipBoard.getDoubleCannon();
    }

    public void setNumExposedConnectors(){
        this.numExposedConnectors = shipBoard.getNumExposedConnectors();
    }

    public void setShields() {
        this.shields = shipBoard.getCoverageShields();
    }

    public void setBatteryCoordinates() {
        this.batteryCoordinates = shipBoard.getBatteryCoordinates();
    }

    public void setCrewCoordinates() {
        this.crewCoordinates = shipBoard.getCabinsCoordinates();
    }

    public void setCargoHoldCoordinates() {
        this.cargoHoldCoordinates = shipBoard.getCargoHoldCoordinates();
    }

    public void setBookedTiles() {
        this.bookedTiles = shipBoard.getBookedTiles();
    }





}
