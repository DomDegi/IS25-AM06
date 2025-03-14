package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Cabin extends Tile {
    protected int crew;
    public Cabin(Link nord, Link east, Link south, Link west) {
        super(nord, east, south, west);
    }
    public int getCrew() {
        return crew;
    }



}
