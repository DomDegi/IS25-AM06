package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Abstract class representing a tile component in the ship grid.
 * All ship components such as cabins, engines, cannons, etc. extend this base class.
 * Each tile has directional connectors, rotation, coordinates, and a unique key.
 */
public abstract class Tile implements Serializable, Cloneable {

    /** Connector on the north side */
    protected Link north;
    /** Connector on the east side */
    protected Link east;
    /** Connector on the south side */
    protected Link south;
    /** Connector on the west side */
    protected Link west;

    /** Rotation of the tile in 90-degree increments */
    protected int rotation;

    /** Path to the tile's image resource */
    protected String imagePath = null;

    /** Position of the tile on the ship grid */
    protected Coordinates coordinates;

    /** Reference to the ship board this tile belongs to */
    protected ShipBoardInterface shipBoard;

    /** Unique identifier for the tile */
    protected int key;

    /** True if the tile has been booked but not placed yet */
    protected boolean booked;

    /**
     * Full constructor with all parameters.
     */
    public Tile(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.rotation = rotation;
        this.coordinates = new Coordinates(0, 0);
        this.imagePath = imagePath;
        this.key = key;
        this.booked = false;
    }

    /**
     * Constructor used for testing with default image and rotation.
     */
    public Tile(Link north, Link east, Link south, Link west) {
        this(north, east, south, west, null, 0, 0);
    }

    /**
     * Default constructor used for deserialization.
     */
    public Tile() {
        this.key = -1;
    }

    // === Getters and Setters with documentation ===

    /** @return Unique ID of the tile */
    public int getKey() {
        return key;
    }

    /** @return North link of the tile */
    public Link getNorth() {
        return north;
    }

    /** @return East link of the tile */
    public Link getEast() {
        return east;
    }

    /** @return South link of the tile */
    public Link getSouth() {
        return south;
    }

    /** @return West link of the tile */
    public Link getWest() {
        return west;
    }

    /** @return File path of the tile image */
    public String getImagePath() {
        return imagePath;
    }

    /** @return Rotation value (0 to 3) of the tile */
    public int getRotation() {
        return rotation;
    }

    /** @param rotation Set rotation value */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /** @param coordinates Coordinates to place the tile at */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /** @return Coordinates of the tile */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /** @param shipBoard Set the ship board this tile belongs to */
    public void setShipBoard(ShipBoardInterface shipBoard) {
        this.shipBoard = shipBoard;
        if(shipBoard!=null)
            this.shipBoard = shipBoard;

    }

    /** @return True if the tile is booked */
    public boolean isBooked() {
        return booked;
    }

    /** @param booked Set whether the tile is booked */
    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    // === Core functionality ===

    /**
     * Rotates the tile clockwise, adjusting the links and direction accordingly.
     */
    public void rotate() {
        rotation++;
        Link tmp = north;
        north = west;
        Link tmp2 = east;
        east = tmp;
        west = south;
        south = tmp2;
    }

    /**
     * Checks whether the tile is correctly connected to its neighbors.
     * @return true if connections are valid, false otherwise
     */
    public boolean isCorrect() {
        Optional<Tile> other;
        Optional<Tile>[][] tileTable = shipBoard.getTilesTable();

        if (this.coordinates.getY() != 6) {
            other = tileTable[this.coordinates.getX()][this.coordinates.getY() + 1];
            if (other.isPresent() && other.get().fillable() && !this.east.isConnected(other.get().getWest())) {
                return false;
            }
        }

        if (this.coordinates.getX() != 4) {
            other = tileTable[this.coordinates.getX() + 1][this.coordinates.getY()];
            if (other.isPresent() && !(other.get() instanceof VoidTile) && !south.isConnected(other.get().getNorth())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Called when the tile is destroyed. Triggers a penalty.
     */
    public void destroy() {
        shipBoard.addPenalty();
    }

    /**
     * Creates a shallow clone of the tile, excluding the ShipBoard reference.
     * @return a copy of this tile
     */
    public Tile send() {
        try {
            Tile cloned = (Tile) super.clone();
            cloned.setRotation(rotation);
            cloned.setShipBoard(null);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if this tile can be legally positioned at the given coordinates.
     * @param coord position on the grid
     * @param shipBoard reference board
     * @return true if it can be placed, false otherwise
     */
    public boolean canPosition(Coordinates coord, ShipBoardInterface shipBoard) {
        return (north.getConnectorsType() != Connectors.SMOOTH && coord.getX() != 0 && shipBoard.getTile(coord.getX() - 1, coord.getY()) != null && shipBoard.getTile(coord.getX() - 1, coord.getY()).getSouth().getConnectorsType() != Connectors.SMOOTH) ||
                (south.getConnectorsType() != Connectors.SMOOTH && coord.getX() != 4 && shipBoard.getTile(coord.getX() + 1, coord.getY()) != null && shipBoard.getTile(coord.getX() + 1, coord.getY()).getNorth().getConnectorsType() != Connectors.SMOOTH) ||
                (east.getConnectorsType() != Connectors.SMOOTH && coord.getY() != 6 && shipBoard.getTile(coord.getX(), coord.getY() + 1) != null && shipBoard.getTile(coord.getX(), coord.getY() + 1).getWest().getConnectorsType() != Connectors.SMOOTH) ||
                (west.getConnectorsType() != Connectors.SMOOTH && coord.getY() != 0 && shipBoard.getTile(coord.getX(), coord.getY() - 1) != null && shipBoard.getTile(coord.getX(), coord.getY() - 1).getEast().getConnectorsType() != Connectors.SMOOTH);
    }

    // === Overridable methods used by subclasses ===

    public void setCrewType(CrewType crewType) {}
    public boolean consumeBattery() {return false;}
    public boolean removeCrew() { return false; }
    public void removeGood(Goods good) {}
    public Coverage getCoveredArea() { return Coverage.NONE; }
    public int addGood(Goods good) { return 0; }
    public void addBattery() {}
    public ArrayList<Goods> getCargo() { return null; }
    public int getNumBatteries() { return 0; }
    public void getStat() {}
    public Direction getDirection() { return null; }
    public int getStrength() { return 0; }
    public int getEngineStrength() { return 0; }
    public boolean fillable() { return true; }
    public CrewType getAlienLifeSupportSystemColor() { return CrewType.NotSupportSystem; }
    public ArrayList<Coordinates> adjacentLifeSupport() { return new ArrayList<>(); }
    public CrewType getCrewType() { return CrewType.NotAcabin; }
    public AlienOptions getAlienability() { return AlienOptions.NO; }
    public void checkAlienability() {}
    public int getCrew() {return 0;}

    // === ASCII display strings ===

    public String toString1() { return "   "; }
    public String toString2() { return "   "; }
    public String toString3() { return "   "; }

    // === Serialization helper ===

    public String toStringData() {
        return "   ";
    }

    /**
     * Loads the tile data from a string array of attributes.
     * Used during game deserialization.
     * @param attributes string attributes from save data
     */
    public void tileLoader(String[] attributes) {
        int k = 0;
        key = Integer.parseInt(attributes[1]);
        if (attributes[2].equals("N")) {
            this.imagePath = null;
        }
        else {
            k++;
            this.imagePath = attributes[2] + " " + attributes[3];
        }
        this.rotation = Integer.parseInt(attributes[3 + k]);
        north = new Link(Connectors.fromValue(Integer.parseInt(attributes[4 + k])));
        east = new Link(Connectors.fromValue(Integer.parseInt(attributes[5 + k])));
        south = new Link(Connectors.fromValue(Integer.parseInt(attributes[6 + k])));
        west = new Link(Connectors.fromValue(Integer.parseInt(attributes[7 + k])));
    }
}


