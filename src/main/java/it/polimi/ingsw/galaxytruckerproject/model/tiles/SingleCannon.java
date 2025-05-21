package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class SingleCannon extends Cannon {
    private float fireStrength;

    public SingleCannon(Link north, Link east, Link south, Link west,String imagePath, int rotation, int key) {
        super(north, east, south, west,imagePath,rotation,key);
        updateFireStrength();
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public SingleCannon(Link north, Link east, Link south, Link west) {
        super(north, east, south, west,null,0,0);
        if(direction == Direction.NORTH) {
            this.fireStrength = 1;
        }
        else {
            this.fireStrength= 0.5F;
        }
    }
    @Override
    public String toString() {
        return "SingleCannon "+ super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }

    @Override
    public String toString1(){
        switch (getDirection()){
            case NORTH->{
                return " ↑  "+getNorth()+"   ";
            }
            case EAST->{
                return " →  "+getNorth()+"   ";
            }
            case SOUTH->{
                return " ↓  "+getNorth()+"   ";
            }
            case WEST->{
                return " ←  "+getNorth()+"   ";
            }
        }
        return "    "+getNorth()+"   ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" CC "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if (getKey() >= 100)
            return "   " + getSouth() +" " + getKey();
        else if (getKey() >= 10 && getKey() < 100)
            return "   " + getSouth() +" " + getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
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
        updateFireStrength();
    }

    @Override
    public String toStringData() {
        return "SC " + key + " " + north.toString() + " " + east.toString() + " " + south.toString() + " " + west.toString() + " " + direction.toString();
    }

    public SingleCannon(){}

    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        updateFireStrength();
    }

    void updateFireStrength () {
        if (this.direction == Direction.NORTH)
            this.fireStrength = 1;
        else
            this.fireStrength=0.5f;
    }
}


