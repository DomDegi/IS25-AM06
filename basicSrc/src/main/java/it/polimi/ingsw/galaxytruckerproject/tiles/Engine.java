package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Engine extends Tile{
    Direction direction;

    public void rotate(){
        super.rotate();
        int i = this.direction.ordinal()+1;
        if(i>3)
            i=0;
        this.direction = Direction.values()[i];
    }

    @Override
    public boolean isCorrect() {
        Tile other=null;
        Tile[][] tileTable = shipBoard.getTilesTable();
        if(this.direction!= Direction.SOUTH ) return false;

        //check south tile
        else if(this.coordinates.getX()!=4){
            other = tileTable[this.coordinates.getX()+1][this.coordinates.getY()];
            if(other!=null && !(other instanceof VoidTile)){
                return false;
            }
        }

        return super.isCorrect();
    }
}
