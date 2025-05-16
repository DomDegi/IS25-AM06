package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

public abstract class Card implements Serializable {
    protected int id;
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

    public void notifyModifiedTiles (String playerName, ArrayList<Tile> tiles) {
        ArrayList<Tile> sendableTiles = new ArrayList<>();
        for (Tile tile : tiles) {
            sendableTiles.add(tile.send());
        }
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyModifiedTiles(playerName, sendableTiles);
            } catch (Exception ignored) {}
        }
    }

    public void notifyBrokenTiles(String playerName, ArrayList<Coordinates> tiles) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyBrokenTile(playerName, tiles);
            } catch (Exception ignored) {}
        }
    }

    public void notifyMovement (Player player) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyPlayerMovement(player.getPlayerName(), player.getPlayerColor(), player.getPlayerPosition(), player.getPlayerRanking());
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

    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove){}

    public void useBatteries(String playerName, ArrayList<Coordinates> batteries){}

    public void rollTheDices(String playerName) {}

    public ArrayList<Goods> getChosenPlanets(int index) {
        return null;
    }

    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices){}

    //Getter methods needed for view
    public int getLevel() {
        return level;
    }
    public int getRequiredDays() { return requiredDays; }
    public int getGainedCredits() { return 0; }
    public int getCrewNumber(){return 0;}
    public ArrayList<Goods> getGoodsList(String playerName){return null;}
    public int getGoodsPenalty(){return 0;}
    public ArrayList<Projectile> getListOfProjectiles() { return null;}
    public float getEnemiesFirePower() {return 0f;}
    public LinkedHashMap<ChallengeType, Penalty> getChallenges() {return null;}
    public ArrayList<Planet> getListOfPlanets() {
        return null;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }
    @Override
    public String toString() {
        return "id: " + id + " level: " +  level + ", required days: " + requiredDays;
    }

}