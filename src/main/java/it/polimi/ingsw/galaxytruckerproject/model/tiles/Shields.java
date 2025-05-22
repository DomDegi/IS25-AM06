package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.Objects;

public class Shields extends Tile{
    Coverage coveredArea;
    public Shields(Link north, Link east, Link south, Link west,String imagePath,int rotation, int key) {
        super(north, east, south, west,imagePath,rotation, key);
        this.coveredArea = Coverage.NORTH_EAST;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public Shields(Link north, Link east, Link south, Link west) {
        super(north, east, south, west,null,0, 0);
        this.coveredArea = Coverage.NORTH_EAST;
    }

    public void rotate(){
        super.rotate();
        int i = this.coveredArea.ordinal()+1;
        if(i>3)
            i=0;
        this.coveredArea = coveredArea.values()[i];
    }

    //we don't need a NumberOfCoverageShields in ShipBoard to update because we can get it from the size of the ArrayList
    @Override
    public String toString() {
        return "Shield coverage:"+ coveredArea.toString() +super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }
    @Override
    public String toString1(){
        if(getCoveredArea()==Coverage.NORTH_EAST||getCoveredArea()==Coverage.NORTH_WEST) {
            return " █  " + getNorth() + " █ ";
        }else if(getCoveredArea()==Coverage.SOUTH_EAST) {
            if (getKey() >= 100)
                return  getKey() +" " + getNorth() + " █ ";
            else if (getKey() >= 10 && getKey() < 100)
                return " " +getKey() + " " + getNorth() + " █ ";
            else
                return " " + getKey() + "  " + getNorth() + " █ ";
        }else if(getCoveredArea()==Coverage.SOUTH_WEST) {
            if (getKey() >= 100)
                return " █ " + getNorth() +" " + getKey();
            else if (getKey() >= 10 && getKey() < 100)
                return " █ " + getNorth() +" " + getKey() + " ";
            else
                return " █ " + getNorth() + "  " + getKey() + " ";
        }else
            return "   "+getNorth()+"   ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" SH "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if(getCoveredArea()==Coverage.NORTH_WEST) {
            if (getKey() >= 100)
                return " █ " + getSouth() +" " + getKey();
            else if (getKey() >= 10 && getKey() < 100)
                return " █ " + getSouth() + " " +getKey() + " ";
            else
                return " █ " + getSouth() + "  " + getKey() + " ";
        }else if(getCoveredArea()==Coverage.NORTH_EAST) {
            if (getKey() >= 100)
                return  getKey() +" " + getSouth() + " █ ";
            else if (getKey() >= 10 && getKey() < 100)
                return " " +getKey() + " " + getSouth() + " █ ";
            else
                return " " + getKey() + " " + getSouth() + "  █ ";
        } else if (getCoveredArea()==Coverage.SOUTH_EAST||getCoveredArea()==Coverage.SOUTH_WEST) {
            return " █ " + getSouth() + "  █ ";
        } else {
            if (getKey() >= 100)
                return "   " + getSouth() +" " + getKey();
            else if (getKey() >= 10 && getKey() < 100)
                return "   " + getSouth() +" " + getKey() + " ";
            else
                return "   " + getSouth() + "  " + getKey() + " ";
        }
    }

    //we don't need a NumberOfCoverageShields to uploaded because we can get it from the size of the ArrayList
    public void getStat() {shipBoard.getCoverageShields().add(this.coveredArea);}

    public Coverage getCoveredArea() {return this.coveredArea;}
    public void destroy(){
        for(Coverage cov : shipBoard.getCoverageShields()){
            if(cov.equals(this.coveredArea)){
                shipBoard.getCoverageShields().remove(cov);
                super.destroy();
                return;
            }
        }
    }

    @Override
    public String toStringData() {
        return "SH "+ key + " " + north.toString() + " " + east.toString() + " " + south.toString() + " " + west.toString() + " " + coveredArea.toStringData();
    }

    public Shields() {}

    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.coveredArea = Coverage.fromStringData(attributes[6]);
        if (Objects.equals(attributes[6], "NONE")) {
            System.out.println("Why this shield has no coverage?");
            throw new IllegalArgumentException();
        }
    }
}
