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
        return "Pipe" + " " + super.toString();
    }
}
