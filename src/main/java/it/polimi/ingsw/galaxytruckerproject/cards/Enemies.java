package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;

public abstract class Enemies extends Card {
    protected final int cannonStrength;

    public Enemies(int level, int requiredDays, int cannonStrength) {
        super(level, requiredDays);
        this.cannonStrength = cannonStrength;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract void initializeCard(Game game);

    @Override
    public abstract void executeCard(Game game, String playerName, String[] input);
}
