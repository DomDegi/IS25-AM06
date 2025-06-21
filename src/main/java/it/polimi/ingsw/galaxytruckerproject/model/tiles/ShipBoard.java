package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * Represents a ship board in the Galaxy Trucker game.
 * Manages the layout, components, crew, and cargo of the player's ship,
 * including construction, destruction, penalties, and verification logic.
 */
public class ShipBoard implements ShipBoardInterface, Serializable {
    /**
     * The player associated with this ship.
     */
    protected final Player player;

    /**
     * 2D array representing the ship layout with optional tiles.
     */
    private Optional<Tile>[][] tilesTable;

    /**
     * Number of exposed connectors on the ship.
     */
    private int numExposedConnectors;

    /**
     * Number of penalty points accumulated.
     */
    private int penalty;

    /**
     * List of booked tiles awaiting placement.
     */
    private final ArrayList<Tile> bookedTiles;

    /**
     * Number of total batteries available.
     */
    private int numBatteries;

    /**
     * Total power from single cannons.
     */
    private float singleCannonPower;

    /**
     * Coordinates of double cannons.
     */
    private final ArrayList<Coordinates> DoubleCannon;

    /**
     * Indicates if this is the first tile placed.
     */
    private boolean first = true;

    /**
     * Number of single engines on the ship.
     */
    private int numSingleEngine;

    /**
     * Coordinates of broken double engines.
     */
    private final ArrayList<Coordinates> DoubleEngine;

    /**
     * List of shield coverage areas.
     */
    private final ArrayList<Coverage> shields;

    /**
     * Coordinates of battery tiles.
     */
    private final ArrayList<Coordinates> batteryCoordinates;

    /**
     * Coordinates of crew cabin tiles.
     */
    private final ArrayList<Coordinates> crewCoordinates;

    /**
     * Coordinates of cargo hold tiles.
     */
    private final ArrayList<Coordinates> cargoHoldCoordinates;

    /**
     * Number of brown aliens on board.
     */
    private int numBrownAliens;

    /**
     * Number of purple aliens on board.
     */
    private int numPurpleAliens;

    /**
     * Number of human crew members.
     */
    private int numHumanCrew;

    /**
     * Indicates whether the ship board has been finalized.
     */
    private boolean completed = false;



    /**
     * Constructs a new ShipBoard for the given player.
     * Initializes internal structures and sets up default values.
     *
     * @param player the player who owns this ship.
     */
    public ShipBoard(Player player) {
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
    }

    // ——— PUBLIC METHODS DOCUMENTED ———

    /**
     * Sets the ship's construction completion status.
     *
     * @param completed true if construction is complete.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Checks if the ship construction is completed.
     *
     * @return true if completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Returns a formatted string representation of the ship board.
     *
     * @return string with tiles layout.
     */
    public String toString() {
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
        return s.toString();
    }

    /**
     * Initializes the ship for a test flight layout.
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
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),red, 0,0);
        else
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web157.jpg",0, 0);
        positionTile(Optional.of(tile), new Coordinates(2, 3));
        this.tilesTable = tilesTable;
    }

    /**
     * Initializes the ship for a Level 2 layout.
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
        if(player.getPlayerColor()== PlayersColor.BLUE) {
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), blue, 0, 0);
        }
        else if(player.getPlayerColor()== PlayersColor.YELLOW)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),yellow,0, 0);
        else if(player.getPlayerColor()== PlayersColor.GREEN)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),green,0, 0);
        else if(player.getPlayerColor()== PlayersColor.RED)
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),red,0, 0);
        else
            tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web157.jpg", 0,0);
        positionTile(Optional.of(tile), new Coordinates(2, 3));
    }

    /**
     * Initializes an empty grid without any predefined void tiles.
     */
    public void simpleInitialize() {
        this.tilesTable = new Optional[5][7];
        for (int i = 0; i < tilesTable.length; i++) {
            for (int j = 0; j < tilesTable[i].length; j++) {
                tilesTable[i][j] = Optional.empty();  // Inizializza con Optional vuoti
            }
        }
    }


    /**
     * Destroys the tile at the given coordinates.
     *
     * @param coordinates the tile to destroy.
     */
    public void destroyForCorrection(Coordinates coordinates) {
        tilesTable[coordinates.x][coordinates.y].get().destroy();
        tilesTable[coordinates.getX()][coordinates.getY()] = Optional.empty();
    }


    /**
     * Destroys a tile and returns the resulting disconnected components.
     *
     * @param coordinates the location of the tile to destroy.
     * @return list of sets of disconnected tile coordinates.
     */
    public ArrayList<Set<Coordinates>> destroyTile(Coordinates coordinates) {
        if (coordinates.x == 0 && coordinates.y == 0) {
            System.out.println("Can't destroy ");
        }
        tilesTable[coordinates.x][coordinates.y].get().destroy();
        tilesTable[coordinates.getX()][coordinates.getY()] = Optional.empty();
        Set<Coordinates> set1 = null;
        Set<Coordinates> set2 = null;
        Set<Coordinates> set3 = null;
        Set<Coordinates> set4 = null;
        //southSet
        int x = coordinates.getX();
        int y = coordinates.getY();

        //south
        if (x< 4 && tilesTable[x + 1][y].isPresent() && tilesTable[x + 1][y].get().fillable()) {
            set1 = (brokenGraph(new Coordinates(x + 1, y)));
        }
        //east
        if (y< 6 && tilesTable[x][y + 1].isPresent() && tilesTable[x][y + 1].get().fillable() && (set1 == null || (tilesTable[x][y + 1].isPresent() && !set1.contains(tilesTable[x][y + 1].get().getCoordinates())))) {
            set2 = brokenGraph(new Coordinates(x, y + 1));
        }
        //north
        if (x> 0 && tilesTable[x - 1][y].isPresent() && tilesTable[x - 1][y].get().fillable() && (set1 == null || !set1.contains(tilesTable[x - 1][y].get().getCoordinates())) && (set2 == null || !set2.contains(tilesTable[x - 1][y].get().getCoordinates()))) {
            set3 = brokenGraph(new Coordinates(x - 1, y));
        }
        //west
        if (y > 0 && tilesTable[x][y - 1].isPresent() && tilesTable[x][y - 1].get().fillable() && (set1 == null || !set1.contains(tilesTable[x][y - 1].get().getCoordinates())) && (set2 == null || !set2.contains(tilesTable[x][y - 1].get().getCoordinates())) && (set3 == null || !set3.contains(tilesTable[x][y - 1].get().getCoordinates()))) {
            set4 = brokenGraph(new Coordinates(x, y - 1));
        }
        ArrayList<Set<Coordinates>> array = new ArrayList<>();
        if (set1 != null) {
            array.add(set1);
        }
        if (set2 != null) {
            array.add(set2);
        }
        if (set3 != null) {
            array.add(set3);
        }
        if (set4 != null) {
            array.add(set4);
        }
        return array;
    }

    /**
     * Keeps only tiles in the provided set and removes all others.
     *
     * @param set the set of valid coordinates.
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
     * Returns all tiles connected to the starting tile by non-smooth connectors.
     *
     * @param start the start coordinate.
     * @return a set of connected tile coordinates.
     */
    public Set<Coordinates> brokenGraph(Coordinates start) {
        Set<Coordinates> set = new HashSet<Coordinates>();
        return connectedSet(start, set);
    }

    /**
     * Adds tile to the board if position is empty.
     *
     * @param tile optional tile to place.
     * @param coordinates location to place the tile.
     * @return true if placed successfully.
     */
    public boolean positionTile(Optional<Tile> tile, Coordinates coordinates) {
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
     * Explicitly sets a null tile at given coordinates.
     *
     * @param tile empty tile.
     * @param coordinates location.
     */
    public void positionNullTile(Optional<Tile> tile, Coordinates coordinates) {
        if (tilesTable[coordinates.getX()][coordinates.getY()].isEmpty() && tile.isEmpty()) {
            tilesTable[coordinates.getX()][coordinates.getY()] = Optional.empty();
        }
    }

    /**
     * Returns the tile at the specified (x, y) position.
     *
     * @param x the row index.
     * @param y the column index.
     * @return the tile or null if absent.
     */
    public Tile getTile(int x, int y) {
        if(tilesTable[x][y].isPresent())
            return tilesTable[x][y].get();
        return null;
    }

    /**
     * Returns the tile at given coordinates.
     *
     * @param coordinates tile position.
     * @return the tile or null if absent.
     */
    public Tile getTile(Coordinates coordinates){
        if (tilesTable[coordinates.getX()][coordinates.getY()].isPresent())
            return tilesTable[coordinates.getX()][coordinates.getY()].get();
        return null;
    }

    /**
     * Verifies if all tiles are correctly connected and valid.
     *
     * @return true if ship is valid.
     */
    public boolean verifyCorrectness() {
        if (completed) {
            return true;
        }
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
        countExposedConnectors();
        return true;
    }


    /**
     * Recursively explores and collects all tiles connected to the starting tile via non-smooth connectors.
     * This method performs a depth-first search from the given coordinate, traversing in all four directions
     * (north, south, east, west), and adds each connected and valid tile to the provided set.
     *
     * @param start the coordinate from which the search starts.
     * @param set the set collecting all reachable and connected coordinates.
     * @return the set of coordinates forming a connected component.
     */
    public Set<Coordinates> connectedSet(Coordinates start, Set<Coordinates> set) {
        int x = start.x;
        int y = start.y;

        // Add the current tile to the set to mark it as visited
        set.add(new Coordinates(x, y));

        // Check south connection
        if (x < 4 && // ensure within bounds
                tilesTable[x][y].get().south.getConnectorsType() != Connectors.SMOOTH && // connector is not smooth
                tilesTable[x + 1][y].isPresent() && // neighbor exists
                tilesTable[x + 1][y].get().fillable() && // neighbor is fillable
                !set.contains(tilesTable[x + 1][y].get().getCoordinates())) { // not visited
            connectedSet(new Coordinates(x + 1, y), set);
        }

        // Check east connection
        if (y < 6 &&
                tilesTable[x][y].get().east.getConnectorsType() != Connectors.SMOOTH &&
                tilesTable[x][y + 1].isPresent() &&
                tilesTable[x][y + 1].get().fillable() &&
                !set.contains(tilesTable[x][y + 1].get().getCoordinates())) {
            connectedSet(new Coordinates(x, y + 1), set);
        }

        // Check north connection
        if (x > 0 &&
                tilesTable[x][y].get().north.getConnectorsType() != Connectors.SMOOTH &&
                tilesTable[x - 1][y].isPresent() &&
                tilesTable[x - 1][y].get().fillable() &&
                !set.contains(tilesTable[x - 1][y].get().getCoordinates())) {
            connectedSet(new Coordinates(x - 1, y), set);
        }

        // Check west connection
        if (y > 0 &&
                tilesTable[x][y].get().west.getConnectorsType() != Connectors.SMOOTH &&
                tilesTable[x][y - 1].isPresent() &&
                tilesTable[x][y - 1].get().fillable() &&
                !set.contains(tilesTable[x][y - 1].get().getCoordinates())) {
            connectedSet(new Coordinates(x, y - 1), set);
        }

        return set;
    }


    /**
     * Returns true if no human crew is left, indicating early landing.
     *
     * @return true if ship has no crew.
     */
    public boolean checkEarlyLanding() {
        return numHumanCrew == 0;
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
     * Updates tile at given coordinates with a new tile.
     *
     * @param coordinates location of tile.
     * @param newTile tile to replace with.
     */
    public void updateTile(Coordinates coordinates, Tile newTile){
        tilesTable[coordinates.getX()][coordinates.getY()] = Optional.of(newTile);
        this.getTile(coordinates).setShipBoard(this);
    }

    /**
     * Resets the number of all crew types to zero.
     */
    public void setCrewNumberToZero() {
        this.numHumanCrew=0;
        this.numBrownAliens=0;
        this.numPurpleAliens=0;
        return;
    }

    /**
     * Triggers the epidemic logic that spreads among connected cabins.
     *
     * @return list of modified (infected) tiles.
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
                    InfectedCabin.add(coordinates2);
                }
            }
        }
        ArrayList<Tile> modifiedCabin = new ArrayList<>();
        for(Coordinates coordinates : InfectedCabin){
            tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
            modifiedCabin.add(this.getTile(coordinates));

        }
        return modifiedCabin;
    }

    /**
     * Removes one crew member from the specified tile if it's a cabin.
     *
     * @param coordinates location of the tile.
     * @return true if crew was removed.
     */
    public boolean chooseCrewToRemove(Coordinates coordinates) {
        if (crewCoordinates.contains(coordinates)) {
            tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
            return true;
        } else
            System.out.println("THIS TILE IS NOT A CABIN");
        return false;
    }

    /**
     * Attempts to use a battery from the given coordinates.
     *
     * @param coordinates location of battery tile.
     * @return true if battery was consumed.
     */
    public boolean chooseBatteryUse(Coordinates coordinates){
        if(batteryCoordinates.contains(coordinates)){
            tilesTable[coordinates.getX()][coordinates.getY()].get().consumeBattery();
            return true;
        }
        else
            return false;
    }

    /**
     * Activates a shield using a battery and returns the shield direction.
     *
     * @param shieldCoordinates location of the shield tile.
     * @param batteryCoordinates location of the battery tile.
     * @return the {@link Coverage} applied.
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
     * Adds goods to a cargo tile.
     *
     * @param goods the item to add.
     * @param coordinates location of cargo tile.
     * @return status code (1 success, 0 full, -1 wrong type).
     */
    public int gainGoods(Goods goods, Coordinates coordinates){
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
     * Removes a specified good from a cargo tile.
     *
     * @param good the good to remove.
     * @param coordinates tile coordinates.
     */
    public void removeGood(Goods good, Coordinates coordinates){
        tilesTable[coordinates.getX()][coordinates.getY()].get().removeGood(good);
    }

    /**
     * Checks whether all cargo tiles are empty.
     *
     * @return true if no goods are stored.
     */
    public boolean isCargoEmpty() {
        for (Coordinates coordinates : cargoHoldCoordinates) {
            if (!tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().isEmpty())
                return false;
        }
        return true;
    }


    /**
     * Converts all stored goods to credits.
     *
     * @return total credit value.
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
     * Returns a list of all goods currently stored.
     *
     * @return list of goods.
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
     * Returns the goods stored in a specific cargo tile.
     *
     * @param coordinatesToFind the cargo tile location.
     * @return list of goods in that tile.
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
     * Checks whether a given cargo tile contains a specific good.
     *
     * @param color the color of the good.
     * @param cargo the cargo tile coordinates.
     * @return true if the tile contains the good.
     */
    public boolean singleCargoContainsGood (GoodsColor color, Coordinates cargo) {
        ArrayList<Goods> cargosGoods = getSingleCargoGoods(cargo);
        for (Goods goods : cargosGoods) {
            if (goods.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * It returns the coordinate of every cargoHold that contains a type of good
     * @param goodColor the type of good
     * @return the coordinates of every good of goodColor
     */
    public ArrayList<Coordinates> cargoHoldContainsGood(Goods goodColor){
        ArrayList<Coordinates> cargoHoldContainsGood = new ArrayList<>();
        for(Coordinates coordinates : cargoHoldCoordinates){
            for(int i=0; i<tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().size(); i++){
                if(tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().get(i).getColor()==goodColor.getColor()){
                    cargoHoldContainsGood.add(coordinates);
                }
            }
        }
        return cargoHoldContainsGood;
    }


    /**
     * Adds a battery to the given battery tile.
     *
     * @param coordinates location of the battery tile.
     */
    public void addBattery(Coordinates coordinates){
        getTile(coordinates).addBattery();
    }

    /**
     * Adds a booked tile to the booking list if under limit.
     *
     * @param tile the tile to book.
     * @return true if booking succeeded.
     */
    public boolean addBookedTile(Tile tile) {
        if (bookedTiles.size() == 2) {
            return false;
        }
        bookedTiles.add(tile);
        tile.setBooked(true);
        return true;
    }

    /**
     * Returns the list of currently booked tiles.
     *
     * @return booked tile list.
     */
    public ArrayList<Tile> getBookedTiles() {
        return bookedTiles;
    }

    /**
     * Removes a booked tile by index.
     *
     * @param num index to remove.
     * @return the removed tile or null.
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
     * Sets the number of human crew members.
     *
     * @param numHumanCrew number of crew.
     */
    public void setHumanCrew(int numHumanCrew) {
        this.numHumanCrew = numHumanCrew;
    }

    /**
     * Sets the current penalty value.
     *
     * @param penalty value to set.
     */
    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    // ============ Various getter methods ============ //

    public ArrayList<Coordinates> getBatteryCoordinates() {
        return batteryCoordinates;
    }

    public ArrayList<Coverage> getCoverageShields() {
        return shields;
    }

    public ArrayList<Coordinates> getCargoHoldCoordinates() {
        return cargoHoldCoordinates;
    }

    public ArrayList<Coordinates> getCabinsCoordinates() {
        return crewCoordinates;
    }

    public Optional<Tile>[][] getTilesTable() {
        return tilesTable;
    }

    public int getPenalty() {
        return penalty;
    }

    public Player getPlayer() {
        return player;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public float getSingleCannonPower() {
        return singleCannonPower;
    }

    public ArrayList<Coordinates> getDoubleCannon() {
        return DoubleCannon;
    }

    public int getNumSingleEngine() {
        return numSingleEngine;
    }

    public ArrayList<Coordinates> getDoubleEngine() {
        return DoubleEngine;
    }

    public int getNumBrownAliens() {
        return numBrownAliens;
    }

    public int getNumPurpleAliens() {
        return numPurpleAliens;
    }

    public int getNumHumanCrew() { return  numHumanCrew; }

    public int getNumExposedConnectors() {
        return numExposedConnectors;
    }

    public int getDoubleCannonPower(Coordinates coordinates) {
        if(DoubleCannon.contains(coordinates)){
            return tilesTable[coordinates.getX()][coordinates.getY()].get().getStrength();

        }
        return 0;
    }

    // ========== Methods needed for the initialization of the ship attributes after verification ========== //

    /**
     * Increases the total lost power due to destroyed single cannons.
     *
     * @param num the amount of power to add to the broken single cannon tally.
     */
    public void addBreakSingleCannonPower(float num) {
        this.singleCannonPower += num;
    }

    /**
     * Updates the count of single engines based on damage or repair.
     *
     * @param ab true if a single engine is broken (increment), false if repaired (decrement).
     */
    public void addBreakSingleEngine(boolean ab) {
        if (ab) numSingleEngine++;
        else numSingleEngine--;
    }

    /**
     * Updates the list of damaged double engines.
     *
     * @param ab true to mark the engine at the given coordinates as broken, false to mark as repaired.
     * @param coordinates the location of the double engine.
     */
    public void addBreakDoubleEngine(boolean ab, Coordinates coordinates) {
        if (ab) {
            DoubleEngine.add(coordinates);
        } else DoubleEngine.remove(coordinates);
    }

    /**
     * Updates the list of damaged double cannons.
     *
     * @param ab true to add the cannon as broken, false to remove it from the list.
     * @param coordinates the location of the double cannon.
     */
    public void addBreakDoubleCannon(boolean ab, Coordinates coordinates) {
        if (ab) {
            DoubleCannon.add(coordinates);
        } else DoubleCannon.remove(coordinates);
    }

    /**
     * Modifies the count of brown aliens on the ship.
     *
     * @param ab true to increment (e.g. death), false to decrement (e.g. revival or recovery).
     */
    public void addBreakBrownAliens(boolean ab) {
        if (ab) numBrownAliens++;
        else numBrownAliens--;
    }

    /**
     * Modifies the count of purple aliens on the ship.
     *
     * @param ab true to increment (e.g. death), false to decrement (e.g. recovery).
     */
    public void addBreakPurpleAliens(boolean ab) {
        if (ab) numPurpleAliens++;
        else numPurpleAliens--;
    }

    /**
     * Adds or subtracts human crew members, typically after damage events.
     *
     * @param num the number of crew members to add (positive) or remove (negative).
     */
    public void addBreakHumanCrew(int num) {
        numHumanCrew += num;
        // Decide which crew to eliminate (game logic not implemented here)
    }

    /**
     * Adds or subtracts available batteries.
     *
     * @param num the number of batteries to add (positive) or consume (negative).
     */
    public void addBreakBatteries(int num) {
        numBatteries += num;
        // if (num < 0) -> logic for selecting which battery to use
    }

    /**
     * Adds one penalty point to the player's ship.
     */
    public void addPenalty() {
        penalty++;
    }

}