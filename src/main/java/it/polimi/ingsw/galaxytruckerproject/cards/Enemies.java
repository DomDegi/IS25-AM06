package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.Game;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Slavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = Smugglers.class, name = "Smugglers"),
        @JsonSubTypes.Type(value = Pirates.class, name = "Pirates")
})

public abstract class Enemies extends Card {
    protected final int cannonStrength;

    public Enemies(int level, int requiredDays, int cannonStrength) {
        super(level, requiredDays);
        this.cannonStrength = cannonStrength;
    }

    @Override
    public String toString() {
        return super.toString() + "  cannonStrength: " + cannonStrength + " ";
    }

    @Override
    public abstract void initializeCard(Game game);

    @Override
    public abstract void executeCard(Game game, String playerName, String[] input);
}
