package it.polimi.ingsw.galaxytruckerproject.cards;

import java.util.ArrayList;

public abstract class Card {
    private final int level;
    private final int requiredDays;

    public Card(int level, int requiredDays) {
        this.level = level;
        this.requiredDays = requiredDays;
    }

    public abstract void executeCard(ArrayList<Player> listOfPlayers);
}