package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
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
    protected GameInterface game = null;

    //This attribute is null until the card is initialized
    protected Map<String, VirtualView> viewsMap = new HashMap<>();

    public Card(int level, int requiredDays) {
        this.level = level;
        this.requiredDays = requiredDays;
    }

    public abstract void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap);

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

    public void notifyModifiedTiles (String playerName, ArrayList<Tile> tiles) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyModifiedTiles(playerName, tiles);
            } catch (Exception ignored) {}
        }
    }

    public void notifyMovement (Player player) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyPlayerMovement(player.getPlayerName(), player.getPlayerPosition(), player.getPlayerRanking());
            } catch (Exception ignored) {}
        }
    }

    public void notifyGainedCredits (String playerName, int credits) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyGainedCredits(playerName, credits);
            } catch (Exception ignored) {}
        }
    }

    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse){}

    public void engineChoice(String playerName, int numDoubleEngine, ArrayList<Coordinates> batteriesToUse){}

    public void manageGoods(String playerName, int clientCredits,  ArrayList<CargoHold> updatedCargos){}

    public void choice(String playerName, boolean decision){}

    public void planetChoice(String playerName, int planet){}

    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove){}


    @Override
    public String toString() {
        return "level: " +  level + ", required days: " + requiredDays;
    }
}