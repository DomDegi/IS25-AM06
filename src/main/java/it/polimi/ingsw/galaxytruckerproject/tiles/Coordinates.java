package it.polimi.ingsw.galaxytruckerproject.tiles;

import java.util.Objects;

public class Coordinates {
    int x; //row
    int y; //column
    public Coordinates(int x, int y){
        this.x=x;
        this.y=y;
    }
    public String toString(){
        return "("+x+";"+y+")";
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void set(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void print(){
        System.out.print(" {"+x+";"+y+"} ");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates that = (Coordinates) obj;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
