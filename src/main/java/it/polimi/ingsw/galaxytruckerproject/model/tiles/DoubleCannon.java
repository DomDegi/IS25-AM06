package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class DoubleCannon extends Cannon{

    public DoubleCannon(Link north, Link east, Link south, Link west, int key) {
        super(north, east, south, west, key);
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public DoubleCannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, 0);
    }

    public boolean chooseToUse(){
        System.out.println("DoubleCannon");
        return true;
    }

    @Override
    public String toString() {
        return"DoubleCannon " +super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }
    @Override
    public String toString1(){
        return "    "+getNorth()+"   ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" DC "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if (getKey() >= 100)
            return "   " + getSouth() +" " + getKey();
        else if (getKey() >= 10 && getKey() < 100)
            return "   " + getSouth() + " " +getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
    }

    public void getStat(){
        shipBoard.addBreakDoubleCannon(true,this.coordinates);
    }

    public void destroy(){
        super.destroy();
        shipBoard.addBreakDoubleCannon(false,this.coordinates);
    }

    public Direction getDirection(){
        return direction;
    }
    @Override
    public int getStrength() {
        if(direction == Direction.NORTH)
            return 2;
        return 1;
    }
}
