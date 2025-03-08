package it.polimi.ingsw.galaxytruckerproject.tiles;

public class BatteryComponents extends Tile{
    int batteryCells;
    public BatteryComponents(int batteryCells) {
        super();
        this.batteryCells = batteryCells;
    }
    public void getStat(){
        // We need to implement Dashboard first
    }
    public int getNumBatteries(){
        return this.batteryCells;
    }
    public void consumeNumBatteries(){
        this.batteryCells--;
    }
}
