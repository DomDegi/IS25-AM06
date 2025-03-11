package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Cabin extends Tile {
    int crew;
    public Cabin(Link nord, Link east, Link south, Link west) {
        super(nord, east, south, west);
    }
}
