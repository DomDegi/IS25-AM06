package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Objects;

/**
 * Represents a double engine tile on the ship board.
 * A double engine provides double movement strength (2 units),
 * and its functionality is directional.
 */
public class DoubleEngine extends Engine {

    /**
     * Constructs a {@code DoubleEngine} with full attributes.
     *
     * @param north      the link on the north side
     * @param south      the link on the south side
     * @param east       the link on the east side
     * @param west       the link on the west side
     * @param imagePath  the image path of the tile
     * @param rotation   the rotation of the tile (in 90° steps)
     * @param key        the unique tile key
     */
    public DoubleEngine(Link north, Link south, Link east, Link west, String imagePath, int rotation, int key) {
        super(north, south, east, west, imagePath, rotation, key);
    }

    /**
     * Constructor for testing purposes only.
     *
     * @param north the link on the north side
     * @param south the link on the south side
     * @param east  the link on the east side
     * @param west  the link on the west side
     */
    public DoubleEngine(Link north, Link south, Link east, Link west) {
        super(north, south, east, west, null, 0, 0);
    }

    /**
     * Gets the direction the engine is facing.
     *
     * @return the engine's direction
     */
    @Override
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns a simplified string representation of the tile data,
     * including tile type, key, links, and direction.
     *
     * @return formatted tile data string
     */
    @Override
    public String toStringData() {
        String image;
        image = Objects.requireNonNullElse(imagePath, "N");
        return "DE " + key + " " + image + " " + rotation + " " + north.toString() + " " + east.toString() + " "
                + south.toString() + " " + west.toString() + " " + direction.toString();
    }

    /**
     * Loads the tile attributes from a serialized string array.
     *
     * @param attributes serialized tile information
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
    }

    /**
     * String representation of this tile for debugging and display.
     *
     * @return the visual representation of the tile
     */
    @Override
    public String toString() {
        return "DoubleEngine" + super.toString() + "\n┌────────┐\n│" + toString1() + "│\n│" +
                toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    @Override
    public String toString1() {
        return switch (getDirection()) {
            case NORTH -> " ↑  " + getNorth() + "   ";
            case EAST  -> " →  " + getNorth() + "   ";
            case SOUTH -> " ↓  " + getNorth() + "   ";
            case WEST  -> " ←  " + getNorth() + "   ";
        };
    }

    @Override
    public String toString2() {
        return " " + getWest() + " DE " + getEast() + " ";
    }

    @Override
    public String toString3() {
        if (getKey() >= 100)
            return "   " + getSouth() + " " + getKey();
        else if (getKey() >= 10)
            return "   " + getSouth() + " " + getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
    }

    /**
     * Used to confirm the tile should be used. Always returns true.
     *
     * @return true
     */
    public boolean chooseToUse() {
        return true;
    }

    /**
     * Updates the ship board with this engine's presence.
     */
    public void getStat() {
        shipBoard.addBreakDoubleEngine(true, this.coordinates);
    }

    /**
     * Destroys the engine and updates the ship board to reflect its removal.
     */
    @Override
    public void destroy() {
        super.destroy();
        shipBoard.addBreakDoubleEngine(false, this.coordinates);
    }

    /**
     * Returns the strength of the engine (always 2 for double engine).
     *
     * @return movement strength
     */
    @Override
    public int getEngineStrength() {
        return 2;
    }

    /**
     * Default constructor used for deserialization.
     */
    public DoubleEngine() {}
}
