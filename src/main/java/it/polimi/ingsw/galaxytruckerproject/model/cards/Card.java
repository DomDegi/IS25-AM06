package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.HashMap;
import java.util.Map;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MeteorSwarm.class, name = "MeteorSwarm"),
        @JsonSubTypes.Type(value = AbandonedStation.class, name = "AbandonedStation"),
        @JsonSubTypes.Type(value = AbandonedShip.class, name = "AbandonedShip"),
        @JsonSubTypes.Type(value = CombatZone.class, name = "CombatZone"),
        @JsonSubTypes.Type(value = Enemies.class, name = "Enemies"),
        @JsonSubTypes.Type(value = OpenSpace.class, name = "OpenSpace"),
        @JsonSubTypes.Type(value = StarDust.class, name = "StarDust"),
        @JsonSubTypes.Type(value = Planets.class, name = "Planets"),
        @JsonSubTypes.Type(value = Epidemic.class, name ="Epidemic")
})

public abstract class Card {
    protected final int level;
    protected final int requiredDays;

    //This attribute is null until the card is initialized
    protected final Map<String, ViewInterface> viewsMap = new HashMap<>();

    public Card(int level, int requiredDays) {
        this.level = level;
        this.requiredDays = requiredDays;
    }

    public abstract void initializeCard(Game game, Map<String, ViewInterface> viewsMap);

    public abstract void executeCard(Game game, Message message);

    public int getLevel() {
        return level;
    }

    public void sendMessageToPlayer (ViewInterface playersView, String message) {
        playersView.showGenericMessage(message);
    }

    public void broadcastMessage (String message) {
        for (ViewInterface view : viewsMap.values()) {
            view.showGenericMessage(message);
        }
    }

    @Override
    public String toString() {
        return "level: " +  level + ", required days: " + requiredDays;
    }
}