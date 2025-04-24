package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class CargoRed extends CargoHold{


    public CargoRed(int totSpaces, Link north, Link east, Link west, Link south, int key) {
        super(totSpaces, north, east, west, south, key);
        hazard = true;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public CargoRed(int totSpaces, Link north, Link east, Link west, Link south) {
        super(totSpaces, north, east, west, south, 0);
        hazard = true;
    }

    @Override
    public String toString() {

        return "Cargo Red" +super.toString();
    }
}
