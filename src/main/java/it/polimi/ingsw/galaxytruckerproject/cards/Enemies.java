package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameInterface;

public abstract class Enemies extends Card {
    protected final int cannonStrength;

    public Enemies(int level, int requiredDays, int cannonStrength) {
        super(level, requiredDays);
        this.cannonStrength = cannonStrength;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract void initializeCard(GameInterface game);

    @Override
    public abstract void executeCard(Game game, String playerName, String[] input);
}
