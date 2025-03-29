package it.polimi.ingsw.galaxytruckerproject.tiles;

public class CargoRed extends CargoHold{
    boolean hazard;

    public CargoRed(int totSpaces, Link north, Link east, Link west, Link south) {
        super(totSpaces, north, east, west, south);
        hazard = true;
    }

    @Override
    public String toString() {

        return "Cargo Red" +super.toString();
    }
}
