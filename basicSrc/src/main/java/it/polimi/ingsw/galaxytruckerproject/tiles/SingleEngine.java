package it.polimi.ingsw.galaxytruckerproject.tiles;

public class SingleEngine extends Engine{
    SingleEngine(){
        super();
    }

    public void getStat(){
        shipBoard.addBreakSingleEngine(true);
    }
    public void destroy(){
        shipBoard.addBreakSingleEngine(false);
        super.destroy();
    }
}
