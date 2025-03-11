package it.polimi.ingsw.galaxytruckerproject.tiles;

public class BatteryComponents extends Tile{
    int batteryCells;
    public BatteryComponents(int batteryCells, Link nord, Link south, Link east, Link west) {
        super(nord,south,west,east);
        this.batteryCells = batteryCells;
    }
    public int getNumBatteries(){
        return this.batteryCells;
    }
    public void consumeNumBatteries(){
        if(this.batteryCells > 0){this.batteryCells--;}
        else{System.out.println("Run out of batteries in this Tile");}
    }

    public void getStat(){
        shipBoard.addBreakBatteries(batteryCells);
        shipBoard.getBatteryCoordinates().add(this.coordinates);
    }
    public void destroy(){
        shipBoard.addBreakBatteries(-batteryCells);
        super.destroy();
    }


}
