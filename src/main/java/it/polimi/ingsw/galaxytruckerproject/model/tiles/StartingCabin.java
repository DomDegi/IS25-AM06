package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;

import java.util.Objects;

/**
 * Represents the Starting Cabin of a player's ship.
 * Each starting cabin begins with 2 human crew members and is associated with a player color.
 */
public class StartingCabin extends Cabin {

    /** Color of the player to whom this cabin belongs */
    PlayersColor playerColor;

    /**
     * Full constructor used during normal game execution.
     *
     * @param nord      the link on the north side
     * @param east      the link on the east side
     * @param south     the link on the south side
     * @param west      the link on the west side
     * @param imagePath the path to the image asset
     * @param rotation  the rotation of the tile
     * @param key       the unique identifier of the tile
     */
    public StartingCabin(Link nord, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(nord, east, south, west, imagePath, rotation, key);
        crew = 2;
    }

    /**
     * Constructor for testing purposes.
     */
    public StartingCabin(Link nord, Link east, Link south, Link west) {
        super(nord, east, south, west, null, 0, 0);
        crew = 2;
    }

    /**
     * Registers this cabin’s presence in the shipboard statistics.
     */
    public void getStat() {
        shipBoard.getCabinsCoordinates().add(this.coordinates);
        shipBoard.addBreakHumanCrew(crew);
    }

    /**
     * Removes one crew member from the cabin.
     *
     * @return true if a crew member was removed, false if none left
     */
    public boolean removeCrew() {
        if (crew > 0) {
            crew--;
            shipBoard.addBreakHumanCrew(-1);
            return true;
        }
        return false;
    }

    /**
     * Sets the coordinates and assigns the color based on the ship’s player.
     *
     * @param coordinates the new coordinates
     */
    @Override
    public void setCoordinates(Coordinates coordinates) {
        playerColor = shipBoard.getPlayer().getPlayerColor();
        super.setCoordinates(coordinates);
    }

    /**
     * Destroys the cabin and updates crew and board statistics accordingly.
     */
    public void destroy() {
        shipBoard.getCabinsCoordinates().remove(this.coordinates);
        shipBoard.addBreakHumanCrew(-crew);
        crew = 0;
        super.destroy();
    }

    /**
     * Returns an ASCII-based string representation of the cabin.
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(playerColor.toString()).append(" Starting Cabin: ");
        s.append(" numCrew:").append(this.crew).append(" Human");
        return s.toString() + super.toString() + "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    @Override
    public String toString1() {
        if (getCrew() == 2)
            return " H  " + getNorth() + " H ";
        else if (getCrew() == 1)
            return " H  " + getNorth() + "   ";
        return "    " + getNorth() + "   ";
    }

    @Override
    public String toString2() {
        return " " + getWest() + " SC " + getEast() + " ";
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
     * Serializes this tile's data for saving/loading.
     *
     * @return formatted data string
     */
    @Override
    public String toStringData() {
        String image;
        image = Objects.requireNonNullElse(imagePath, "N");
        return "ST " + image + " " + playerColor.toString() + " " + crew;
    }

    /**
     * Default constructor for deserialization.
     */
    public StartingCabin() {}

    /**
     * Loads the tile data from string attributes.
     *
     * @param attributes the array of data describing the tile
     */
    @Override
    public void tileLoader(String[] attributes) {
        this.north = new Link(Connectors.UNIVERSAL);
        this.east = new Link(Connectors.UNIVERSAL);
        this.south = new Link(Connectors.UNIVERSAL);
        this.west = new Link(Connectors.UNIVERSAL);
        if (!attributes[1].equals("N")) {
            this.imagePath = attributes[1];
        }
        else {
            this.imagePath = null;
        }
        this.playerColor = PlayersColor.fromString(attributes[2]);
        this.crew = Integer.parseInt(attributes[3]);
        this.key = 0;
    }
}
