package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Represents a blue cargo hold tile that can store non-hazardous goods.
 * This cargo hold cannot contain hazardous (red) goods.
 */
public class CargoBlue extends CargoHold {

    /**
     * Full constructor for the CargoBlue tile.
     *
     * @param totSpaces   the total number of goods this cargo hold can store
     * @param north       the link on the north side
     * @param east        the link on the east side
     * @param west        the link on the west side
     * @param south       the link on the south side
     * @param imagePath   path to the tile image
     * @param rotation    initial rotation
     * @param key         unique identifier for the tile
     */
    public CargoBlue(int totSpaces, Link north, Link east, Link west, Link south, String imagePath, int rotation, int key) {
        super(totSpaces, north, east, west, south, imagePath, rotation, key);
        hazard = false;
    }

    /**
     * Constructor for testing purposes without image or ID.
     *
     * @param totSpaces total number of cargo spaces
     * @param north     the north link
     * @param east      the east link
     * @param west      the west link
     * @param south     the south link
     */
    public CargoBlue(int totSpaces, Link north, Link east, Link west, Link south) {
        super(totSpaces, north, east, west, south, null, 0, 0);
        hazard = false;
    }

    /**
     * Returns a string representation of the tile with visual formatting.
     */
    @Override
    public String toString() {
        return "Cargo Blue" + super.toString() +
                "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    /**
     * Returns the first line of the formatted tile string.
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
            else if (getCargo().size() >= 2)
                return " " + getCargo().getFirst().toString() + "  " + getNorth() + " " + getCargo().get(1).toString() + " ";
            else
                return " ░  " + getNorth() + " ░ ";
        }
    }

    /**
     * Returns the second line of the formatted tile string.
     */
    @Override
    public String toString2() {
        return " " + getWest() + " CB " + getEast() + " ";
    }

    /**
     * Returns the third line of the formatted tile string.
     */
    @Override
    public String toString3() {
        if (getTotSpaces() == 3) {
            if (getKey() >= 100) {
                if (getCargo().size() == 3)
                    return " " + getCargo().get(2).toString() + " " + getSouth() + " " + getKey();
                else
                    return " ░ " + getSouth() + " " + getKey();
            } else if (getKey() >= 10) {
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
            else if (getKey() >= 10)
                return "   " + getSouth() + " " + getKey() + " ";
            else
                return "   " + getSouth() + "  " + getKey() + " ";
        }
    }

    /**
     * Serializes the tile to a string for persistence or transmission.
     *
     * @return string representing the tile's state
     */
    @Override
    public String toStringData() {
        StringBuilder sb = new StringBuilder();
        sb.append("CB ").append(key).append(" ").append(north.toString())
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
     * Default constructor for deserialization.
     */
    public CargoBlue() {
        super();
    }

    /**
     * Loads the tile's attributes from serialized data.
     *
     * @param attributes the serialized data as a string array
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.hazard = false;
    }
}
