package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.Map;

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
    public abstract void initializeCard(Game game, Map<String, ViewInterface> viewsMap);

    @Override
    public abstract void executeCard(Message message);
}
