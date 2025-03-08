package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Tile {
    private Link nord;
    private Link east;
    private Link south;
    private Link west;
    private Coordinates coordinates;
    private ShipBoard shipBoard;

    public boolean isCorrect(){
        //  We need to remember the LinkCheck
        return true;
    }
    public void rotate(){
        Link tmp=nord;
        nord=west;
        Link tmp2=east;
        east=tmp;
        west=south;
        south=east;
    }
    public Tile(){
        this.nord=nord;
        this.east=east;
        this.south=south;
        this.west=west;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

}
