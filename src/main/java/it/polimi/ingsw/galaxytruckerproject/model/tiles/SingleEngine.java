package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Represents a single engine tile in the game.
 * It contributes 1 engine strength and must be oriented SOUTH to be valid.
 */
public class SingleEngine extends Engine {

    /**
     * Constructs a single engine tile with full configuration.
     *
     * @param north      the link on the north side
     * @param east       the link on the east side
     * @param south      the link on the south side
     * @param west       the link on the west side
     * @param imagePath  the image path for UI
     * @param rotation   the rotation of the tile
     * @param key        the unique identifier
     */
    public SingleEngine(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
    }

    /**
     * Constructor for testing purposes.
     */
    public SingleEngine(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
    }

    /**
     * Returns the direction this engine is facing.
     *
     * @return the direction
     */
    @Override
    public Direction getDirection() {
        return direction;
    }

    /**
     * Registers this engine on the ship board for statistics tracking.
     */
    public void getStat() {
        shipBoard.addBreakSingleEngine(true);
    }

    /**
     * Destroys this tile and updates ship stats accordingly.
     */
    public void destroy() {
        shipBoard.addBreakSingleEngine(false);
        super.destroy();
    }

    /**
     * Returns the engine strength provided by this tile.
     * For a single engine, this is always 1.
     *
     * @return the engine strength
     */
    @Override
    public int getEngineStrength() {
        return 1;
    }

    /**
     * Serializes this tile into a string format.
     *
     * @return the serialized string representing the tile
     */
    @Override
    public String toStringData() {
        return "SE " + key + " " + north.toString() + " " + east.toString() + " "
                + south.toString() + " " + west.toString() + " " + direction.toString();
    }

    /**
     * Default constructor for deserialization.
     */
    public SingleEngine() {}

    /**
     * Loads the tile data from the given array of attributes.
     *
     * @param attributes the attributes describing this tile
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
    }

    // ASCII visual representation methods

    /**
     * Returns a detailed ASCII representation of the tile.
     *
     * @return a formatted string representing the tile
     */
    @Override
    public String toString() {
        return "SingleEngine:" + super.toString() +
                "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    /**
     * ASCII top row.
     */
    @Override
    public String toString1() {
        return switch (getDirection()) {
            case NORTH -> " ↑  " + getNorth() + "   ";
            case EAST  -> " →  " + getNorth() + "   ";
            case SOUTH -> " ↓  " + getNorth() + "   ";
            case WEST  -> " ←  " + getNorth() + "   ";
        };
    }

    /**
     * ASCII middle row.
     */
    @Override
    public String toString2() {
        return " " + getWest() + " EE " + getEast() + " ";
    }

    /**
     * ASCII bottom row.
     */
    @Override
    public String toString3() {
        if (getKey() >= 100)
            return "   " + getSouth() + " " + getKey();
        else if (getKey() >= 10)
            return "   " + getSouth() + " " + getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
    }
}
