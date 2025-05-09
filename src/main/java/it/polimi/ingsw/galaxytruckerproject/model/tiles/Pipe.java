package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class Pipe extends Tile{
    public Pipe(Link north, Link east, Link south, Link west, int key){
        super(north,east,south,west,key);
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public Pipe(Link north, Link east, Link south, Link west){
        super(north,east,south,west,0);
    }
    public String toString() {
        return "Pipe" + " " + super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }
    @Override
    public String toString1(){
        return "    "+getNorth()+"   ";
    }
    @Override
    public String toString2(){
        return "  "+getWest()+"PP"+getEast()+" ";
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

}
