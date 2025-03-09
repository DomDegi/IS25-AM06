package it.polimi.ingsw.galaxytruckerproject.tiles;

public class Coordinates {
    int x; //row
    int y; //column
    public Coordinates(int x, int y){
        this.x=x;
        this.y=y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
