package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Tile {
    private Link nord;
    private Link east;
    private Link south;
    private Link west;
    private boolean destroyed = false;
    private Coordinates coordinates;
    protected ShipBoard shipBoard;

    public Tile(){
        this.nord=nord;
        this.east=east;
        this.south=south;
        this.west=west;
    }
    //GETTER METHODS LINKS
    public Link getNord(){
        return nord;
    }
    public Link getEast(){
        return east;
    }
    public Link getSouth(){
        return south;
    }
    public Link getWest(){
        return west;
    }

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

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public void destroy(){
        destroyed = true;
    }



}
