package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public abstract class Cabin extends Tile {
    protected int crew;
    public Cabin(Link north, Link east, Link south, Link west, int key) {
        super(north, east, south, west,key);
    }
    //METODO COSTRUTTORE PER IL TESTING
    public Cabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west,0);
    }
    public int getCrew() {
        return crew;
    }
}
