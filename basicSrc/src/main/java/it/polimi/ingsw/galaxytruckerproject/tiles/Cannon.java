package it.polimi.ingsw.galaxytruckerproject.tiles;

public abstract class Cannon extends Tile {
    protected Direction direction;

    public Cannon() {
        super();
        this.direction = Direction.NORTH;

    }
    public void getStat(){
        //I need to access shipboard
    }
    public boolean isCorrect(){

        //Check Shipboard
        if(super.isCorrect() ){
            return true;
        }
        return false;
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
