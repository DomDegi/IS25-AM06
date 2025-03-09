package it.polimi.ingsw.galaxytruckerproject.tiles;

public class DoubleEngine extends Engine{

    public DoubleEngine(){
        super();
    }

    public boolean ChoosetoUse(){
        return true;
    }

    public void getStat(){shipBoard.addBreakDoubleEngine(true);}
    public void destroy(){
        shipBoard.addBreakDoubleEngine(false);
        super.destroy();
    }
}
