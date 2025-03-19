package it.polimi.ingsw.galaxytruckerproject.tiles;

public class DoubleCannon extends Cannon{

    public DoubleCannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }


    public boolean chooseToUse(){
        System.out.println("DoubleCannon");
        return true;
    }

    @Override
    public String toString() {
        return"DoubleCannon " +super.toString();
    }

    public void getStat(){
        shipBoard.addBreakDoubleCannon(true,this.coordinates);
    }

    public void destroy(){
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
