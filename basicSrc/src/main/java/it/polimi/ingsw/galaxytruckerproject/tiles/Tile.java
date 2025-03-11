package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Tile {
    protected Link nord;
    protected Link east;
    protected Link south;
    protected Link west;
    protected boolean destroyed = false;
    protected Coordinates coordinates;
    protected ShipBoard shipBoard;

    public Tile(Link nord, Link east, Link south, Link west){
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

        Tile other;
        Tile[][] tileTable= shipBoard.getTilesTable();
        //Check north
        if(this.coordinates.getX()!=0) {
            other = tileTable[this.coordinates.getX()-1][this.coordinates.getY()];
            if(other!=null && !(other instanceof VoidTile) && !nord.isConnected(other.getSouth()) ) {
                return false;
            }
        }
        //Check east
        if(this.coordinates.getY()!=6) {
            other = tileTable[this.coordinates.getX()][this.coordinates.getY()+1];
            if(other!=null && !(other instanceof VoidTile) && !east.isConnected(other.getWest())) {
                return false;
            }
        }
        /*//Check south
        if(this.coordinates.getX()!=4) {
            other = tileTable[this.coordinates.getX()+1][this.coordinates.getY()];
            if(other!=null && !(other instanceof VoidTile) && !south.isConnected(other.getNord())) {
                return false;
            }
        }
        //Check west

        if(this.coordinates.getY()!=0) {
            other = tileTable[this.coordinates.getX()][this.coordinates.getY()-1];
            if(other!=null && !(other instanceof VoidTile) && !west.isConnected(other.getEast())) {
                return false;
            }
        }*/
        //Checked
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
        return;
    }

}
