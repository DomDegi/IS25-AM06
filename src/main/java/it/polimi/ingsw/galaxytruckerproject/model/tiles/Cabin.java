package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public abstract class Cabin extends Tile {
    protected int crew;
    public Cabin(Link north, Link east, Link south, Link west, int key) {
        super(north, east, south, west,key);
    }
    public int getCrew() {
        return crew;
    }
}
