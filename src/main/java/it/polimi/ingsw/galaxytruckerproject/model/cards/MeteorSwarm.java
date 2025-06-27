package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Defense;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Represents the Meteor Swarm event card that simulates a series of meteors
 * hitting each player's ship.
 */
public class MeteorSwarm extends Card {

    /** List of meteors (projectiles) that will be applied in sequence. */
    private final ArrayList<Projectile> listOfMeteors;

    /** List of players currently in flight. */
    private List<Player> inFlightPlayers;

    /** Map of players to their currently active projectile penalty. */
    private final Map<Player, ProjectilePenalty> activePenalties = new HashMap<>();

    /** Current result of the dice roll. */
    private int currentDiceRoll;

    /** Reference to the game logic. */
    private GameInterface game;

    /** Index of the currently processed meteor. */
    private int currentMeteorIndex = 0;

    /** Player who is currently expected to roll the dice. */
    Player currentPlayerRolling = null;

    /**
     * Constructor used by Jackson for deserialization.
     *
     * @param level         The difficulty level of the card.
     * @param listOfMeteors The list of meteors to simulate.
     * @param filePath      The path to the image for the card.
     */
    @JsonCreator
    public MeteorSwarm(@JsonProperty("level") int level,
                       @JsonProperty("listOfMeteors") ArrayList<Projectile> listOfMeteors,
                       @JsonProperty("imagePath") String filePath) {
        super(level, 0, filePath);
        this.listOfMeteors = listOfMeteors;
        this.currentDiceRoll = 0;
    }

    /**
     * Constructor without image path.
     *
     * @param level         The difficulty level of the card.
     * @param listOfMeteors The list of meteors to simulate.
     */
    public MeteorSwarm(@JsonProperty("level") int level,
                       @JsonProperty("listOfMeteors") ArrayList<Projectile> listOfMeteors) {
        super(level, 0);
        this.listOfMeteors = listOfMeteors;
        this.currentDiceRoll = 0;
    }

    /**
     * Initializes the card and starts the meteor event.
     *
     * @param game      Reference to the game instance.
     * @param viewsMap  Map of player names to their corresponding virtual views.
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.inFlightPlayers = new ArrayList<>(game.getListOfInFlightPlayers());
        this.currentMeteorIndex = 0;
        startMeteorPhase();
    }

    /**
     * Starts the phase for the current meteor, asking the first player to roll the dice.
     */
    private void startMeteorPhase() {
        if (currentMeteorIndex >= listOfMeteors.size()) {
            game.endCardEvent();
            return;
        }
        activePenalties.clear();
        currentDiceRoll = 0;
        currentPlayerRolling = inFlightPlayers.getFirst();
        VirtualView currentView = viewsMap.get(currentPlayerRolling.getPlayerName());
        try {
            currentView.setClientState(ClientState.ROLL_DICE);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Applies the current meteor to all in-flight players.
     */
    public void applyMeteorToPlayers() {
        Projectile currentMeteor = listOfMeteors.get(currentMeteorIndex);
        for (Player player : inFlightPlayers) {
            ArrayList<Projectile> singleMeteorCopy = new ArrayList<>();
            singleMeteorCopy.add(currentMeteor);
            ProjectilePenalty newPenalty = new ProjectilePenalty(singleMeteorCopy);
            newPenalty.setDiceRoll(currentDiceRoll);
            if (newPenalty.initializePenalty(game, viewsMap.get(player.getPlayerName()), player)) {
                activePenalties.put(player, newPenalty);
            } else {
                ArrayList<Coordinates> toDestroy = new ArrayList<>();
                Coordinates destroyedTile = newPenalty.getDestroyedTile();
                if (destroyedTile != null && (newPenalty.getBranch() == null || newPenalty.getBranch().size() < 2)) {
                    toDestroy.add(destroyedTile);
                    notifyBrokenTiles(player.getPlayerName(), toDestroy);
                }
            }
        }
        prepareNextMeteor();
    }

    /**
     * Called when a player rolls the dice.
     *
     * @param playerName Name of the player rolling.
     */
    @Override
    public void rollTheDices(String playerName) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayerRolling.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        diceRoll();
        applyMeteorToPlayers();
    }

    /**
     * Notifies all views of the current dice roll.
     */
    public void notifyDiceRoll() {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.showDiceRoll(currentDiceRoll);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Handles battery usage by a player to defend from a meteor.
     *
     * @param playerName Name of the player.
     * @param batteries  Coordinates of batteries to be used.
     */
    @Override
    public void useBatteries(String playerName, ArrayList<Coordinates> batteries) {
        Player player = game.identifyPlayerByName(playerName);
        if (!activePenalties.containsKey(player) || activePenalties.get(player).getDefenseStatus() != Defense.CHOOSETOUSEBATTERY) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        Tile batteryComponent = activePenalties.get(player).playerUsesBatteryToDefend(player, batteries);
        if (batteries.isEmpty() && batteryComponent == null) {
            activePenalties.get(player).hitOrMiss(viewsMap.get(playerName), player);
            Coordinates destroyedTile = activePenalties.get(player).getDestroyedTile();
            if ((activePenalties.get(player).getBranch() == null || activePenalties.get(player).getBranch().size() < 2) && destroyedTile != null) {
                ArrayList<Coordinates> toRemove = new ArrayList<>();
                toRemove.add(destroyedTile);
                notifyBrokenTiles(playerName, toRemove);
                if (!activePenalties.get(player).initializePenalty(game, viewsMap.get(playerName), player)) {
                    playerCompletedMeteor(player);
                }
            }
            return;
        }
        ArrayList<Tile> modifiedTiles = new ArrayList<>();
        modifiedTiles.add(batteryComponent);
        if (batteryComponent != null) {
            notifyModifiedTiles(playerName, modifiedTiles);
            if (!activePenalties.get(player).initializePenalty(game, viewsMap.get(playerName), player)) {
                playerCompletedMeteor(player);
            }
        } else {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
        }
    }

    /**
     * Handles a player's decision on which branch of their ship to keep.
     *
     * @param playerName     Name of the player.
     * @param branchChoices  List of coordinates the player chooses to maintain.
     */
    @Override
    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices) {
        Player player = game.identifyPlayerByName(playerName);
        if (!activePenalties.containsKey(player) || activePenalties.get(player).getBranch() == null) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> removedTiles = activePenalties.get(player).chooseToMaintain(player, branchChoices);
        if (!removedTiles.isEmpty()) {
            notifyBrokenTiles(playerName, removedTiles);
        }
        if (!activePenalties.get(player).initializePenalty(game, viewsMap.get(playerName), player)) {
            playerCompletedMeteor(player);
        }
    }

    /**
     * Called when a player has completed handling the current meteor.
     *
     * @param player The player who completed the penalty.
     */
    public void playerCompletedMeteor(Player player) {
        activePenalties.remove(player);
        prepareNextMeteor();
    }

    /**
     * Prepares the next meteor if no penalties are active.
     */
    public void prepareNextMeteor() {
        if (activePenalties.isEmpty()) {
            currentMeteorIndex++;
            startMeteorPhase();
        }
    }

    /**
     * Returns a string representation of the card and its meteors.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("MeteorSwarm: ").append(super.toString()).append(" ");
        for (Projectile projectile : listOfMeteors) {
            string.append(projectile.toString());
        }
        return string.toString();
    }

    /**
     * Rolls a random value for the meteor attack and notifies views.
     */
    public void diceRoll() {
        Random random = new Random();
        this.currentDiceRoll = 2 + random.nextInt(11);
        notifyDiceRoll();
    }

    /**
     * Sets the dice roll manually and immediately applies the meteor (for testing).
     *
     * @param diceRoll The value to set as dice roll.
     */
    public void setDiceRoll(int diceRoll) {
        this.currentDiceRoll = diceRoll;
        applyMeteorToPlayers();
    }

    /**
     * Returns the list of projectiles (meteors) in this card.
     */
    @Override
    public ArrayList<Projectile> getListOfProjectiles() {
        return listOfMeteors;
    }

    /**
     * Handles player disconnection during meteor phase.
     *
     * @param playerName Name of the disconnected player.
     */
    @Override
    public void playerDisconnected(String playerName) {
        Player player = game.identifyPlayerByName(playerName);
        if (player != null && activePenalties.containsKey(player)) {
            if (activePenalties.get(player).initializePenalty(game, viewsMap.get(playerName), game.identifyPlayerByName(playerName)))
                System.out.println("Error, player is disconnected but the penalty isn't completed");
            activePenalties.remove(player);
            prepareNextMeteor();
        }
    }

    /**
     * Handles the scenario when a player lands on the 'Smugglers' card and resolves
     * the corresponding sequence of events based on game conditions.
     *
     * @param playerName the name of the player who landed on the card
     */
    @Override
    public void playerLanded(String playerName) {
        Player player = game.identifyPlayerByName(playerName);
        if (player != null && activePenalties.containsKey(player)) {
            if (activePenalties.get(player).initializePenalty(game, viewsMap.get(playerName), game.identifyPlayerByName(playerName)))
                System.out.println("Error, player is disconnected but the penalty isn't completed");
            activePenalties.remove(player);
            prepareNextMeteor();
        }
    }

    public int getDiceRoll() {
       return this.currentDiceRoll;
    }
}
