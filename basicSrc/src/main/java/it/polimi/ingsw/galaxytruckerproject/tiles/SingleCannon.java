package it.polimi.ingsw.galaxytruckerproject.tiles;

public class SingleCannon extends Cannon {
    private float fireStrength;

    public SingleCannon() {
        super();
        if(direction == Direction.NORTH) {
            this.fireStrength = 1;
        }
        else {
            this.fireStrength=0.5f;
        }
    }

    @Override
    public String toString() {
        return "SingleCannon "+ super.toString();
    }

    public void getStat(){
        shipBoard.addBreakSingleCannonPower(fireStrength);
    }
    public void destroy(){
        shipBoard.addBreakSingleCannonPower(-fireStrength);
        super.destroy();
    }
}


