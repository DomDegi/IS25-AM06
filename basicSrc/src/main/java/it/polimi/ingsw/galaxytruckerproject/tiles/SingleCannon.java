package it.polimi.ingsw.galaxytruckerproject.tiles;

public class SingleCannon extends Cannon {
    private float fireStrength;

    public SingleCannon() {
        super();
        if(direction == Direction.Nord) {
            this.fireStrength = 1;
        }
        else {
            this.fireStrength = 0,5;
        }
    }

    public getStat(){
        //Check shipboard
    }


}


