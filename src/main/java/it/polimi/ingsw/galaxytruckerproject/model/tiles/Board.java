package it.polimi.ingsw.galaxytruckerproject.model.tiles;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;

import java.util.*;

public interface Board {

    public void initializeTestFlight();
    public void initializeLevel2();

    public Tile getTile(Coordinates coordinates);

    public Tile getTile(int x, int y);


    public void setHumanCrew(int humanCrew);

    public int getPenalty();

    public int getPlayer();

    public int getNumBatteries();

    public float getSingleCannonPower();

    public ArrayList<Coordinates> getDoubleCannon();

    public ArrayList<Coordinates> getNumSingleEngine();

    public ArrayList<Coordinates> getDoubleEngine();

    public int getNumPurpleAliens();

    public int getNumHumanCrew();

    public int getNumExsposedConnectors();

    public int getDoubleCannonPower(Coordinates coordinates);

    public ArrayList<Coordinates> getBatteryCoordinates();

    public ArrayList<Coverage> getCoverageShields();

    public ArrayList<Coordinates> getCargoHoldCoordinates();

    public ArrayList<Coordinates> getCargoShieldCoordinates();

    public ArrayList<Coordinates> getCabinsCoordinates();

    public Optional<Tile>[][] getTilesTable();

    public void addBreakSingleCannonPower(float power);

    public void addBreakSingleEngine(boolean ab);

    public void addBreakDoubleEngine(boolean ab, Coordinates coordinates);

    public void addBreakDoubleCannon(boolean ab, Coordinates coordinates);

    public void addBreakBrownAliens(boolean ab);

    public void addBreakPurpleAliens(boolean ab);

    public void addBreakHumanCrew(int num);

    public void addBreakBatteries(int num);

    public void addPenalty();

    public boolean addBookedTile(Tile tile);

    public ArrayList<Tile> getBookedTiles();

    public Tile removeBookedTile(int num);

    public boolean positionTile(Optional<Tile> tile, Coordinates coordinates);

    public void destroyForCorrection(Coordinates coordinates);

    public ArrayList<Set<Coordinates>> destroyTile(Coordinates coordinates);

    public boolean checkEarlyLanding();

    public boolean verifyCorrectness();

    public boolean chooseCrewToRemove(Coordinates coordinates);

    public ArrayList<Tile> epidemic();

    public boolean chooseBatteryUse(Coordinates coordinates);

    public Coverage chooseShields(Coordinates shieldCoordinates, Coordinates batteryCoordinates);

    public int gainGoods(Goods goods, Coordinates coordinates);

    public void removeGood(Goods good, Coordinates coordinates);




}
