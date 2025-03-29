package it.polimi.ingsw.galaxytruckerproject.tiles;

import java.util.Optional;

public abstract class Cannon extends Tile {
    protected Direction direction;

    public Cannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
        this.direction = Direction.NORTH;
    }

    @Override
    public String toString() {
        return "direction: "+direction.toString() + " " +super.toString();
    }

    public void getStat(){
        //OVERRIDE IN THE SUBCLASSES
    }
    public boolean isCorrect(){
        Optional<Tile> other= Optional.empty();
        Optional<Tile>[][] tileTable= shipBoard.getTilesTable();
        //checking the tile in the cannon direction
        if(this.direction == Direction.NORTH && this.coordinates.getX()!=0){
            other = tileTable[this.coordinates.getX()- 1][this.coordinates.getY()];
        }
        else if(this.direction == Direction.EAST && this.coordinates.getY()!=6){
            other = tileTable[this.coordinates.getX()][this.coordinates.getY()+1];
        }
        else if(this.direction == Direction.SOUTH && this.coordinates.getX()!=4){
            other = tileTable[this.coordinates.getX()+1][this.coordinates.getY()];
        }
        else if(this.direction == Direction.WEST && this.coordinates.getY()!=0){
            other = tileTable[this.coordinates.getX()][this.coordinates.getY()-1];
        }
        if(other.isPresent() && !(other.get() instanceof VoidTile)){
            return false;
        }
        return super.isCorrect();
    }

    public void rotate(){
        super.rotate();
        int i=this.direction.ordinal()+1;
        if(i>3){
            i=0;
        }
        this.direction = Direction.values()[i];
    }
}
