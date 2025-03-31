package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class SingleEngine extends Engine{
    public SingleEngine(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }

    @Override
    public String toString() {
        return "SingleEngine:"+ super.toString();
    }

    public void getStat(){
        shipBoard.addBreakSingleEngine(true);
    }
    public void destroy(){
        super.destroy();
        shipBoard.addBreakSingleEngine(false);
    }
}
