package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class DoubleEngine extends Engine{

    public DoubleEngine(Link north,Link south,Link east,Link west, int key){
        super(north,south,east,west,key);
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public DoubleEngine(Link north,Link south,Link east,Link west){
        super(north,south,east,west,0);
    }
    @Override
    public String toString() {
        return "DoubleEngine"+ super.toString();
    }

    public boolean chooseToUse(){
        return true;
    }

    public void getStat(){shipBoard.addBreakDoubleEngine(true,this.coordinates);}
    public void destroy(){
        super.destroy();
        shipBoard.addBreakDoubleEngine(false,this.coordinates);
    }

    @Override
    public int getEngineStrength(){
        return 2;
    }
}
