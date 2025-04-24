package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class CargoBlue extends CargoHold {

    public CargoBlue(int totSpaces, Link north, Link east, Link west, Link south,int key) {
        super(totSpaces, north, east, west, south, key);
        hazard = false;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public CargoBlue(int totSpaces, Link north, Link east, Link west, Link south) {
        super(totSpaces, north, east, west, south,0);
        hazard = false;
    }
    public String toString() {

        return "Cargo Blue" +super.toString();
    }

}
