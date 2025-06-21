package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract base class representing a card in the game.
 * Cards define various events or encounters during gameplay.
 */
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
        @JsonSubTypes.Type(value = Epidemic.class, name = "Epidemic")
})
public abstract class Card implements Serializable {

    /** Card identifier */
    protected int id;

    /** Level of the card (difficulty or stage) */
    protected final int level;

    /** Days required to pass this card */
    protected final int requiredDays;

    /** Reference to the game model */
    protected GameInterface game = null;

    /** Optional path to the card's image or file representation */
    protected String filePath;

    /** Map of player names to their corresponding VirtualView */
    protected Map<String, VirtualView> viewsMap = new HashMap<>();

    /**
     * Basic constructor.
     * @param level the level of the card
     * @param requiredDays days required to complete the card
     */
    public Card(int level, int requiredDays) {
        this.level = level;
        this.requiredDays = requiredDays;
    }

    /**
     * Constructor including file path.
     * @param level the level of the card
     * @param requiredDays days required to complete the card
     * @param filePath path to image or data file
     */
    @JsonCreator
    public Card(int level, int requiredDays, String filePath) {
        this.level = level;
        this.requiredDays = requiredDays;
        this.filePath = filePath;
    }

    /**
     * Initialize card logic with game reference and views.
     * @param game the game interface
     * @param viewsMap map of views
     */
    public abstract void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap);

    /**
     * Notifies all views of modified tiles.
     * @param playerName name of the player
     * @param tiles modified tiles
     */
    public void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) {
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

    /**
     * Notifies all views of broken tiles.
     * @param playerName name of the player
     * @param tiles coordinates of broken tiles
     */
    public void notifyBrokenTiles(String playerName, ArrayList<Coordinates> tiles) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyBrokenTile(playerName, tiles);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Notifies all views of player movement.
     * @param player the player object
     */
    public void notifyMovement(Player player) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyPlayerMovement(player.getPlayerName(), player.getPlayerColor(), player.getPlayerPosition(), player.getPlayerRanking());
            } catch (Exception ignored) {}
        }
    }

    /**
     * Notifies all views that a player has gained credits.
     * @param playerName name of the player
     * @param credits amount of credits gained
     */
    public void notifyGainedCredits(String playerName, int credits) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyGainedCredits(playerName, credits);
            } catch (Exception ignored) {}
        }
    }

    // Optional override hooks for specific cards
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {}
    public void engineChoice(String playerName, int numDoubleEngine, ArrayList<Coordinates> batteriesToUse) {}
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {}
    public void choice(String playerName, boolean decision) {}
    public void planetChoice(String playerName, int planet) {}
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove) {}
    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove) {}
    public void useBatteries(String playerName, ArrayList<Coordinates> batteries) {}
    public void rollTheDices(String playerName) {}
    public ArrayList<Goods> getChosenPlanets(int index) { return null; }
    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices) {}

    /**
     * Notifies views that the player is a victim of a penalty.
     * @param playerName name of the victim player
     */
    public void notifyVictim(String playerName) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.victimOfThePenalty(playerName);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void playerDisconnected(String playerName) {}

    // Getters for views or external systems
    public int getLevel() { return level; }
    public int getRequiredDays() { return requiredDays; }
    public int getGainedCredits() { return 0; }
    public int getCrewNumber() { return 0; }
    public ArrayList<Goods> getGoodsList(String playerName) { return null; }
    public void setGoodsList(String playerName, int planet) {}
    public int getGoodsPenalty() { return 0; }
    public ArrayList<Projectile> getListOfProjectiles() { return null; }
    public float getEnemiesFirePower() { return 0f; }
    public LinkedHashMap<ChallengeType, Penalty> getChallenges() { return null; }
    public ArrayList<Planet> getListOfPlanets() { return null; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Penalty getPenalty() { return null; }
    public String getFilePath() { return filePath; }

    @Override
    public String toString() {
        return "id: " + id + " level: " + level + ", required days: " + requiredDays;
    }
}
