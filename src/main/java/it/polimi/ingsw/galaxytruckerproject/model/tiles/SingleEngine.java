package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class SingleEngine extends Engine{
    public SingleEngine(Link north, Link east, Link south, Link west,int key) {
        super(north, east, south, west,key);
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

    @Override
    public int getEngineStrength(){
        return 1;
    }
}
