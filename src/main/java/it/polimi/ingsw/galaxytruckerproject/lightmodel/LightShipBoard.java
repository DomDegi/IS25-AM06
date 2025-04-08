package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Optional;

public class LightShipBoard {
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

    public setTilesTable(){
        
    }
}
