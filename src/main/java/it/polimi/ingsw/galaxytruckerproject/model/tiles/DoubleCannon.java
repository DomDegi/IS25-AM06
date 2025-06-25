package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Objects;

/**
 * Represents a double cannon tile in the spaceship.
 * Double cannons provide more firepower than single ones but only when facing forward (NORTH).
 * The strength of the cannon depends on its direction.
 */
public class DoubleCannon extends Cannon {

    /**
     * Constructs a {@code DoubleCannon} with the specified connectors, image path, rotation, and key.
     *
     * @param north      the link on the north side
     * @param east       the link on the east side
     * @param south      the link on the south side
     * @param west       the link on the west side
     * @param imagePath  path to the tile image
     * @param rotation   rotation value (in 90° steps)
     * @param key        unique key identifier
     */
    public DoubleCannon(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
    }

    /**
     * Constructor for testing purposes.
     *
     * @param north the link on the north side
     * @param east  the link on the east side
     * @param south the link on the south side
     * @param west  the link on the west side
     */
    public DoubleCannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
    }

    /**
     * Method representing the usage of the double cannon.
     *
     * @return always true (used for logic flow validation)
     */
    public boolean chooseToUse() {
        System.out.println("DoubleCannon");
        return true;
    }

    /**
     * Gets the direction this cannon is facing.
     *
     * @return direction of the cannon
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the strength of the cannon.
     * Strength is 2 if facing NORTH, otherwise 1.
     *
     * @return firepower of the cannon
     */
    @Override
    public int getStrength() {
        return direction == Direction.NORTH ? 2 : 1;
    }

    /**
     * Updates the ship board status by registering this cannon.
     */
    public void getStat() {
        shipBoard.addBreakDoubleCannon(true, this.coordinates);
    }

    /**
     * Removes the cannon from the ship board and updates relevant data.
     */
    @Override
    public void destroy() {
        super.destroy();
        shipBoard.addBreakDoubleCannon(false, this.coordinates);
    }

    /**
     * Returns a compact string representation of this tile’s data.
     *
     * @return string containing tile type, key, links, and direction
     */
    @Override
    public String toStringData() {
        String image;
        image = Objects.requireNonNullElse(imagePath, "N");
        return "DC " + key + " " + image + " " + rotation + " " + north.toString() + " " + east.toString() + " " +
                south.toString() + " " + west.toString() + " " + direction.toString();
    }

    /**
     * Loads this tile's attributes from a string array (used in deserialization).
     *
     * @param attributes the serialized tile data
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
    }

    /**
     * Default constructor required for deserialization.
     */
    public DoubleCannon() {
    }

    /**
     * Returns a visual representation of the tile.
     */
    @Override
    public String toString() {
        return "DoubleCannon " + super.toString() + "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() +
                "│\n│" + toString3() + "│\n└────────┘";
    }

    @Override
    public String toString1() {
        return switch (getDirection()) {
            case NORTH -> " ↑  " + getNorth() + "   ";
            case EAST -> " →  " + getNorth() + "   ";
            case SOUTH -> " ↓  " + getNorth() + "   ";
            case WEST -> " ←  " + getNorth() + "   ";
        };
    }

    @Override
    public String toString2() {
        return " " + getWest() + " DC " + getEast() + " ";
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
