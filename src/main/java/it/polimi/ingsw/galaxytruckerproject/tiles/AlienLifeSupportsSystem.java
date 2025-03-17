package it.polimi.ingsw.galaxytruckerproject.tiles;

public class AlienLifeSupportsSystem extends Tile{
    CrewType alienType;

    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west, CrewType alienType) {
        super(north,east,south,west);
        this.alienType = alienType;
    }
    public CrewType getAlienType() {
        return alienType;
    }
    @Override
    public String toString() {
        return "AlienLifeSupportsSystem alienType=" + alienType +" " + super.toString();
    }
}
