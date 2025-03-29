package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Cabin extends Tile {
    protected int crew;
    public Cabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }
    public int getCrew() {
        return crew;
    }
}
