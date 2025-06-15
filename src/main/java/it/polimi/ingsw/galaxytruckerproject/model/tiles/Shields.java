package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Objects;

/**
 * Represents a shield tile in the game, which can protect one of the four corners of the ship
 * (North-East, South-East, South-West, North-West). The covered area rotates with the tile.
 */
public class Shields extends Tile {

    /** The area this shield tile covers */
    Coverage coveredArea;

    /**
     * Constructs a {@code Shields} tile with specified links and data.
     *
     * @param north the link on the north side
     * @param east the link on the east side
     * @param south the link on the south side
     * @param west the link on the west side
     * @param imagePath the path to the image resource
     * @param rotation the initial rotation of the tile
     * @param key unique identifier for the tile
     */
    public Shields(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
        this.coveredArea = Coverage.NORTH_EAST;
    }

    /**
     * Constructor for testing purposes only.
     */
    public Shields(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
        this.coveredArea = Coverage.NORTH_EAST;
    }

    /**
     * Rotates the shield and updates its covered area accordingly.
     */
    @Override
    public void rotate() {
        super.rotate();
        int i = this.coveredArea.ordinal() + 1;
        if (i > 3) i = 0;
        this.coveredArea = Coverage.values()[i];
    }

    /**
     * Updates the shipBoard with this shield's coverage.
     */
    public void getStat() {
        shipBoard.getCoverageShields().add(this.coveredArea);
    }

    /**
     * Gets the area currently covered by the shield.
     *
     * @return the covered area
     */
    public Coverage getCoveredArea() {
        return this.coveredArea;
    }

    /**
     * Removes this shield's effect from the shipBoard and destroys the tile.
     */
    public void destroy() {
        for (Coverage cov : shipBoard.getCoverageShields()) {
            if (cov.equals(this.coveredArea)) {
                shipBoard.getCoverageShields().remove(cov);
                super.destroy();
                return;
            }
        }
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
        return "SH " + key + " " + image + " " + north.toString() + " " + east.toString() + " "
                + south.toString() + " " + west.toString() + " " + coveredArea.toStringData();
    }

    /**
     * Default constructor for deserialization.
     */
    public Shields() {}

    /**
     * Loads the tile's attributes from a given array of strings.
     *
     * @param attributes the string array representing the tile data
     */
    @Override
    public void tileLoader(String[] attributes) {
        int k = 0;
        super.tileLoader(attributes);
        if (imagePath != null) {
            k++;
        }
        this.coveredArea = Coverage.fromStringData(attributes[7 + k]);
        if (Objects.equals(attributes[7], "NONE")) {
            System.out.println("Why this shield has no coverage?");
            throw new IllegalArgumentException();
        }
    }

    // String rendering methods

    @Override
    public String toString() {
        return "Shield coverage:" + coveredArea.toString() + super.toString() +
                "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    @Override
    public String toString1() {
        if (getCoveredArea() == Coverage.NORTH_EAST || getCoveredArea() == Coverage.NORTH_WEST)
            return " █  " + getNorth() + " █ ";
        else if (getCoveredArea() == Coverage.SOUTH_EAST) {
            if (getKey() >= 100)
                return getKey() + " " + getNorth() + " █ ";
            else if (getKey() >= 10)
                return " " + getKey() + " " + getNorth() + " █ ";
            else
                return " " + getKey() + "  " + getNorth() + " █ ";
        } else if (getCoveredArea() == Coverage.SOUTH_WEST) {
            if (getKey() >= 100)
                return " █ " + getNorth() + " " + getKey();
            else if (getKey() >= 10)
                return " █ " + getNorth() + " " + getKey() + " ";
            else
                return " █ " + getNorth() + "  " + getKey() + " ";
        } else {
            return "   " + getNorth() + "   ";
        }
    }

    @Override
    public String toString2() {
        return " " + getWest() + " SH " + getEast() + " ";
    }

    @Override
    public String toString3() {
        if (getCoveredArea() == Coverage.NORTH_WEST) {
            if (getKey() >= 100)
                return " █ " + getSouth() + " " + getKey();
            else if (getKey() >= 10)
                return " █ " + getSouth() + " " + getKey() + " ";
            else
                return " █ " + getSouth() + "  " + getKey() + " ";
        } else if (getCoveredArea() == Coverage.NORTH_EAST) {
            if (getKey() >= 100)
                return getKey() + " " + getSouth() + " █ ";
            else if (getKey() >= 10)
                return " " + getKey() + " " + getSouth() + " █ ";
            else
                return " " + getKey() + " " + getSouth() + "  █ ";
        } else if (getCoveredArea() == Coverage.SOUTH_EAST || getCoveredArea() == Coverage.SOUTH_WEST) {
            return " █ " + getSouth() + "  █ ";
        } else {
            if (getKey() >= 100)
                return "   " + getSouth() + " " + getKey();
            else if (getKey() >= 10)
                return "   " + getSouth() + " " + getKey() + " ";
            else
                return "   " + getSouth() + "  " + getKey() + " ";
        }
    }
}
