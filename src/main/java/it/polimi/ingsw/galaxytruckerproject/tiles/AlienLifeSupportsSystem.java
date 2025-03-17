package it.polimi.ingsw.galaxytruckerproject.tiles;

public class AlienLifeSupportsSystem extends Tile{
    CrewType alienType;

    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }

    public CrewType getAlienType() {
        return alienType;
    }
}
