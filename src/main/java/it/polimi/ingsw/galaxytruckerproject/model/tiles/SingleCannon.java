package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Objects;

/**
 * Represents a single cannon tile in the game.
 * The fire strength of this cannon depends on its orientation:
 * it deals full strength (1.0) if facing NORTH, otherwise it deals half strength (0.5).
 */
public class SingleCannon extends Cannon {

    /** The actual fire strength of the cannon, depending on its direction. */
    private float fireStrength;

    /**
     * Constructs a single cannon tile with given links and metadata.
     *
     * @param north the link on the north side
     * @param east the link on the east side
     * @param south the link on the south side
     * @param west the link on the west side
     * @param imagePath the image path for UI
     * @param rotation the initial rotation
     * @param key unique identifier for the tile
     */
    public SingleCannon(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
        updateFireStrength();
    }

    /**
     * Constructor for testing purposes only.
     */
    public SingleCannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
        if (direction == Direction.NORTH) {
            this.fireStrength = 1;
        } else {
            this.fireStrength = 0.5F;
        }
    }

    /**
     * Updates the fire strength on the ship board.
     */
    public void getStat() {
        shipBoard.addBreakSingleCannonPower(fireStrength);
    }

    /**
     * Destroys the tile and removes its contribution to fire strength.
     */
    public void destroy() {
        shipBoard.addBreakSingleCannonPower(-fireStrength);
        fireStrength = 0;
        super.destroy();
    }

    /**
     * Gets the direction the cannon is currently facing.
     * @return the cannon's direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns an indicator of strength used for meteor interactions.
     * Negative value signals a single cannon.
     * @return -2 if facing NORTH, -1 otherwise
     */
    @Override
    public int getStrength() {
        return direction == Direction.NORTH ? -2 : -1;
    }

    /**
     * Rotates the tile and updates its fire strength accordingly.
     */
    @Override
    public void rotate() {
        super.rotate();
        updateFireStrength();
    }

    /**
     * Updates the fire strength based on the current direction.
     */
    void updateFireStrength() {
        if (this.direction == Direction.NORTH)
            this.fireStrength = 1;
        else
            this.fireStrength = 0.5f;
    }

    /**
     * Serializes the tile into a string format.
     *
     * @return string representation of the tile's data
     */
    @Override
    public String toStringData() {
        String image;
        image = Objects.requireNonNullElse(imagePath, "N");
        return "SC " + key + " " + image + " " + north.toString() + " " + east.toString() + " "
                + south.toString() + " " + west.toString() + " " + direction.toString();
    }

    /**
     * Default constructor for deserialization.
     */
    public SingleCannon() {}

    /**
     * Loads the tile's attributes from a given array of strings.
     *
     * @param attributes the string array representing the tile data
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        updateFireStrength();
    }

    // Rendering methods for ASCII output
    @Override
    public String toString() {
        return "SingleCannon " + super.toString() +
                "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
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
        return " " + getWest() + " CC " + getEast() + " ";
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
}
