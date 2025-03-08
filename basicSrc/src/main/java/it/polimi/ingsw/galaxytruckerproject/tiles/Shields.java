package it.polimi.ingsw.galaxytruckerproject.tiles;

public class Shields extends Tile{
    Covarage coveredArea;
    public Shields() {
        super();
        this.coveredArea = Covarage.NordEast;
    }
    public void getStat() {
        //We need to implement Dashboard first
    }
    public void rotate(){
        super.rotate();
        int i = this.coveredArea.ordinal()+1;
        if(i>3)
            i=0;
        this.coveredArea = coveredArea.values()[i];
    }
}
