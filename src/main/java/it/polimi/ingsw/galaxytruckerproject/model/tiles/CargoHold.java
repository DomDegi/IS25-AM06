package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents a cargo hold tile on a ship. It can store a certain number of goods,
 * and optionally support hazardous goods (RED) if the hazard flag is set to true.
 */
public abstract class CargoHold extends Tile {

    /**
     * Maximum number of goods this cargo hold can contain.
     */
    protected int totSpaces;

    /**
     * Indicates whether this cargo hold supports hazardous (RED) goods.
     */
    protected boolean hazard;

    /**
     * The list of goods currently stored in the cargo hold.
     */
    protected ArrayList<Goods> cargo = new ArrayList<>();

    /**
     * Full constructor.
     *
     * @param totSpaces the maximum number of goods
     * @param north     link on the north side
     * @param east      link on the east side
     * @param south     link on the south side
     * @param west      link on the west side
     * @param imagePath image path for rendering
     * @param rotation  tile rotation
     * @param key       tile identifier
     */
    public CargoHold(int totSpaces, Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
        this.totSpaces = totSpaces;
    }

    /**
     * Constructor used for testing without image or key.
     *
     * @param totSpaces the number of goods this hold can contain
     * @param north     north link
     * @param east      east link
     * @param south     south link
     * @param west      west link
     */
    public CargoHold(int totSpaces, Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
        this.totSpaces = totSpaces;
        this.cargo = new ArrayList<>();
    }

    /**
     * Returns a short string representation of the cargo contents.
     */
    @Override
    public String toString() {
        return cargo.stream()
                .map(Goods::toString)
                .collect(Collectors.joining()) + " " + super.toString();
    }

    /**
     * Registers the coordinates of this cargo hold on the shipBoard.
     */
    public void getStat() {
        shipBoard.getCargoHoldCoordinates().add(this.coordinates);
    }

    /**
     * Attempts to add a good to the cargo hold.
     *
     * @param good the good to add
     * @return 1 if successful, 0 if full, -1 if invalid due to hazard
     */
    public int addGood(Goods good) {
        if (cargo.size() == totSpaces) {
            System.out.println("\nCargoHold is full");
            return 0;
        }
        if (good.getColor().equals(GoodsColor.RED) && !hazard) {
            return -1;
        } else {
            cargo.add(good);
            return 1;
        }
    }

    /**
     * Removes one instance of a good matching the given type.
     *
     * @param good the good to remove (by color)
     */
    public void removeGood(Goods good) {
        if (cargo.isEmpty()) {
            System.out.println("CargoHold is already empty");
        } else {
            cargo.removeIf(g -> g.getColor().equals(good.getColor()));
        }
    }

    /**
     * Clears all cargo and marks this tile as destroyed.
     */
    @Override
    public void destroy() {
        cargo.clear();
        this.shipBoard.getCargoHoldCoordinates().remove(this.coordinates);
        super.destroy();
    }

    /**
     * Default constructor for deserialization.
     */
    public CargoHold() {
        super();
    }

    /**
     * Returns the current goods in this cargo hold.
     *
     * @return list of goods
     */
    public ArrayList<Goods> getCargo() {
        return cargo;
    }

    /**
     * Returns whether this cargo hold supports hazardous goods.
     *
     * @return true if hazardous goods are supported, false otherwise
     */
    public boolean getHazard() {
        return hazard;
    }

    /**
     * Returns the total capacity of this cargo hold.
     *
     * @return total number of storable goods
     */
    public int getTotSpaces() {
        return totSpaces;
    }

    /**
     * Loads the tile from serialized string attributes.
     *
     * @param attributes array of strings representing tile data
     */
    @Override
    public void tileLoader(String[] attributes) {
        int k = 0;
        super.tileLoader(attributes);
        if (imagePath != null) {
            k++;
        }
        this.totSpaces = Integer.parseInt(attributes[8 + k]);
        if (attributes.length < 9 + k + totSpaces) {
            throw new IllegalArgumentException("Malformed data: not enough attributes.");
        }
        for (int i = 0; i < totSpaces; i++) {
            int goodsAttribute = 9 + k + i;
            if (!attributes[goodsAttribute].equals("N")) {
                cargo.add(new Goods(GoodsColor.fromString(attributes[goodsAttribute])));
            }
        }
    }

    /**
     * Sends a clone of this cargo hold without its ShipBoard reference.
     *
     * @return a cloned CargoHold
     */
    @Override
    public CargoHold send() {
        try {
            CargoHold cloned = (CargoHold) super.clone();
            cloned.setShipBoard(null);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
