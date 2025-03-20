package it.polimi.ingsw.galaxytruckerproject.tiles;

public class DoubleEngine extends Engine{

    public DoubleEngine(Link north,Link south,Link east,Link west){
        super(north,south,east,west);
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
        shipBoard.addBreakDoubleEngine(false,this.coordinates);
    }
}
