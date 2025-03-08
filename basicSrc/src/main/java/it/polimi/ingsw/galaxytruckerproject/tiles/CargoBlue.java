package it.polimi.ingsw.galaxytruckerproject.tiles;

public class CargoBlue extends CargoHold {
    boolean hazard;

    public CargoBlue(int totSpaces, Link nord, Link east, Link west, Link south) {
        super(totSpaces, nord, east, west, south);
        hazard = false;
    }
}
