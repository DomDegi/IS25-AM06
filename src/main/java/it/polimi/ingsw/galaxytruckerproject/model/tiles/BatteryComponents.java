package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class BatteryComponents extends Tile{
    int batteryCells;
    public BatteryComponents(Link nord, Link east, Link south, Link west, int key, int numCells) {
        super(nord,east,south,west,key);
        this.batteryCells = numCells;
    }

    //METODO COSTRUTTORE PER IL TESTING
    public BatteryComponents(Link nord, Link east, Link south, Link west, int numCells) {
        super(nord,east,south,west,0);
        this.batteryCells = numCells;
    }


    @Override
    public String toString() {
        return "BatteryComponents "+batteryCells+" cells"+super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }
    @Override
    public String toString1(){
        if(batteryCells==0)
            return "    "+getNorth()+"   ";
        else if(batteryCells==1)
            return " ▄  "+getNorth()+"   ";
        else
            return " ▄  "+getNorth()+" ▄ ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" BC "+getEast()+" ";
    }
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

    @Override
    public void addBattery() {
        this.batteryCells++;
        shipBoard.addBreakBatteries(+1);
        if(!shipBoard.getBatteryCoordinates().contains(this.coordinates)){
            shipBoard.getBatteryCoordinates().add(this.coordinates);
        }
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

    public BatteryComponents(){
    }

    @Override
    public String toStringData() {
        return "BC " + key + " " + north.toString() + " " + east.toString() + " " + south.toString() + " " + west.toString() + " " + batteryCells;
    }

    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.batteryCells = Integer.parseInt(attributes[6]);
    }
}
