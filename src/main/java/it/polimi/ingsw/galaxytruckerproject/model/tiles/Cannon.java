package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Optional;

/**
 * Abstract class representing a generic cannon tile on a ship.
 * A cannon has a direction in which it fires and must not face another tile.
 * Specific cannon types (e.g., single, double) extend this class.
 */
public abstract class Cannon extends Tile {

    /** The direction in which the cannon is facing. */
    protected Direction direction;

    /**
     * Full constructor for a cannon tile.
     *
     * @param north      link on the north side
     * @param east       link on the east side
     * @param south      link on the south side
     * @param west       link on the west side
     * @param imagePath  path to the tile's image
     * @param rotation   rotation of the tile
     * @param key        unique tile identifier
     */
    public Cannon(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
        this.direction = Direction.NORTH;
    }

    /**
     * Constructor for testing purposes. Uses default rotation and key.
     *
     * @param north link on the north side
     * @param east  link on the east side
     * @param south link on the south side
     * @param west  link on the west side
     */
    public Cannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
        this.direction = Direction.NORTH;
    }

    /**
     * Returns a string representation of the cannon,
     * including its direction and tile information.
     *
     * @return string describing the cannon
     */
    @Override
    public String toString() {
        return "direction: " + direction.toString() + " " + super.toString();
    }

    /**
     * Populates shipBoard stats for the cannon.
     * Should be overridden by subclasses.
     */
    public void getStat() {
        // Overridden in the subclasses
    }

    /**
     * Validates that the cannon is not facing another tile.
     * Cannon must face a void space (no tile in the direction).
     *
     * @return true if direction is valid and passes super checks
     */
    public boolean isCorrect() {
        Optional<Tile> other = Optional.empty();
        Optional<Tile>[][] tileTable = shipBoard.getTilesTable();

        if (this.direction == Direction.NORTH && this.coordinates.getX() != 0) {
            other = tileTable[this.coordinates.getX() - 1][this.coordinates.getY()];
        } else if (this.direction == Direction.EAST && this.coordinates.getY() != 6) {
            other = tileTable[this.coordinates.getX()][this.coordinates.getY() + 1];
        } else if (this.direction == Direction.SOUTH && this.coordinates.getX() != 4) {
            other = tileTable[this.coordinates.getX() + 1][this.coordinates.getY()];
        } else if (this.direction == Direction.WEST && this.coordinates.getY() != 0) {
            other = tileTable[this.coordinates.getX()][this.coordinates.getY() - 1];
        }
        if(other.isPresent() && !(other.get() instanceof VoidTile)){
            return false;
        }
        return super.isCorrect();
    }

    /**
     * Rotates the cannon clockwise and updates its direction accordingly.
     */
    public void rotate() {
        super.rotate();
        int i = this.direction.ordinal() + 1;
        if (i > 3) {
            i = 0;
        }
        this.direction = Direction.values()[i];
    }

    /**
     * Default constructor for deserialization or empty initialization.
     */
    public Cannon() {
        super();
    }

    /**
     * Loads tile data from a saved format (string array).
     *
     * @param attributes the serialized attributes of the tile
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.direction = Direction.fromString(attributes[7]);
    }
}
