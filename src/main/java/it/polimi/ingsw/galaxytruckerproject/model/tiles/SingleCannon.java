package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class SingleCannon extends Cannon {
    private float fireStrength;

    public SingleCannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
        if(direction == Direction.NORTH) {
            this.fireStrength = 1;
        }
        else {
            this.fireStrength= 0.5F;
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
        fireStrength=0;
        super.destroy();
    }

    public Direction getDirection(){
        return direction;
    }

    //no real meaning used only in large meteor, negative return only for single cannon
    public int getStrength(){
        if(direction == Direction.NORTH)
            return -2;
        return -1;
    }

    @Override
    public void rotate(){
        super.rotate();
        if (this.direction == Direction.NORTH)
            this.fireStrength = 1;
        else
            this.fireStrength=0.5f;
    }
}


