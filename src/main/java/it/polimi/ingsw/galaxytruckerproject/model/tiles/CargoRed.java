package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Represents a red cargo hold, which is capable of storing hazardous goods (RED).
 */
public class CargoRed extends CargoHold {

    /**
     * Full constructor with all parameters.
     *
     * @param totSpaces number of spaces available in the cargo
     * @param north     link on the north side
     * @param east      link on the east side
     * @param west      link on the west side
     * @param south     link on the south side
     * @param imagePath path to the image representing this tile
     * @param rotation  rotation of the tile
     * @param key       unique identifier key for the tile
     */
    public CargoRed(int totSpaces, Link north, Link east, Link west, Link south, String imagePath, int rotation, int key) {
        super(totSpaces, north, east, west, south, imagePath, rotation, key);
        hazard = true;
    }

    /**
     * Simplified constructor for testing without image and key.
     *
     * @param totSpaces number of spaces in the cargo
     * @param north     north link
     * @param east      east link
     * @param west      west link
     * @param south     south link
     */
    public CargoRed(int totSpaces, Link north, Link east, Link west, Link south) {
        super(totSpaces, north, east, west, south, null, 0, 0);
        hazard = true;
    }

    /**
     * Default constructor used for deserialization or reflection.
     */
    public CargoRed() {}

    /**
     * Returns a stringified ASCII-art view of the tile and its contents.
     */
    @Override
    public String toString() {
        return "Cargo Red" + super.toString() + "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    /**
     * Builds the top row of the ASCII-art representation.
     */
    @Override
    public String toString1() {
        if (getTotSpaces() == 0) {
            return "    " + getNorth() + "   ";
        } else if (getTotSpaces() == 1) {
            if (getCargo().size() == 1)
                return " " + getCargo().getFirst().toString() + "  " + getNorth() + "   ";
            else
                return " ░  " + getNorth() + "   ";
        } else {
            if (getCargo().size() == 1)
                return " " + getCargo().getFirst().toString() + "  " + getNorth() + " ░ ";
            else if (getCargo().size() == 2 || getCargo().size() == 3)
                return " " + getCargo().getFirst().toString() + "  " + getNorth() + " " + getCargo().get(1).toString() + " ";
            else
                return " ░  " + getNorth() + " ░ ";
        }
    }

    /**
     * Builds the middle row of the ASCII-art representation.
     */
    @Override
    public String toString2() {
        return " " + getWest() + " CR " + getEast() + " ";
    }

    /**
     * Builds the bottom row of the ASCII-art representation.
     */
    @Override
    public String toString3() {
        if (getTotSpaces() == 3) {
            if (getKey() >= 100) {
                if (getCargo().size() == 3)
                    return " " + getCargo().get(2).toString() + " " + getSouth() + " " + getKey();
                else
                    return " ░ " + getSouth() + " " + getKey();
            } else if (getKey() >= 10 && getKey() < 100) {
                if (getCargo().size() == 3)
                    return " " + getCargo().get(2).toString() + " " + getSouth() + " " + getKey() + " ";
                else
                    return " ░ " + getSouth() + " " + getKey() + " ";
            } else {
                if (getCargo().size() == 3)
                    return " " + getCargo().get(2).toString() + " " + getSouth() + "  " + getKey() + " ";
                else
                    return " ░ " + getSouth() + "  " + getKey() + " ";
            }
        } else {
            if (getKey() >= 100)
                return "   " + getSouth() + " " + getKey();
            else if (getKey() >= 10 && getKey() < 100)
                return "   " + getSouth() + " " + getKey() + " ";
            else
                return "   " + getSouth() + "  " + getKey() + " ";
        }
    }

    /**
     * Serializes the tile data into a space-separated string.
     * Format: CR key north east south west totSpaces [goods...]
     *
     * @return the serialized tile string
     */
    @Override
    public String toStringData() {
        StringBuilder sb = new StringBuilder();
        sb.append("CR ").append(key).append(" ").append(north.toString())
                .append(" ").append(east.toString()).append(" ").append(south.toString())
                .append(" ").append(west.toString()).append(" ").append(totSpaces).append(" ");
        for (int i = 0; i < totSpaces; i++) {
            if (i < cargo.size())
                sb.append(cargo.get(i)).append(" ");
            else
                sb.append("N ");
        }
        return sb.toString();
    }

    /**
     * Deserializes tile attributes and restores cargo and state.
     *
     * @param attributes array of serialized tile data
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.hazard = true;
    }
}
