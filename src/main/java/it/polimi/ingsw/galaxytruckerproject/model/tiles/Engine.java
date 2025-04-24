package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Optional;

public abstract class Engine extends Tile{
    Direction direction;

    public Engine(Link north, Link east, Link south, Link west, int key) {
        super(north, east, south, west, key);
        south.connectorsType = Connectors.SMOOTH;
        direction = Direction.SOUTH;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public Engine(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, 0);
        south.connectorsType = Connectors.SMOOTH;
        direction = Direction.SOUTH;
    }

    @Override
    public String toString() {
        return "direction:"+direction.toString()+" " + super.toString();
    }

    public void rotate(){
        super.rotate();
        int i = this.direction.ordinal()+1;
        if(i>3)
            i=0;
        this.direction = Direction.values()[i];
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public boolean isCorrect() {
        Optional<Tile> other=null;
        Optional<Tile>[][] tileTable = shipBoard.getTilesTable();
        if(this.direction!= Direction.SOUTH ) return false;
        //check south tile
        else if(this.coordinates.getX()!=4){
            other = tileTable[this.coordinates.getX()+1][this.coordinates.getY()];
            if(other.isPresent() && other.get().fillable()){
                return false;
            }
        }

        return super.isCorrect();
    }
}
