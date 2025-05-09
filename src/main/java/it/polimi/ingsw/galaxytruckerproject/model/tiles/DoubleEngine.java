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
        return "DoubleEngine"+ super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }

    @Override
    public String toString1(){
        return "    "+getNorth()+"   ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" DE "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if (getKey() >= 100)
            return "   " + getSouth() +" " + getKey();
        else if (getKey() >= 10 && getKey() < 100)
            return "   " + getSouth() + " " +getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
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
