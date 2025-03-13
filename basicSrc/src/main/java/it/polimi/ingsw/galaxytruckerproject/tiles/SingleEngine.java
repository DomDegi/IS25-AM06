package it.polimi.ingsw.galaxytruckerproject.tiles;

public class SingleEngine extends Engine{
    public SingleEngine(Link nord, Link east, Link south, Link west) {
        super();

    }

    @Override
    public String toString() {
        return "SingleEngine:"+ super.toString();
    }

    public void getStat(){
        shipBoard.addBreakSingleEngine(true);
    }
    public void destroy(){
        shipBoard.addBreakSingleEngine(false);
        super.destroy();
    }
}
