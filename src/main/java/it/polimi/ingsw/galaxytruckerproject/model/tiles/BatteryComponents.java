package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Represents a ship tile that stores and manages battery cells.
 * Batteries can be used to power other components and are tracked during gameplay.
 */
public class BatteryComponents extends Tile {

    /** The number of battery cells available in this component. */
    int batteryCells;

    /**
     * Full constructor for a BatteryComponents tile.
     *
     * @param north      link on the north side
     * @param east       link on the east side
     * @param south      link on the south side
     * @param west       link on the west side
     * @param imagePath  path to the image representing the tile
     * @param rotation   initial rotation of the tile
     * @param key        unique identifier key
     * @param numCells   initial number of battery cells
     */
    public BatteryComponents(Link north, Link east, Link south, Link west, String imagePath, int rotation,int key, int numCells) {
        super(north, east, south, west, imagePath,rotation, key);
        this.batteryCells = numCells;
    }

    /**
     * Simplified constructor for testing purposes.
     *
     * @param north    link on the north side
     * @param east     link on the east side
     * @param south    link on the south side
     * @param west     link on the west side
     * @param numCells initial number of battery cells
     */
    public BatteryComponents(Link north, Link east, Link south, Link west, int numCells) {
        super(north,east,south,west, null,0,0);
        this.batteryCells = numCells;
    }

    /**
     * @return the tile's string representation for debugging and CLI visualization
     */
    @Override
    public String toString() {
        return "BatteryComponents "+batteryCells+" cells"+super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }

    /**
     * @return first line of the CLI visual string of this tile
     */
    @Override
    public String toString1(){
        if(batteryCells==0)
            return "    "+getNorth()+"   ";
        else if(batteryCells==1)
            return " ▄  "+getNorth()+"   ";
        else
            return " ▄  "+getNorth()+" ▄ ";
    }

    /**
     * @return second line of the CLI visual string of this tile
     */
    @Override
    public String toString2(){
        return " "+getWest()+" BC "+getEast()+" ";
    }

    /**
     * @return third line of the CLI visual string of this tile
     */
    @Override
    public String toString3(){
        if(batteryCells==3) {
            if (getKey() >= 100)
                return " ▄ " + getSouth() +" " + getKey();
            else if (getKey() >= 10 && getKey() < 100)
                return " ▄ " + getSouth() + " " +getKey() + " ";
            else
                return " ▄ " + getSouth() + "  " + getKey() + " ";
        }else {
            if (getKey() >= 100)
                return "   " + getSouth() +" " + getKey();
            else if (getKey() >= 10 && getKey() < 100)
                return "   " + getSouth() +" " + getKey() + " ";
            else
                return "   " + getSouth() + "  " + getKey() + " ";
        }
    }

    /**
     * @return the number of batteries currently stored in the component
     */
    public int getNumBatteries(){
        return this.batteryCells;
    }

    /**
     * Consumes one battery, updates the shipBoard statistics, and removes the tile from battery list if empty.
     */
    public void consumeBattery(){
        if(this.batteryCells > 0){
            this.batteryCells--;
            shipBoard.addBreakBatteries(-1);
            if(this.batteryCells == 0){
                if(shipBoard.getBatteryCoordinates()!=null&&shipBoard.getBatteryCoordinates().contains(this.coordinates)){
                    shipBoard.getBatteryCoordinates().remove(this.coordinates);
                }
            }
        }
        else{System.out.println("Run out of batteries in this Tile");}
    }

    /**
     * Adds one battery to this component and updates the shipBoard's battery tracking.
     */
    @Override
    public void addBattery() {
        this.batteryCells++;
        shipBoard.addBreakBatteries(+1);
        if(!shipBoard.getBatteryCoordinates().contains(this.coordinates)){
            shipBoard.getBatteryCoordinates().add(this.coordinates);
        }
    }

    /**
     * Registers the battery tile and its current number of cells into the shipBoard's tracking.
     */
    public void getStat(){
        shipBoard.addBreakBatteries(batteryCells);
        shipBoard.getBatteryCoordinates().add(this.coordinates);
    }

    /**
     * Destroys the tile and removes all references from the shipBoard.
     */
    public void destroy(){
        shipBoard.addBreakBatteries(-batteryCells);
        shipBoard.getBatteryCoordinates().remove(this.coordinates);
        super.destroy();
    }

    /**
     * Default constructor used for deserialization.
     */
    public BatteryComponents(){
    }

    /**
     * Converts the tile's data into a string format for serialization.
     *
     * @return string representing this tile's state
     */
    @Override
    public String toStringData() {
        return "BC " + key + " " + north.toString() + " " + east.toString() + " " + south.toString() + " " + west.toString() + " " + batteryCells;
    }

    /**
     * Loads the tile's data from a string array, typically after deserialization.
     *
     * @param attributes array of strings containing tile parameters
     */
    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.batteryCells = Integer.parseInt(attributes[6]);
    }
}
