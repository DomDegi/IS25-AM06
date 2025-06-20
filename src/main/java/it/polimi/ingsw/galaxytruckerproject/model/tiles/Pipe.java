package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Objects;

/**
 * Represents a Pipe tile used to connect components within a ship.
 * Pipes serve as non-functional structural connections, useful to ensure continuity
 * between functional components like engines, cabins, or batteries.
 */
public class Pipe extends Tile {

    /**
     * Constructs a Pipe tile with full attributes including image and rotation.
     *
     * @param north the link on the north side
     * @param east  the link on the east side
     * @param south the link on the south side
     * @param west  the link on the west side
     * @param imagePath the image path representing the tile
     * @param rotation the rotation of the tile
     * @param key the unique identifier for this tile
     */
    public Pipe(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
    }

    /**
     * Simplified constructor for testing purposes, without image or rotation data.
     *
     * @param north the link on the north side
     * @param east  the link on the east side
     * @param south the link on the south side
     * @param west  the link on the west side
     */
    public Pipe(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
    }

    /**
     * Default constructor for deserialization or empty initialization.
     */
    public Pipe() {}

    /**
     * Returns a string representing the tile with human-readable graphics.
     *
     * @return visual representation of the tile
     */
    @Override
    public String toString() {
        return "Pipe" + " " + super.toString()
                + "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    /**
     * First line of the visual tile representation.
     *
     * @return formatted string
     */
    @Override
    public String toString1() {
        return "    " + getNorth() + "   ";
    }

    /**
     * Second line of the visual tile representation.
     *
     * @return formatted string
     */
    @Override
    public String toString2() {
        return " " + getWest() + " PP " + getEast() + " ";
    }

    /**
     * Third line of the visual tile representation.
     *
     * @return formatted string
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

    /**
     * Returns a compact string suitable for saving/serialization.
     *
     * @return serialized tile representation
     */
    @Override
    public String toStringData() {
        String image;
        image = Objects.requireNonNullElse(imagePath, "N");
        return "PP " + key + " " + image + " " + north.toString() + " " + east.toString()
                + " " + south.toString() + " " + west.toString();
    }

    /**
     * Loads tile properties from an array of string attributes.
     *
     * @param attributes array of string values representing the tile's data
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
    }
}
