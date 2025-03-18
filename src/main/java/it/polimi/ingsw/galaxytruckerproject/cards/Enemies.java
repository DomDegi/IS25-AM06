package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;

public abstract class Enemies extends Card {
    protected final int cannonStrength;

    public Enemies(int level, int requiredDays, int cannonStrength) {
        super(level, requiredDays);
        this.cannonStrength = cannonStrength;
    }

    public int getCannonStrength(){
        return cannonStrength;
    }

}
