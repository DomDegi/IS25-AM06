package it.polimi.ingsw.galaxytruckerproject.tiles;

public class BatteryComponents extends Tile{
    int batteryCells;
    public BatteryComponents(int batteryCells) {
        super();
        this.batteryCells = batteryCells;
    }
    public int getNumBatteries(){
        return this.batteryCells;
    }
    public void consumeNumBatteries(){
        this.batteryCells--;
    }

    public void getStat(){
        shipBoard.addBreakBatteries(batteryCells);
    }
    public void destroy(){
        shipBoard.addBreakBatteries(-batteryCells);
        super.destroy();
    }
}
