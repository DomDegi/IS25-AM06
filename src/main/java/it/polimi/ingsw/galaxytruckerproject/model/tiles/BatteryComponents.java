package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class BatteryComponents extends Tile{
    int batteryCells;
    public BatteryComponents(Link nord, Link east, Link south, Link west, int numCells) {
        super(nord,east,south,west);
        this.batteryCells = numCells;
    }

    @Override
    public String toString() {
        return "BatteryComponents "+batteryCells+" cells"+super.toString();
    }

    public int getNumBatteries(){
        return this.batteryCells;
    }
    public void consumeBattery(){
        if(this.batteryCells > 0){
            this.batteryCells--;
            shipBoard.addBreakBatteries(-1);
            if(this.batteryCells == 0){
                shipBoard.getBatteryCoordinates().remove(this.batteryCells);
            }
        }
        else{System.out.println("Run out of batteries in this Tile");}
    }


    public void getStat(){
        shipBoard.addBreakBatteries(batteryCells);
        shipBoard.getBatteryCoordinates().add(this.coordinates);
    }
    public void destroy(){
        shipBoard.addBreakBatteries(-batteryCells);
        shipBoard.getBatteryCoordinates().remove(this.coordinates);
        super.destroy();
    }



}
