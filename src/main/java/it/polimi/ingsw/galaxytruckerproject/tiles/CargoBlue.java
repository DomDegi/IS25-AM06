package it.polimi.ingsw.galaxytruckerproject.tiles;

public class CargoBlue extends CargoHold {
    boolean hazard;

    public CargoBlue(int totSpaces, Link north, Link east, Link west, Link south) {
        super(totSpaces, north, east, west, south);
        hazard = false;
    }
    public String toString() {

        return "Cargo Blue" +super.toString();
    }

}
