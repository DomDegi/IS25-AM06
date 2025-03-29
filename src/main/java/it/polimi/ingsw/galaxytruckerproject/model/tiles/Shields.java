package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class Shields extends Tile{
    Coverage coveredArea;
    public Shields(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
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
        return "Shield coverage:"+ coveredArea.toString() +super.toString();
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
}
