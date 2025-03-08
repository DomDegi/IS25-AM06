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
        if(this.direction!=Direction.Nord)
            return false;
        return super.isCorrect();
    }
    public void getStat(){
        //We need to implement Dashboard first
    }
}
