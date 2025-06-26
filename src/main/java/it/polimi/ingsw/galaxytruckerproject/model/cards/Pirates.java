package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the "Pirates" enemy card.
 * Players must defeat pirates using cannon power; if they fail,
 * their ship may suffer damage through randomized projectile impacts.
 */
public class Pirates extends Enemies {

    /** List of projectiles used by the pirates to attack the player’s ship. */
    private final ArrayList<Projectile> listOfCannonShots;

    /** Credits rewarded to the player if they win the encounter. */
    private final int rewardCredits;

    /** Index of the current player in the list of in-flight players. */
    private int playerIndex = -1;

    /** The current player facing the pirates. */
    private Player currentPlayer = null;

    /** The virtual view associated with the current player. */
    private VirtualView currentView = null;

    /** Penalty object to handle projectile damage if the player loses. */
    private ProjectilePenalty penaltyIfLose;

    private ProjectilePenalty penaltySaved;

    private int i=1;

    /** Status of the encounter: 0 = undecided, 1 = win, -1 = lose. */
    private int won = 0;

    /**
     * Constructor used for JSON deserialization.
     *
     * @param level          the enemy threat level
     * @param requiredDays   days lost after victory
     * @param cannonStrength cannon strength to beat
     * @param rewardCredits  credits awarded upon victory
     * @param listOfShots    list of projectiles to apply on failure
     * @param filePath       image path for the card
     */
    @JsonCreator
    public Pirates(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("listOfShots") ArrayList<Projectile> listOfShots,
            @JsonProperty("imagePath") String filePath) {
        super(level, requiredDays, cannonStrength, filePath);
        this.rewardCredits = rewardCredits;
        this.listOfCannonShots = listOfShots;
    }

    /**
     * Constructor without image path.
     */
    public Pirates(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("listOfShots") ArrayList<Projectile> listOfShots) {
        super(level, requiredDays, cannonStrength);
        this.rewardCredits = rewardCredits;
        this.listOfCannonShots = listOfShots;
        this.penaltySaved=penaltyIfLose;

    }

    /**
     * Returns a string representation of the Pirates card.
     *
     * @return string with basic info and projectile list
     */
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Pirates: ").append(super.toString()).append("rewardCredits: ").append(rewardCredits).append(" ");
        for (Projectile projectile : listOfCannonShots) {
            string.append(projectile.toString());
        }
        return string.toString();
    }

    /**
     * Initializes the card and begins the encounter sequence.
     *
     * @param game     the game instance
     * @param viewsMap map of player names to virtual views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    /**
     * Proceeds to the next player in the encounter.
     */
    public void nextPlayer() {
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = currentPlayer.getPlayerName();
        this.currentView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
        if (singleCannonPower > 0) {
            singleCannonPower += currentPlayer.getShipBoard().getNumPurpleAliens() * 2;
        }
        penaltyIfLose = new ProjectilePenalty(listOfCannonShots);
        won = 0;
        if (currentPlayer.IsDisconnected()) {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            } else if (singleCannonPower == cannonStrength) {
                nextPlayer();
            } else {
                won = -1;
                if (penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) return;
                nextPlayer();
            }
        } else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                try {
                    currentView.setClientState(ClientState.ACTION);
                } catch (Exception ignored) {}
            } else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getNumBatteries() == 0) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                } else {
                    won = -1;
                    penaltyIfLose.initializePenalty(game, currentView, currentPlayer);
                }
            } else {
                try {
                    currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
                } catch (Exception ignored) {}
            }
        }
    }

    /**
     * Handles the player’s decision after winning.
     *
     * @param playerName name of the player
     * @param decision   true to take the reward, false to skip
     */
    @Override
    public void choice(String playerName, boolean decision) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || won != 1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        if (decision) {
            currentPlayer.gainCredit(rewardCredits);
            notifyGainedCredits(currentPlayer.getPlayerName(), currentPlayer.getCredit());
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            notifyMovement(currentPlayer);
        }
        game.endCardEvent();
    }

    /**
     * Handles the player's cannon usage in combat.
     */
    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        Map<Float, ArrayList<Tile>> returned = player.useCannons(doubleCannonPower, batteriesToUse);
        if (returned == null) {
            float power = player.getShipBoard().getSingleCannonPower();
            if (power > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            } else if (power == cannonStrength) {
                nextPlayer();
            } else {
                won = -1;
                if (penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) return;
                nextPlayer();
            }
            return;
        }
        notifyModifiedTiles(playerName, returned.values().iterator().next());
        float cannonPower = returned.keySet().iterator().next();
        if (cannonPower > cannonStrength) {
            won = 1;
            if (currentPlayer.IsDisconnected()) {
                choice(playerName, false);
            } else {
                try {
                    currentView.setClientState(ClientState.ACTION);
                } catch (Exception ignored) {}
            }
        } else if (cannonPower == cannonStrength) {
            nextPlayer();
        } else {
            won = -1;
            if (!penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
                nextPlayer();
            }
            try {
                currentView.setClientState(ClientState.ROLL_DICE);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Performs a dice roll for projectile impact when required.
     */
    @Override
    public void rollTheDices(String playerName) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || penaltyIfLose.getDiceRoll() != -1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> broken = new ArrayList<>();
        Coordinates firstBrokenTile = penaltyIfLose.randomRollForOne(currentView, currentPlayer);
        try {
            currentView.showDiceRoll(penaltyIfLose.getDiceRoll());
        } catch (Exception ignored) {}
        if (firstBrokenTile != null) {
            broken.add(firstBrokenTile);
            notifyBrokenTiles(playerName, broken);
        }
        else if (penaltyIfLose.getBranch() == null) {
            if (!penaltyIfLose.initializePenalty(game,currentView, currentPlayer)) {
                nextPlayer();
            }
        }
    }

    /**
     * Allows the player to choose which branches to keep when a projectile hits a branching tile.
     */
    @Override
    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> removedTiles = penaltyIfLose.chooseToMaintain(player, branchChoices);
        if (removedTiles == null) {
            try {
                currentView.showWrongInputMessage();
            } catch (Exception ignored) {}
        } else {
            notifyBrokenTiles(playerName, removedTiles);
            if (!penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
                nextPlayer();
            }
        }
    }

    /**
     * Allows the player to use batteries to defend against a projectile.
     */
    @Override
    public void useBatteries(String playerName, ArrayList<Coordinates> batteries) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        Tile batteryComponent = penaltyIfLose.playerUsesBatteryToDefend(player, batteries);
        if (batteries.isEmpty() && batteryComponent == null) {
            penaltyIfLose.hitOrMiss(currentView, player);
            Coordinates destroyedTile = penaltyIfLose.getDestroyedTile();
            if (penaltyIfLose.getBranch() == null && destroyedTile != null) {
                ArrayList<Coordinates> toRemove = new ArrayList<>();
                toRemove.add(destroyedTile);
                notifyBrokenTiles(playerName, toRemove);
            }
            return;
        }
        ArrayList<Tile> modifiedTiles = new ArrayList<>();
        if (batteryComponent != null) {
            modifiedTiles.add(batteryComponent);
            notifyModifiedTiles(playerName, modifiedTiles);
            if (!penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
                nextPlayer();
            }
        } else {
            try {
                currentView.showWrongInputMessage();
            } catch (Exception ignored) {}
        }
    }

    /**
     * Returns the list of projectiles used by the pirates.
     *
     * @return list of projectiles
     */
    @Override
    public ArrayList<Projectile> getListOfProjectiles() {
        return penaltyIfLose.getListOfProjectiles();
    }

    /**
     * Returns the amount of credits awarded on victory.
     *
     * @return credits gained
     */
    @Override
    public int getGainedCredits() {
        return rewardCredits;
    }

    /**
     * Handles disconnection of the current player.
     *
     * @param playerName the name of the disconnected player
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            if (won == 1) {
                choice(playerName, false);
            } else if (won == -1) {
                if (penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
                    System.out.println("Error applying automatic penalty");
                }
                nextPlayer();
            } else {
                playerIndex--;
                nextPlayer();
            }
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
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            if (won == 1) {
                choice(playerName, false);
            } else if (won == -1) {
                if (penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
                    System.out.println("Error applying automatic penalty");
                }
                nextPlayer();
            } else {
                nextPlayer();
            }
        }
    }
}
