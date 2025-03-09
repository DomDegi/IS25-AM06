package it.polimi.ingsw.galaxytruckerproject.tiles;

public class DoubleCannon extends Cannon{

    public DoubleCannon(){
        super();
    }


    public boolean chooseToUse(){
        System.out.println("DoubleCannon");
        return true;
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
        super.destroy();
    }
}
