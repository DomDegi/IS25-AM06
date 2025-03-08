package it.polimi.ingsw.galaxytruckerproject.cards;

import java.util.ArrayList;

public abstract class Card {
    protected final int level;
    protected final int requiredDays;

    public Card(int level, int requiredDays) {
        this.level = level;
        this.requiredDays = requiredDays;
    }

    public abstract void executeCard(FlightBoard flightBoard);

    public int getLevel() {
        return level;
    }

    public int getRequiredDays() {
        return requiredDays;
    }
}