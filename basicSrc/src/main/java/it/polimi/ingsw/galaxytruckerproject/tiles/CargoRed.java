package it.polimi.ingsw.galaxytruckerproject.tiles;

public class CargoRed extends CargoHold{
    boolean hazard;

    public CargoRed(int totSpaces, Link nord, Link east, Link west, Link south) {
        super(totSpaces, nord, east, west, south);
        hazard = true;
    }
}
