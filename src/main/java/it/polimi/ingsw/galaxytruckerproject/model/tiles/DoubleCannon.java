package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class DoubleCannon extends Cannon{

    public DoubleCannon(Link north, Link east, Link south, Link west, int key) {
        super(north, east, south, west, key);
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
