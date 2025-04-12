package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class Pipe extends Tile{
    public Pipe(Link north, Link east, Link south, Link west, int key){
        super(north,east,south,west,key);
    }

    public String toString() {
        return "Pipe" + " " + super.toString();
    }
}
