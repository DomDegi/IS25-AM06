package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents the light version of the shipboard in the game.
 * It simplifies the shipboard data, allowing easier communication between client and server.
 * The `LightShipBoard` class includes methods for initializing the ship, positioning tiles, managing cargo,
 * and tracking crew, shields, and other game-related elements.
 * <p>
 * It is used in conjunction with the `ShipBoard` class to manage the game state on the client side.
 * </p>
 */
public class LightShipBoard implements ShipBoardInterface , Remote {

    /**
     * Reference to the full `ShipBoard` instance that holds all game logic.
     */
    private ShipBoard shipBoard;

    /**
     * The player associated with this shipboard.
     */
    protected final LightPlayer player;

    /**
     * 2D array representing the ship's tiles, where each tile can be empty or filled with a `Tile` object.
     */
    private Optional<Tile>[][] tilesTable;

    /**
     * Represents the penalty score associated with this shipboard.
     */
    private int penalty;

    /**
     * List of tiles that are booked but not yet placed on the shipboard.
     */
    private ArrayList<Tile> bookedTiles;

    /**
     * The number of exposed connectors on the ship.
     */
    private int numExposedConnectors;

    /**
     * The number of batteries present on the ship.
     */
    private int numBatteries;

    /**
     * Power of the single cannon installed on the ship.
     */
    private float singleCannonPower;

    /**
     * List of coordinates where the double cannons are located.
     */
    private ArrayList<Coordinates> DoubleCannon;

    /**
     * Coordinates of the cargo hold tiles on the shipboard.
     */
    private ArrayList<Coordinates> cargoHoldCoordinates;

    /**
     * The number of single-engine components installed on the ship.
     */
    private int numSingleEngine;

    /**
     * List of coordinates where the double engines are located.
     */
    private ArrayList<Coordinates> DoubleEngine;

    /**
     * List of shields installed on the ship.
     */
    private ArrayList<Coverage> shields;

    /**
     * Coordinates of the battery tiles.
     */
    private ArrayList<Coordinates> batteryCoordinates;

    /**
     * Coordinates of the crew cabins on the shipboard.
     */
    private ArrayList<Coordinates> crewCoordinates;

    /**
     * The number of brown aliens on the ship.
     */
    private int numBrownAliens;

    /**
     * The number of purple aliens on the ship.
     */
    private int numPurpleAliens;

    /**
     * The number of human crew members on the ship.
     */
    private int numHumanCrew;

    /**
     * The total credit score of the player.
     */
    private int credit;

    /**
     * Flag to indicate if this is the first time the shipboard is being initialized.
     */
    private boolean first = true;

    /**
     * Flag to indicate if the shipboard has been completed.
     */
    private boolean completed = false;

    /**
     * Constructor that creates a light version of the shipboard from a full `ShipBoard`.
     * Initializes the player's shipboard and copies relevant data from the full version.
     *
     * @param shipBoard the full `ShipBoard` instance to extract data from
     */
    public LightShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        this.bookedTiles = new ArrayList<>();
        this.cargoHoldCoordinates = new ArrayList<>();
        this.crewCoordinates = new ArrayList<>();
        this.credit = 0;
        this. player = new LightPlayer(this.shipBoard.getPlayer());
        convertToLightShipBoard(shipBoard);
    }

    /**
     * Constructor for creating an empty shipboard for a player.
     * Initializes all properties of the shipboard to default values.
     *
     * @param player the `LightPlayer` to associate with this shipboard
     */
    public LightShipBoard(LightPlayer player) {
        this.player = player;
        this.player.setPlayerShip(this);
        this.penalty = 0;
        this.bookedTiles = new ArrayList<Tile>();
        this.numBatteries = 0;
        this.singleCannonPower = 0;
        this.DoubleCannon = new ArrayList<Coordinates>();
        this.batteryCoordinates= new ArrayList<Coordinates>();
        this.crewCoordinates=  new ArrayList<Coordinates>();
        this.cargoHoldCoordinates= new ArrayList<Coordinates>();
        this.numSingleEngine = 0;
        this.DoubleEngine = new ArrayList<Coordinates>();
        this.shields = new ArrayList<Coverage>();
        this.numBrownAliens = 0;
        this.numPurpleAliens = 0;
        this.numExposedConnectors = 0;
        this.numHumanCrew = 0;
        this.credit = 0;
    }

    /**
     * Initializes the shipboard for the "Test Flight" mode by setting up tiles and cabins.
     */
    public void initializeTestFlight() {
        this.tilesTable = new Optional[5][7];
        // Inizializza le caselle riempibili a null
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                this.tilesTable[i][j] = Optional.empty();

            }
        }
        // Coordinates of VoidTile
        int[][] voidPositions = {
                {0, 0}, {0, 1}, {0, 2}, {0, 4}, {0, 5}, {0, 6},
                {1, 0}, {1, 1}, {1, 5}, {1, 6},
                {2, 0}, {2, 6}, {3, 0}, {3, 6},
                {4, 0}, {4, 3}, {4, 6}
        };

        // Initialize VoidTile
        for (int[] pos : voidPositions) {
            tilesTable[pos[0]][pos[1]] = Optional.of(new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),null));
        }
        Tile tile;
        String blue="/images/startingCabins/blue_starting_cabin.png";
        String yellow="/images/startingCabins/yellow_starting_cabin.png";
        String green="/images/startingCabins/green_starting_cabin.png";
        String red="/images/startingCabins/red_starting_cabin.png";
        if(player.getPlayerColor()== PlayersColor.BLUE)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),blue,0, 0);
        else if(player.getPlayerColor()== PlayersColor.YELLOW)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),yellow,0, 0);
        else if(player.getPlayerColor()== PlayersColor.GREEN)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),green,0, 0);
        else if(player.getPlayerColor()== PlayersColor.RED)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),red,0, 0);
        else
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web157.jpg",0, 0);
        positionTile(Optional.of(tile), new Coordinates(2, 3));

        this.tilesTable = tilesTable;
    }


    /**
     * Initializes the shipboard for the "Level 2" mode by setting up tiles and cabins.
     */
    public void initializeLevel2() {
        this.tilesTable = new Optional[5][7];
        //Set empty the normal Tile
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                this.tilesTable[i][j] = Optional.empty();

            }
        }
        int[][] voidPositions = {
                {0, 0}, {0, 1}, {1, 0}, {4, 3}, {0, 3}, {0, 5},
                {0, 6}, {1, 6},
        };

        // Initialize VoidTile
        for (int[] pos : voidPositions) {
            this.tilesTable[pos[0]][pos[1]] = Optional.of(new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),null));
        }
        Tile tile;
        String blue="/images/startingCabins/blue_starting_cabin.png";
        String yellow="/images/startingCabins/yellow_starting_cabin.png";
        String green="/images/startingCabins/green_starting_cabin.png";
        String red="/images/startingCabins/red_starting_cabin.png";
        if(player.getPlayerColor()== PlayersColor.BLUE)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),blue,0, 0);
        else if(player.getPlayerColor()== PlayersColor.YELLOW)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),yellow,0, 0);
        else if(player.getPlayerColor()== PlayersColor.GREEN)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),green,0, 0);
        else if(player.getPlayerColor()== PlayersColor.RED)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),red,0, 0);
        else
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web157.jpg",0, 0);
        positionTile(Optional.of(tile), new Coordinates(2, 3));
    }

    /**
     * Retrieves the player associated with this shipboard.
     *
     * @return the player associated with this shipboard
     */
    public LightPlayer getPlayer() {
        return player;
    }

    /**
     * Adds power to the ship's cannon.
     *
     * @param num the amount of power to add
     */
    public void addBreakSingleCannonPower(float num) {
        this.singleCannonPower += num;
    }

    /**
     * Adjusts the number of single-engine components on the ship.
     *
     * @param ab indicates whether to increase or decrease the number of single-engine components
     */
    public void addBreakSingleEngine(boolean ab) {
        if (ab) numSingleEngine++;
        else numSingleEngine--;
    }
    /**
     * Adjusts the number of double-engine components on the ship.
     *
     * @param ab indicates whether to increase or decrease the number of double-engine components
     * @param coordinates the coordinates of the double-engine tile
     */
    public void addBreakDoubleEngine(boolean ab, Coordinates coordinates) {
        if (ab) {
            DoubleEngine.add(coordinates);
        } else DoubleEngine.remove(coordinates);
    }
    /**
     * Adjusts the number of brown aliens on the ship.
     *
     * @param ab indicates whether to increase or decrease the number of brown aliens
     */
    public void addBreakDoubleCannon(boolean ab, Coordinates coordinates) {
        if (ab) {
            DoubleCannon.add(coordinates);
        } else DoubleCannon.remove(coordinates);
    }
    /**
     * Adjusts the number of purple aliens on the ship.
     *
     * @param ab indicates whether to increase or decrease the number of purple aliens
     */
    public void addBreakBrownAliens(boolean ab) {
        if (ab) numBrownAliens++;
        else numBrownAliens--;
    }

    /**
     * Adjusts the number of purple aliens on the ship.
     *
     * @param ab indicates whether to increase or decrease the number of purple aliens
     */
    public void addBreakPurpleAliens(boolean ab) {
        if (ab) numPurpleAliens++;
        else numPurpleAliens--;
    }
    /**
     * Adjusts the number of human crew members on the ship.
     *
     * @param num the number of human crew members to add or remove
     */
    public void addBreakHumanCrew(int num) {
        numHumanCrew += num;
        //Decide which crew to eliminate
    }
    /**
     * Adjusts the number of batteries on the ship.
     *
     * @param num the number of batteries to add or remove
     */
    public void addBreakBatteries(int num) {
        numBatteries += num;
        //if (num<0) -> Decide which Battery to use
    }
    /**
     * Adds a penalty to the shipboard's penalty score.
     */
    public void addPenalty() {
        penalty++;
    }

    /**
     * Retrieves the tile at the specified coordinates.
     *
     * @param coordinates the coordinates of the tile to retrieve
     * @return the `Tile` at the given coordinates, or `null` if no tile is present
     */
    public Tile getTile(Coordinates coordinates){
        if(tilesTable[coordinates.getX()][coordinates.getY()].isPresent())
            return tilesTable[coordinates.getX()][coordinates.getY()].get();
        else
            return null;
    }

    /**
     * Retrieves the tile at the specified coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the `Tile` at the given coordinates, or `null` if no tile is present
     */
    public Tile getTile(int x, int y){
        if(tilesTable[x][y].isPresent())
            return tilesTable[x][y].get();
        else
            return null;
    }

    /**
     * Positions a tile at the specified coordinates on the shipboard.
     *
     * @param tile the tile to position
     * @param coordinates the coordinates where the tile should be positioned
     * @return `true` if the tile was successfully positioned, `false` otherwise
     */
    public boolean positionTile(Optional<Tile> tile, Coordinates coordinates) {
        if (tile.isPresent() && tilesTable[coordinates.getX()][coordinates.getY()].isEmpty()) {
            if (first){
                tilesTable[coordinates.getX()][coordinates.getY()] = tile;
                tile.get().setShipBoard(this);
                tile.get().setCoordinates(coordinates);
                first=false;
                return true;
            }
            else if (tile.get().canPosition( coordinates, this)) {
                tilesTable[coordinates.getX()][coordinates.getY()] = tile;
                tile.get().setShipBoard(this);
                tile.get().setCoordinates(coordinates);
                return true;
            }
        }
        return false;
    }

    /**
     * Positions a tile at the specified coordinates without checking adjacency.
     *
     * @param tile the tile to position
     * @param coordinates the coordinates where the tile should be positioned
     * @return `true` if the tile was successfully positioned, `false` otherwise
     */
    public boolean positionTileWithoutAdjacencyCheck(Optional<Tile> tile, Coordinates coordinates) {
        if (tile.isPresent() && tilesTable[coordinates.getX()][coordinates.getY()].isEmpty()) {
            tilesTable[coordinates.getX()][coordinates.getY()] = tile;
            tile.get().setShipBoard(this);
            tile.get().setCoordinates(coordinates);
            first=false;
            return true;
        }
        return false;
    }
    /**
     * Positions a null object at those coordinates
     *
     * @param tile null
     * @param coordinates the coordinates of the tile to remove
     */
    public void positionNullTile(Optional<Tile> tile, Coordinates coordinates) {
        if (tilesTable[coordinates.getX()][coordinates.getY()].isEmpty() && tile.isEmpty()) {
            tilesTable[coordinates.getX()][coordinates.getY()] = Optional.empty();
        }
    }

    /**
     * Method used to update the tile that get their attributes like crew, batteries and cargo goods modified.
     * It swaps the old tile with the new one received from the server and sets the parameters.
     * @param tile modified tile.
     */
    public void swapTile(Optional<Tile> tile) {
        Coordinates coordinates = tile.get().getCoordinates();
        tilesTable[coordinates.getX()][coordinates.getY()] = tile;
        tile.get().setShipBoard(this);
    }


    /**
     * Retrieves the list of shields installed on the ship.
     *
     * @return the list of shields
     */
    public ArrayList<Coverage> getCoverageShields(){
        return shields;
    }

    /**
     * Removes the crew member at the given coordinates on the shipboard.
     *
     * @param coordinates the coordinates of the tile from which the crew is removed
     * @return true if the crew was successfully removed, false otherwise
     */
    public boolean chooseCrewToRemove(Coordinates coordinates) {
        return tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
    }

    /**
     * Consumes a battery from the specified coordinates on the shipboard.
     *
     * @param coordinates the coordinates of the tile where the battery is consumed
     * @return true if the battery was successfully consumed
     */
    public boolean chooseBatteryUse(Coordinates coordinates){
        return tilesTable[coordinates.getX()][coordinates.getY()].get().consumeBattery();
    }


    /**
     * Activates a shield by consuming a battery located at the given coordinates.
     *
     * @param shieldCoordinates the coordinates of the shield tile
     * @param batteryCoordinates the coordinates of the battery to use for activating the shield
     * @return the coverage area of the shield
     */
    public Coverage chooseShields(Coordinates shieldCoordinates, Coordinates batteryCoordinates){
        if(!(tilesTable[shieldCoordinates.getX()][shieldCoordinates.getY()].get().getCoveredArea() == Coverage.NONE)){
            chooseBatteryUse(batteryCoordinates);
        }
        else{
            System.out.println("THE TILE IS NOT A SHIELD");
        }
        return tilesTable[shieldCoordinates.getX()][shieldCoordinates.getY()].get().getCoveredArea();
    }

    /**
     * Counts and returns the number of exposed connectors on the ship.
     *
     * @return number of exposed connectors.
     */
    public int countExposedConnectors() {
        numExposedConnectors = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                checkBorderTile(i, j);
            }
        return numExposedConnectors;
    }

    /**
     * Marks the tile as a border tile and increments exposed connector count if needed.
     *
     * @param i row index.
     * @param j column index.
     */
    public void checkBorderTile(int i, int j) {

        //if the tile at its LEFT is either out of bounds, a VoidTile, or empty,
        //then our Tile is a borderTile and I have to check if it is Exposed
        if ((j - 1 < 0 || tilesTable[i][j-1].isEmpty() || !tilesTable[i][j-1].get().fillable())&& tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getWest().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile at its RIGHT is either out of bounds, a VoidTile, or empty,
        //then our Tile is a borderTile and I have to check if it is Exposed
        if ((j + 1 > 6 ||  tilesTable[i] [j+1].isEmpty() || !tilesTable[i][j+1].get().fillable()) && tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getEast().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile UNDER is either out of bounds, a VoidTile, or empty,
        //then our Tile is a borderTile and I have to check if it is Exposed
        if ((i + 1 > 4  || tilesTable[i+1][j].isEmpty() || !tilesTable[i+1][j].get().fillable())&& tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getSouth().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile OVER is either out of bounds, a VoidTile, or empty,
        //then our Tile is a borderTile and I have to check if it is Exposed
        if ((i - 1 < 0  || tilesTable[i-1][j].isEmpty() || !tilesTable[i-1][j].get().fillable())&& tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getNorth().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;
    }
    /**
     * Adds a good to the cargo hold at the specified coordinates, if the tile is a valid cargo holder.
     *
     * @param goods the goods to be added
     * @param coordinates the coordinates of the cargo hold where the goods are placed
     * @return 0 if the goods were successfully added, -1 if the tile is not a valid cargo holder
     */
    public int gainGoods(Goods goods, Coordinates coordinates) {
        if(cargoHoldCoordinates.contains(coordinates)){
            if(tilesTable[coordinates.getX()][coordinates.getY()].get().addGood(goods)==1)
                return 0;
            if( tilesTable[coordinates.getX()][coordinates.getY()].get().addGood(goods)==-1){
                System.out.println("THIS TILE IS NOT RED CARGO HOLDER, input again");
                return -1;
            }

        }
        else{
            System.out.println("THIS TILE IS NOT A CARGO HOLDER, input again");
            return -1;
        }
        return 1;
    }

    /**
     * Returns the coordinates of all cargo hold tiles that contain a specific type of good.
     *
     * @param good the good type to search for (red, yellow, green, or blue)
     * @return a list of coordinates where the cargo hold contains the specified good
     */
    public ArrayList<Coordinates> cargoHoldContainsGood(Goods good){
        ArrayList<Coordinates> cargoHoldContainsGood = new ArrayList<>();
        for(Coordinates coordinates : cargoHoldCoordinates){
            for(int i=0; i<tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().size(); i++){
                if(tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().get(i).getColor()==good.getColor()){
                    cargoHoldContainsGood.add(coordinates);
                }
            }
        }
        return cargoHoldContainsGood;
    }

    /**
     * Removes a specific good from the given coordinates in the cargo hold.
     *
     * @param good the good to remove
     * @param coordinates the coordinates of the cargo hold where the good should be removed
     */
    public void removeGood(Goods good, Coordinates coordinates){
        tilesTable[coordinates.getX()][coordinates.getY()].get().removeGood(good);
    }

    /**
     * Returns the total number of crew members on the ship (human and aliens).
     *
     * @return the total number of crew members
     */
    public int getNumTotalCrew(){
        return this.numHumanCrew+this.numBrownAliens+this.numPurpleAliens;
    }

    /**
     * Returns the number of batteries available on the ship.
     *
     * @return the number of batteries
     */
    public int getNumBatteries(){
        return numBatteries;
    }

    /**
     * Adds a tile to the booked tiles list if there's space (maximum of 2 booked tiles).
     *
     * @param tile the tile to be added to the booked list
     * @return true if the tile was added, false if the maximum limit of booked tiles is reached
     */
    public boolean addBookedTile(Tile tile) {
        if (bookedTiles.size() == 2) {
            return false;
        }
        bookedTiles.add(tile);
        return true;
    }
    /**
     * Returns the list of booked tiles.
     *
     * @return the list of booked tiles
     */
    public ArrayList<Tile> getBookedTiles() {
        return bookedTiles;
    }
    /**
     * Removes a tile from the booked tiles list.
     *
     * @param num the index of the tile to remove
     * @return the removed tile if successful, null if the index is invalid
     */
    public Tile removeBookedTile(int num) {
        if (num > 1 || num < 0) {
            System.out.println("the tile do not exist");
            return null;
        }
        Tile tile = bookedTiles.get(num);
        bookedTiles.remove(num);
        return tile;
    }


    /**
     * Converts the goods in the cargo hold to credits. Each type of good has a different credit value.
     *
     * @return the total credit value of the goods in the cargo hold
     */
    public int convertGoodsToCredit(){
        int credit = 0;
        Goods good = new Goods(GoodsColor.RED);
        credit = 4 * cargoHoldContainsGood(good).size();
        good = new Goods(GoodsColor.YELLOW);
        credit += 3 * cargoHoldContainsGood(good).size();
        good = new Goods(GoodsColor.GREEN);
        credit += 2 * cargoHoldContainsGood(good).size();
        good = new Goods(GoodsColor.BLUE);
        credit += cargoHoldContainsGood(good).size();
        return credit;
    }

    /**
     * Checks whether the cargo hold is empty (i.e., no goods are present in any cargo hold).
     *
     * @return true if all cargo holds are empty, false if any cargo hold contains goods
     */
    public boolean isCargoEmpty() {
        for (Coordinates coordinates : cargoHoldCoordinates) {
            if (!tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().isEmpty())
                return false;
        }
        return true;
    }



    /**
     * Returns all goods currently stored in the cargo hold.
     *
     * @return a list of all goods in the cargo hold
     */
    public ArrayList<Goods> getAllGoods(){
        ArrayList<Goods> goods = new ArrayList<>();
        int i;
        Goods good = new Goods(GoodsColor.RED);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
            goods.add(good);
        }
        good = new Goods(GoodsColor.YELLOW);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
            goods.add(good);

        }
        good = new Goods(GoodsColor.GREEN);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
            goods.add(good);
        }
        good = new Goods(GoodsColor.BLUE);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
            goods.add(good);
        }
        return goods;
    }


    /**
     * Returns the goods stored in a specific cargo hold at the given coordinates.
     *
     * @param coordinatesToFind the coordinates of the cargo hold to search
     * @return a list of goods stored at the specified coordinates
     */
    public ArrayList<Goods> getSingleCargoGoods(Coordinates coordinatesToFind){
        ArrayList<Goods> goods = new ArrayList<>();
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.RED))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.RED));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.YELLOW))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.YELLOW));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.GREEN))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.GREEN));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.BLUE))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.BLUE));
            }
        }
        return goods;
    }

    /**
     * Adds a battery to the cargo hold at the specified coordinates.
     *
     * @param coordinates the coordinates where the battery should be added
     */
    public void addBattery(Coordinates coordinates){
        getTile(coordinates).addBattery();
    }

    /**
     * Removes crew members from the specified coordinates in the case of an epidemic.
     *
     * @param coordinatesEpidemic the list of coordinates where the crew should be removed
     */
    public void removeCrew(ArrayList<Coordinates> coordinatesEpidemic){
        for (Coordinates coordinates : coordinatesEpidemic) {
            getTile(coordinates).removeCrew();
        }
    }

    /**
     * Destroys the tiles at the specified coordinates.
     *
     * @param coordinatesDestroyed the list of coordinates to destroy
     */
    public void destroy(ArrayList<Coordinates> coordinatesDestroyed){
        for (Coordinates coordinates : coordinatesDestroyed) {
            if(getTile(coordinates)!=null) {
                getTile(coordinates).destroy();
                tilesTable[coordinates.getX()][coordinates.getY()]=Optional.empty();
            }
        }
    }

    /**
     * Returns a string representation of the shipboard, including the tiles and current credits.
     *
     * @return the string representation of the shipboard
     */
    public String toString(){
        StringBuilder s = new StringBuilder("Shipboard: ");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable()) {
                    s.append(tilesTable[i][j].get().toString());
                    s.append("\n");
                }
            }
            s.append("\n-\n");
        }
        s.append("\ncredit:"+credit + "\n");
        return s.toString();
    }

    /**
     * Checks whether early landing is possible based on the number of human crew members on the ship.
     *
     * @return true if early landing is possible (i.e., no human crew left), false otherwise
     */
    public boolean checkEarlyLanding() {
        return numHumanCrew == 0;
    }

    /**
     * Sets the ship to a new configuration by removing tiles that are not part of the given set.
     *
     * @param set the set of coordinates representing the new configuration of the ship
     */
    public void SetNewShip(Set<Coordinates> set) {
        Coordinates c = new Coordinates(0, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable()) {
                    c.set(i, j);
                    if (!set.contains(c)) {
                        tilesTable[i][j].get().destroy();
                        tilesTable[i][j] = Optional.empty();
                    }
                }
            }
        }
    }



    /**
     * Simulates an epidemic affecting the crew members aboard the ship. The epidemic spreads based on adjacency
     * between crew cabins that are connected by non-smooth links (e.g., walls).
     * If two crew cabins are adjacent without a smooth connection, the crew in the second cabin is infected.
     *
     * @return a list of the modified tiles (i.e., the infected crew cabins) after the epidemic spreads
     */
    public ArrayList<Tile> epidemic(){
        HashSet<Coordinates> InfectedCabin = new HashSet<>();
        for(Coordinates coordinates : crewCoordinates){
            for(Coordinates coordinates2 : crewCoordinates){
                if ((coordinates.getX() == coordinates2.getX() && coordinates.getY() - 1 == coordinates2.getY() && !this.getTile(coordinates).getWest().getConnectorsType().equals(Connectors.SMOOTH))
                        || (coordinates.getX() == coordinates2.getX() && coordinates.getY() + 1 == coordinates2.getY() && !this.getTile(coordinates).getEast().getConnectorsType().equals(Connectors.SMOOTH))
                        || (coordinates.getX() - 1 == coordinates2.getX() && coordinates.getY() == coordinates2.getY() && !this.getTile(coordinates).getNorth().getConnectorsType().equals(Connectors.SMOOTH))
                        || coordinates.getX() + 1 == coordinates2.getX() && coordinates.getY() == coordinates2.getY() && !this.getTile(coordinates).getSouth().getConnectorsType().equals(Connectors.SMOOTH)
                ) {
                    //(Math.abs(coordinates.getX() - coordinates2.getX())==1 ^ Math.abs(coordinates.getY() - coordinates2.getY())==1)&& !coordinates.equals(coordinates2)
                    InfectedCabin.add(coordinates2);
                }
            }
        }
        for(Coordinates coordinates : InfectedCabin){
            tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
        }
        ArrayList<Tile> modifiedCabin = new ArrayList<>();
        for (Coordinates coordinates: InfectedCabin) {
            modifiedCabin.add(this.getTile(coordinates));
        }
        return modifiedCabin;
    }

    /**
     * Retrieves the list of coordinates where batteries are located on the ship.
     *
     * @return a list of coordinates where batteries are placed
     */
    public ArrayList<Coordinates> getBatteryCoordinates() {
        return batteryCoordinates;
    }

    /**
     * Retrieves the list of coordinates where the cargo hold tiles are located on the ship.
     *
     * @return a list of coordinates where cargo hold tiles are positioned
     */
    public ArrayList<Coordinates> getCargoHoldCoordinates() {
        return cargoHoldCoordinates;
    }
    /**
     * Retrieves the list of coordinates where the crew cabins are located on the ship.
     *
     * @return a list of coordinates where crew cabins are placed
     */
    public ArrayList<Coordinates> getCabinsCoordinates() {
        return crewCoordinates;
    }
    /**
     * Retrieves the current ship's tiles table, representing the grid of tiles on the ship.
     *
     * @return a 2D array of optional tiles, where each entry represents a tile on the ship's grid
     */
    public Optional<Tile>[][] getTilesTable() {
        return tilesTable;
    }

    /**
     * Checks if the ship's tiles are connected properly by verifying the links between them.
     *
     * @param start the starting coordinates of the check
     * @param set the set of coordinates to track during the connectivity check
     * @return the set of connected coordinates
     */
    public Set<Coordinates> connectedSet(Coordinates start, Set<Coordinates> set) {
        int x = start.getX();
        int y = start.getY();
        set.add(new Coordinates(x, y));
        //south
        if (x<4 && tilesTable[x][y].get().getSouth().getConnectorsType() != Connectors.SMOOTH && tilesTable[x + 1][y].isPresent() && tilesTable[x + 1][y].get().fillable() && !set.contains(tilesTable[x + 1][y].get().getCoordinates())) {
            connectedSet(new Coordinates(x + 1, y), set);
        }
        //east
        if (y<6 && tilesTable[x][y].get().getEast().getConnectorsType() != Connectors.SMOOTH && tilesTable[x][y + 1].isPresent() && tilesTable[x][y + 1].get().fillable() && !set.contains(tilesTable[x][y + 1].get().getCoordinates())) {
            connectedSet(new Coordinates(x, y + 1), set);
        }
        //north
        if (x>0 && tilesTable[x][y].get().getNorth().getConnectorsType() != Connectors.SMOOTH && tilesTable[x - 1][y].isPresent() && tilesTable[x - 1][y].get().fillable() && !set.contains(tilesTable[x - 1][y].get().getCoordinates())) {
            connectedSet(new Coordinates(x - 1, y), set);
        }
        //west
        if (y>0 && tilesTable[x][y].get().getWest().getConnectorsType() != Connectors.SMOOTH && tilesTable[x][y - 1].isPresent() && tilesTable[x][y - 1].get().fillable() && !set.contains(tilesTable[x][y - 1].get().getCoordinates())) {
            connectedSet(new Coordinates(x, y - 1), set);
        }
        return set;
    }

    /**
     * Verifies that the shipboard is correctly constructed, ensuring all tiles are connected.
     *
     * @return true if the ship is correctly constructed, false otherwise
     */
    public boolean verifyCorrectness() {
        Set<Coordinates> set = new HashSet<Coordinates>();
        set=this.connectedSet( new Coordinates(2,3), set);
        // da controllare che tutte le caselle non vuote siano nel set per la correttezza (no caso delle due navi separate)
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable() )
                    if(!tilesTable[i][j].get().isCorrect() ||! set.contains(tilesTable[i][j].get().getCoordinates()) ) return false;
            }
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent())
                    tilesTable[i][j].get().getStat();
            }
        int i=bookedTiles.size();
        for(int j=0;j<i;j++) {
            addPenalty();
        }
        return true;
    }

    /**
     * Resets the ship's status and updates the status of each tile on the ship.
     * This method is responsible for ensuring that the ship's statistics are reset before updating the status of each tile.
     * It checks every tile on the ship's grid, and if the tile is fillable, it updates the tile's status.
     */
    public void setGetStat(){
        resetStat();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable())
                    tilesTable[i][j].get().getStat();
            }



    }

    /**
     * Resets the statistics of the ship, clearing all values related to tiles, crew, and components.
     */
    public void resetStat() {
        numBatteries=0;
        singleCannonPower=0;
        DoubleCannon= new ArrayList<>();
        cargoHoldCoordinates=new ArrayList<>();
        numSingleEngine=0;
        DoubleEngine = new ArrayList<>();
        shields = new ArrayList<>();
        batteryCoordinates = new ArrayList<>();
        crewCoordinates = new ArrayList<>();
    }

    /**
     * Updates the status of each cabin on the ship.
     * This method specifically updates the status of the crew cabins.
     * It iterates through all the cabin coordinates and updates the status of the tiles at those positions.
     */
    public void setCabinStat(){
        for(Coordinates coordinates: getCabinsCoordinates())
        {
            tilesTable[coordinates.getX()][coordinates.getY()].get().getStat();
        }
    }

    /**
     * Converts a ShipBoard object into a LightShipBoard object.
     * This method initializes the tiles table of the LightShipBoard based on the provided ShipBoard,
     * and sets the shipboard reference for each tile in the tiles table.
     *
     * @param shipBoard the ShipBoard object to convert into a LightShipBoard
     */
    public  void convertToLightShipBoard(ShipBoard shipBoard) {
        this.tilesTable =  shipBoard.getTilesTable();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                Optional<Tile> tile = tilesTable[i][j];
                tile.ifPresent(value -> value.setShipBoard(this));
            }
        }
    }

    /**
     * Sets the completion status of the ship's construction.
     * This method marks the ship as completed based on the provided argument.
     *
     * @param completed the completion status to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Sets the penalty for the shipboard based on the given value.
     * This method updates the penalty count for the ship.
     *
     * @param penalty the penalty value to set
     */
    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    /**
     * Loads the tiles into the ship's grid from the provided list of tiles.
     * This method ensures the tiles are correctly placed on the ship, checking that the number of tiles is valid,
     * and handling the deserialization of the ship's state.
     *
     * @param tiles the list of tiles to load into the ship
     */
    public void loadFromTiles(ArrayList<Tile> tiles) {
        if (tiles.size() < 37) {
            System.out.println("Error deserializing ship");
        }
        simpleInitialize();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                Tile currentTile = tiles.removeFirst();
                if (currentTile == null) {
                    this.positionNullTile(Optional.empty(), new Coordinates(i, j));
                }
                else {
                    this.positionTileWithoutAdjacencyCheck(Optional.of(currentTile), new Coordinates(i, j));
                }
            }
        }
        if (tiles.size() < 2) {
            System.out.println("Error deserializing ship: no booked tiles");
        }
        for (int i = 0; i < 2; i++) {
            Tile currentTile = tiles.removeFirst();
            if (currentTile != null) {
                this.bookedTiles.add(currentTile);
            }
        }
        this.verifyCorrectness();
    }

    /**
     * Initializes the ship's tile grid with empty tiles.
     * This method prepares the tiles table for use by setting all entries in the grid to empty optionals.
     */
    public void simpleInitialize() {
        this.tilesTable = new Optional[5][7];
        for (int i = 0; i < tilesTable.length; i++) {
            for (int j = 0; j < tilesTable[i].length; j++) {
                tilesTable[i][j] = Optional.empty();  // Initializes with empty optionals
            }
        }
    }

    public int getPenalty() {
        return penalty;
    }

    public void swapTile(Optional<Tile> tile, Coordinates coordinates) {
        tilesTable[coordinates.getX()][coordinates.getY()] = tile;
        tile.get().setShipBoard(this);
        tile.get().setCoordinates(coordinates);
    }
}