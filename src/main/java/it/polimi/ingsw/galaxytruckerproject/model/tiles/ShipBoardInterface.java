package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Interface representing the public contract for a ShipBoard in Galaxy Trucker.
 * Allows access to ship tiles, operations for damage and crew management,
 * booking and positioning of tiles, and goods management during gameplay.
 */
public interface ShipBoardInterface extends Remote {

    /**
     * Initializes the ship board for a test flight.
     */
    void initializeTestFlight();

    /**
     * Initializes the ship board for Level 2.
     */
    void initializeLevel2();

    /**
     * Returns the tile located at the specified coordinates.
     *
     * @param coordinates the coordinates of the tile.
     * @return the {@link Tile} at the given position.
     */
    Tile getTile(Coordinates coordinates);

    /**
     * Returns the coordinates of all cargo hold tiles.
     *
     * @return a list of coordinates for cargo holds.
     */
    ArrayList<Coordinates> getCargoHoldCoordinates();

    /**
     * Returns the coordinates of all battery components.
     *
     * @return a list of coordinates for battery tiles.
     */
    ArrayList<Coordinates> getBatteryCoordinates();

    /**
     * Returns the coordinates of all cabin tiles.
     *
     * @return a list of coordinates for cabins.
     */
    ArrayList<Coordinates> getCabinsCoordinates();

    /**
     * Returns the shield coverage areas of the ship.
     *
     * @return a list of {@link Coverage} objects representing shield protection.
     */
    ArrayList<Coverage> getCoverageShields();

    /**
     * Returns the player who owns this ship.
     *
     * @return the associated {@link PlayerInterface}.
     */
    PlayerInterface getPlayer();

    /**
     * Returns the tile located at the specified (x, y) position.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @return the {@link Tile} at that position.
     */
    Tile getTile(int x, int y);

    /**
     * Returns the 2D grid of tiles on the board.
     *
     * @return a 2D array of optional tiles.
     */
    Optional<Tile>[][] getTilesTable();

    /**
     * Registers the breaking of a single cannon with a given power value.
     *
     * @param power the power of the cannon to be marked as broken.
     */
    void addBreakSingleCannonPower(float power);

    /**
     * Registers the breaking of a single engine.
     *
     * @param ab true if broken, false otherwise.
     */
    void addBreakSingleEngine(boolean ab);

    /**
     * Registers the breaking of a double engine at the specified coordinates.
     *
     * @param ab true if broken, false otherwise.
     * @param coordinates the location of the engine.
     */
    void addBreakDoubleEngine(boolean ab, Coordinates coordinates);

    /**
     * Registers the breaking of a double cannon at the specified coordinates.
     *
     * @param ab true if broken, false otherwise.
     * @param coordinates the location of the cannon.
     */
    void addBreakDoubleCannon(boolean ab, Coordinates coordinates);

    /**
     * Registers the breaking of brown alien support systems.
     *
     * @param ab true if broken, false otherwise.
     */
    void addBreakBrownAliens(boolean ab);

    /**
     * Registers the breaking of purple alien support systems.
     *
     * @param ab true if broken, false otherwise.
     */
    void addBreakPurpleAliens(boolean ab);

    /**
     * Reduces the human crew by a specified number.
     *
     * @param num the number of crew members to remove.
     */
    void addBreakHumanCrew(int num);

    /**
     * Reduces the number of usable batteries by the specified number.
     *
     * @param num the number of batteries to remove.
     */
    void addBreakBatteries(int num);

    /**
     * Adds a penalty to the player.
     */
    void addPenalty();

    /**
     * Adds a tile to the list of booked tiles, if possible.
     *
     * @param tile the tile to book.
     * @return true if successfully booked, false otherwise.
     */
    boolean addBookedTile(Tile tile);

    /**
     * Returns the list of all currently booked tiles.
     *
     * @return a list of {@link Tile} objects.
     */
    ArrayList<Tile> getBookedTiles();

    /**
     * Removes and returns a booked tile by index.
     *
     * @param num the index of the tile to remove.
     * @return the removed {@link Tile}.
     */
    Tile removeBookedTile(int num);

    /**
     * Attempts to position a tile on the board at the given coordinates.
     *
     * @param tile the tile to position.
     * @param coordinates the target position.
     * @return true if the tile was successfully positioned, false otherwise.
     */
    boolean positionTile(Optional<Tile> tile, Coordinates coordinates);

    /**
     * Checks whether the ship is eligible for early landing.
     *
     * @return true if early landing conditions are met, false otherwise.
     */
    boolean checkEarlyLanding();

    /**
     * Allows a player to choose and remove a crew member from the specified tile.
     *
     * @param coordinates the tile to remove the crew from.
     * @return true if removal was successful, false otherwise.
     */
    boolean chooseCrewToRemove(Coordinates coordinates);

    /**
     * Applies the effects of an epidemic and returns the affected tiles.
     *
     * @return a list of tiles affected by the epidemic.
     */
    ArrayList<Tile> epidemic();

    /**
     * Chooses whether to use a battery at a given coordinate.
     *
     * @param coordinates the location to consider.
     * @return true if a battery is used, false otherwise.
     */
    boolean chooseBatteryUse(Coordinates coordinates);

    /**
     * Chooses which shield to activate using the specified coordinates for shield and battery.
     *
     * @param shieldCoordinates the location of the shield.
     * @param batteryCoordinates the location of the battery.
     * @return the chosen {@link Coverage} direction.
     */
    Coverage chooseShields(Coordinates shieldCoordinates, Coordinates batteryCoordinates);

    /**
     * Adds goods to a cargo tile at the specified coordinates.
     *
     * @param goods the goods to add.
     * @param coordinates the location of the cargo tile.
     * @return the number of goods successfully stored.
     */
    int gainGoods(Goods goods, Coordinates coordinates);

    /**
     * Removes a specific good from a tile at the given coordinates.
     *
     * @param good the good to remove.
     * @param coordinates the location of the cargo tile.
     */
    void removeGood(Goods good, Coordinates coordinates);

    /**
     * Checks whether all cargo holds are empty.
     *
     * @return true if no goods are stored, false otherwise.
     */
    boolean isCargoEmpty();

    /**
     * Returns a list of all goods currently stored on the ship.
     *
     * @return a list of {@link Goods}.
     */
    ArrayList<Goods> getAllGoods();

    /**
     * Adds a battery to the specified coordinates.
     *
     * @param coordinates the location to place the battery.
     */
    void addBattery(Coordinates coordinates);

}
