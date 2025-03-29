package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class Pipe extends Tile{
    public Pipe(Link north, Link east, Link south, Link west){
        super(north,east,south,west);
    }

    public String toString() {
        return "Pipe" + " " + super.toString();
    }
}
