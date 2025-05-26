package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.ArrayList;

import static it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType.BROWN;
import static it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType.NotSupportSystem;

/**
 * Represents a tile that supports a specific alien crew type (BROWN or PURPLE),
 * providing life support for adjacent cabins containing aliens of the same type.
 */
public class AlienLifeSupportsSystem extends Tile {

    /**
     * The type of alien crew this support system is compatible with.
     */
    CrewType colorAlienSupported;

    /**
     * Constructs an AlienLifeSupportsSystem tile with specified links, image, rotation, key, and alien color supported.
     *
     * @param north the link on the north side
     * @param east the link on the east side
     * @param south the link on the south side
     * @param west the link on the west side
     * @param imagePath path to the image representing the tile
     * @param rotation initial rotation of the tile
     * @param key unique identifier for the tile
     * @param colorAlienSupported the alien crew type supported (BROWN or PURPLE)
     */
    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west, String imagePath ,int rotation,int key, CrewType colorAlienSupported) {
        super(north, east, south, west, imagePath,rotation, key);
        this.colorAlienSupported = colorAlienSupported;
        if (colorAlienSupported == CrewType.HUMAN)
            System.out.println("AlienLifeSupportsSystem only supports BROWN AND PURPLE");
    }

    /**
     * Constructor for testing purposes without image path and key.
     *
     * @param north the link on the north side
     * @param east the link on the east side
     * @param south the link on the south side
     * @param west the link on the west side
     * @param colorAlienSupported the alien crew type supported
     */
    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west, CrewType colorAlienSupported) {
        super(north, east, south, west, null,0,0);
        this.colorAlienSupported = colorAlienSupported;
        if (colorAlienSupported == CrewType.HUMAN)
            System.out.println("AlienLifeSupportsSystem only supports BROWN AND PURPLE");
    }

    /**
     * Returns the crew type supported by this Alien Life Support System.
     *
     * @return the supported CrewType (BROWN or PURPLE)
     */
    public CrewType getAlienLifeSupportSystemColor() {return colorAlienSupported;}

    /**
     * Finds all adjacent equipped cabins that have an alien of the same color supported by this system.
     *
     * @return a list of coordinates of compatible adjacent cabins
     */
    public ArrayList<Coordinates> adjacentEquipCabinWithAlien() {
        ArrayList<Coordinates> adjacentEquipCabin = new ArrayList<>();
        if(this.coordinates.getX()!=0 && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].isPresent()){

            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCoordinates());
        }

        if(this.coordinates.getX()!=4 && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].isPresent()){
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCoordinates());
        }

        if(this.coordinates.getY()!=0 && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].isPresent()){
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].get().getCoordinates());
        }

        if(this.coordinates.getY()!=6 && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].isPresent()) {
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() + 1].get().getCoordinates());
        }
        return adjacentEquipCabin;
    }

    /**
     * Destroys the tile and updates all adjacent alien cabins to check for new alien compatibility.
     * Resets this tile's supported alien type to NotSupportSystem.
     */
    public void destroy() {
        this.colorAlienSupported=NotSupportSystem;
        ArrayList<Coordinates> adjacentEquipCabinwithAlien = adjacentEquipCabinWithAlien();
        for (Coordinates c : adjacentEquipCabinwithAlien) {
            shipBoard.getTile(c).checkAlienability();
        }
        super.destroy();
    }


    /**
     * Returns a formatted string representation of the tile, including type, color, and layout.
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return "AlienLifeSupportsSystem alienType=" + colorAlienSupported + " " + super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }

    /**
     * Returns the top visual line of the tile's ASCII representation.
     *
     * @return first line of visual representation
     */
    @Override
    public String toString1(){
        return " "+getAlienLifeSupportSystemColor().toString()+"  "+getNorth()+"   ";
    }

    /**
     * Returns the middle visual line of the tile's ASCII representation.
     *
     * @return second line of visual representation
     */
    @Override
    public String toString2(){
        return " "+getWest()+" AS "+getEast()+" ";
    }

    /**
     * Returns the bottom visual line of the tile's ASCII representation.
     *
     * @return third line of visual representation
     */
    @Override
    public String toString3(){
        if (getKey()>=100)
            return "   "+getSouth()+" "+getKey();
        else if(getKey()>=10&&getKey()<100)
            return "   "+getSouth()+" "+getKey()+" ";
        else
            return "   "+getSouth()+"  "+getKey()+" ";
    }

    /**
     * Returns a serialized representation of the tile for persistence.
     *
     * @return a string representing the tile's data
     */
    @Override
    public String toStringData() {
        return "AL " + key + " " + north.toString() + " " + east.toString() + " " + south.toString() + " " + west.toString() + " " + colorAlienSupported.toString();
    }

    /**
     * Default constructor used for deserialization or empty initialization.
     */
    public AlienLifeSupportsSystem() {
        super();
    }

    /**
     * Initializes the tile from a serialized string array.
     *
     * @param attributes array of attributes (must include the alien type at index 6)
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.colorAlienSupported = CrewType.fromString(attributes[6]);
    }
}
