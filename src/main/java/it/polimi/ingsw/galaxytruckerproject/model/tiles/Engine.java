package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Optional;

/**
 * Abstract class representing an engine tile in the ship.
 * Engines provide movement during the flight phase and must be correctly oriented and placed.
 * The default direction is {@link Direction#SOUTH}, and this class ensures proper connectivity rules.
 */
public abstract class Engine extends Tile {

    /**
     * The direction in which the engine is facing.
     * Default is SOUTH.
     */
    protected Direction direction;

    /**
     * Full constructor used during game initialization.
     *
     * @param north      connector on the north side
     * @param east       connector on the east side
     * @param south      connector on the south side (forced to SMOOTH)
     * @param west       connector on the west side
     * @param imagePath  image path for the engine tile
     * @param rotation   rotation of the tile (0–3)
     * @param key        unique identifier for the tile
     */
    public Engine(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
        south.connectorsType = Connectors.SMOOTH;
        direction = Direction.SOUTH;
    }

    /**
     * Constructor for testing purposes, sets default values and no image.
     *
     * @param north connector on the north side
     * @param east  connector on the east side
     * @param south connector on the south side (forced to SMOOTH)
     * @param west  connector on the west side
     */
    public Engine(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
        south.connectorsType = Connectors.SMOOTH;
        direction = Direction.SOUTH;
    }

    /**
     * Default constructor for deserialization.
     */
    public Engine() {}

    /**
     * Returns a string representation of the engine including its direction.
     *
     * @return string with direction and tile info
     */
    @Override
    public String toString() {
        return "direction:" + direction.toString() + " " + super.toString();
    }

    /**
     * Rotates the engine 90 degrees clockwise and updates its direction.
     */
    @Override
    public void rotate() {
        super.rotate();
        int i = this.direction.ordinal() + 1;
        if (i > 3) i = 0;
        this.direction = Direction.values()[i];
    }

    /**
     * Destroys the engine tile, calling the base destroy method.
     */
    @Override
    public void destroy() {
        super.destroy();
    }

    /**
     * Verifies if the engine is correctly placed.
     * Engines must face south and cannot have fillable tiles directly behind them.
     *
     * @return true if the placement is valid, false otherwise
     */
    @Override
    public boolean isCorrect() {
        Optional<Tile> other = null;
        Optional<Tile>[][] tileTable = shipBoard.getTilesTable();

        if (this.direction != Direction.SOUTH)
            return false;

        if (this.coordinates.getX() != 4) {
            other = tileTable[this.coordinates.getX() + 1][this.coordinates.getY()];
            if (other.isPresent() && other.get().fillable()) {
                return false;
            }
        }

        return super.isCorrect();
    }

    /**
     * Loads tile data from string attributes, including direction.
     *
     * @param attributes array of serialized tile data
     */
    @Override
    public void tileLoader(String[] attributes) {
        int k = 0;
        super.tileLoader(attributes);
        if (imagePath != null) {
            k++;
        }
        this.direction = Direction.fromString(attributes[8 + k]);
    }
}
