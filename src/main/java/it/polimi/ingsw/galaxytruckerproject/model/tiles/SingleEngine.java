package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class SingleEngine extends Engine{
    public SingleEngine(Link north, Link east, Link south, Link west,int key) {
        super(north, east, south, west,key);
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public SingleEngine(Link north, Link east, Link south, Link west) {
        super(north, east, south, west,0);
    }

    @Override
    public String toString() {
        return "SingleEngine:"+ super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }

    @Override
    public Direction getDirection(){
        return direction;
    }

    @Override
    public String toString1(){
        switch (getDirection()){
            case NORTH->{
                return " ↑  "+getNorth()+"   ";
            }
            case EAST->{
                return " →  "+getNorth()+"   ";
            }
            case SOUTH->{
                return " ↓  "+getNorth()+"   ";
            }
            case WEST->{
                return " ←  "+getNorth()+"   ";
            }
        }
        return "    "+getNorth()+"   ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" EE "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if (getKey() >= 100)
            return "   " + getSouth() +" " + getKey();
        else if (getKey() >= 10 && getKey() < 100)
            return "   " + getSouth() +" " + getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
    }

    public void getStat(){
        shipBoard.addBreakSingleEngine(true);
    }
    public void destroy(){
        shipBoard.addBreakSingleEngine(false);
        super.destroy();
    }

    @Override
    public int getEngineStrength(){
        return 1;
    }

    @Override
    public String toStringData() {
        return "SE " + key + " " + north.toString() + " " + east.toString() + " " + south.toString() + " " + west.toString() + " " + direction.toString();
    }

    public SingleEngine(){}

    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
    }
}
