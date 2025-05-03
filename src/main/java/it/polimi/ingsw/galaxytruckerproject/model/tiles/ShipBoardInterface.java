package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public interface ShipBoardInterface extends Serializable {

    void initializeTestFlight();

    void initializeLevel2();

    Tile getTile(Coordinates coordinates);

    ArrayList<Coordinates> getCargoHoldCoordinates();

    ArrayList<Coordinates> getBatteryCoordinates();

    ArrayList<Coordinates> getCabinsCoordinates();

    ArrayList<Coverage> getCoverageShields();

    PlayerInterface getPlayer();



    Tile getTile(int x, int y);

    Optional<Tile>[][] getTilesTable();

    //void setHumanCrew(int humanCrew);

    //int getPenalty();

    //Player getPlayer();

    // int getNumBatteries();

    // float getSingleCannonPower();

    // ArrayList<Coordinates> getDoubleCannon();

    //int getNumSingleEngine();

    // ArrayList<Coordinates> getDoubleEngine();

    //int getNumPurpleAliens();

    // int getNumHumanCrew();

    //  int getNumExposedConnectors();

    // int getDoubleCannonPower(Coordinates coordinates);

    // ArrayList<Coordinates> getBatteryCoordinates();

    // ArrayList<Coverage> getCoverageShields();

    //ArrayList<Coordinates> getCargoHoldCoordinates();

    //ArrayList<Coordinates> getCabinsCoordinates();

    //Optional<Tile>[][] getTilesTable();

    void addBreakSingleCannonPower(float power);

    void addBreakSingleEngine(boolean ab);

    void addBreakDoubleEngine(boolean ab, Coordinates coordinates);

    void addBreakDoubleCannon(boolean ab, Coordinates coordinates);

    void addBreakBrownAliens(boolean ab);

    void addBreakPurpleAliens(boolean ab);

    void addBreakHumanCrew(int num);

    void addBreakBatteries(int num);

    void addPenalty();

    boolean addBookedTile(Tile tile);

    ArrayList<Tile> getBookedTiles();

    Tile removeBookedTile(int num);

    boolean positionTile(Optional<Tile> tile, Coordinates coordinates);

    //void destroyForCorrection(Coordinates coordinates);

    //ArrayList<Set<Coordinates>> destroyTile(Coordinates coordinates);

    boolean checkEarlyLanding();



    boolean chooseCrewToRemove(Coordinates coordinates);

    ArrayList<Tile> epidemic();

    boolean chooseBatteryUse(Coordinates coordinates);

    Coverage chooseShields(Coordinates shieldCoordinates, Coordinates batteryCoordinates);

    int gainGoods(Goods goods, Coordinates coordinates);

    void removeGood(Goods good, Coordinates coordinates);

    boolean isCargoEmpty();



    ArrayList<Goods> getAllGoods();

    void addBattery(Coordinates coordinates);


}