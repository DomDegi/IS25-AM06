package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Represents a VoidTile, a special type of tile used as a placeholder
 * for invalid or non-usable areas of the ship grid.
 * It is non-interactive, non-fillable, and always returns true for validity checks.
 */
public class VoidTile extends Tile {

    /**
     * Constructs a VoidTile with predefined smooth connectors and no image.
     * Key is set to -1 by default.
     *
     * @param north      ignored, always replaced with SMOOTH
     * @param south      ignored, always replaced with SMOOTH
     * @param east       ignored, always replaced with SMOOTH
     * @param west       ignored, always replaced with SMOOTH
     * @param imagePath  ignored, always null
     */
    public VoidTile(Link north, Link south, Link east, Link west, String imagePath) {
        super(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),
                new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),
                null, 0, -1);
    }

    /**
     * Default constructor used in deserialization or generic creation.
     * Initializes with all SMOOTH connectors and key -1.
     */
    public VoidTile() {
        super(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),
                new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),
                null, 0, -1);
    }

    /**
     * Always returns true, since VoidTile is never invalid by definition.
     *
     * @return true
     */
    @Override
    public boolean isCorrect() {
        return true;
    }

    /**
     * Indicates that this tile cannot contain any component or connection.
     *
     * @return false always
     */
    @Override
    public boolean fillable() {
        return false;
    }

    /**
     * Returns an ASCII-art representation of the void tile.
     *
     * @return stylized string block of solid "▓" characters
     */
    @Override
    public String toString() {
        return "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    @Override
    public String toString1() {
        return "▓▓▓▓▓▓▓▓";
    }

    @Override
    public String toString2() {
        return "▓▓▓▓▓▓▓▓";
    }

    @Override
    public String toString3() {
        return "▓▓▓▓▓▓▓▓";
    }

    /**
     * Encodes this tile to a minimal representation ("VT").
     *
     * @return "VT" string tag
     */
    @Override
    public String toStringData() {
        return "VT";
    }

    /**
     * VoidTile does not require any loading logic; this is a no-op.
     *
     * @param attributes ignored
     */
    @Override
    public void tileLoader(String[] attributes) {
        // no-op
    }
}
