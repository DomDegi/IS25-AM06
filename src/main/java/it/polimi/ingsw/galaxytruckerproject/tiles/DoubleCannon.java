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
        if(direction == Direction.NORTH)
            shipBoard.addBreakStraightDoubleCannon(true);
        else
            shipBoard.addBreakSidewaysDoubleCannon(true);
    }

    public void destroy(){
        if(direction == Direction.NORTH)
            shipBoard.addBreakStraightDoubleCannon(false);
        else
            shipBoard.addBreakSidewaysDoubleCannon(false);
    }
}
