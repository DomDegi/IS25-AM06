package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Abstract class representing a cabin tile on a ship.
 * A cabin contains crew members and extends the generic {@link Tile} class.
 * Specific cabin types (e.g., human or alien cabins) should extend this class.
 */
public abstract class Cabin extends Tile {

    /** Number of crew members in the cabin. */
    protected int crew;

    /**
     * Constructor for cabin tile with full initialization.
     *
     * @param north      link on the north side
     * @param east       link on the east side
     * @param south      link on the south side
     * @param west       link on the west side
     * @param imagePath  path to the tile's image
     * @param rotation   rotation of the tile
     * @param key        unique tile identifier
     */
    public Cabin(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
    }

    /**
     * Constructor used for testing purposes (default rotation and key).
     *
     * @param north     link on the north side
     * @param east      link on the east side
     * @param south     link on the south side
     * @param west      link on the west side
     * @param imagePath path to the tile's image
     */
    public Cabin(Link north, Link east, Link south, Link west, String imagePath) {
        super(north, east, south, west, imagePath, 0, 0);
    }

    /**
     * Returns the number of crew members in the cabin.
     *
     * @return the crew count
     */
    @Override
    public int getCrew() {
        return crew;
    }

    /**
     * Default constructor for deserialization or subclass initialization.
     */
    public Cabin() {
        super();
    }
}
