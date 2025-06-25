package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the "Slavers" enemy card in the game.
 * This card challenges players with combat and imposes penalties
 * or rewards depending on the outcome.
 */
public class Slavers extends Enemies {

    /** Credits rewarded to the player upon winning the encounter. */
    private final int rewardCredits;

    /** Number of crew members lost if the player fails. */
    private final int lostCrew;

    /** Index of the current player being processed. */
    private int playerIndex;

    /** Current player facing the Slavers. */
    private Player currentPlayer = null;

    /** View of the current player. */
    private VirtualView playersView = null;

    /** Penalty applied when the player loses the encounter. */
    private final CrewPenalty penaltyIfLose;

    /** Status of the battle: 0 = undecided, 1 = won, -1 = lost. */
    private int won = 0;

    /**
     * JSON constructor for deserialization.
     *
     * @param level          the threat level of the enemy
     * @param requiredDays   days lost if defeated
     * @param cannonStrength the enemy's cannon strength
     * @param rewardCredits  credits rewarded if defeated
     * @param lostCrew       crew members lost if defeated
     * @param filePath       path to the enemy image
     */
    @JsonCreator
    public Slavers(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("lostCrew") int lostCrew,
            @JsonProperty("imagePath") String filePath
    ) {
        super(level, requiredDays, cannonStrength, filePath);
        this.rewardCredits = rewardCredits;
        this.lostCrew = lostCrew;
        this.playerIndex = 0;
        this.penaltyIfLose = new CrewPenalty(lostCrew);
    }

    /**
     * Alternative constructor without image path.
     */
    public Slavers(
            int level,
            int requiredDays,
            int cannonStrength,
            int rewardCredits,
            int lostCrew
    ) {
        super(level, requiredDays, cannonStrength, null);
        this.rewardCredits = rewardCredits;
        this.lostCrew = lostCrew;
        this.playerIndex = 0;
        this.penaltyIfLose = new CrewPenalty(lostCrew);
    }

    /**
     * Returns a string representation of the Slavers card.
     *
     * @return a string with relevant card info
     */
    public String toString() {
        return "Slavers " + super.toString() + " rewardCredits: " + rewardCredits + "  lostCrew: " + lostCrew;
    }

    /**
     * Initializes the card and starts the player encounter sequence.
     *
     * @param game     reference to the game state
     * @param viewsMap map of player names to their views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    /**
     * Proceeds to the next player in the encounter sequence.
     */
    public void nextPlayer() {
        if (currentPlayer != null){
            playerIndex++;
        }
        won = 0;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = currentPlayer.getPlayerName();
        this.playersView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
        if (singleCannonPower > 0) {
            singleCannonPower += 2 * currentPlayer.getShipBoard().getNumPurpleAliens();
        }

        won = 0;
        if (currentPlayer.IsDisconnected()) {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            } else if (singleCannonPower == cannonStrength) {
                nextPlayer();
            } else {
                won = -1;
                penaltyIfLose.initializePenalty(game, playersView, currentPlayer);
                nextPlayer();
            }
        } else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                try {
                    playersView.setClientState(ClientState.ACTION);
                } catch(Exception ignored) {}
            } else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                } else {
                    won = -1;
                    if (!penaltyIfLose.initializePenalty(game, playersView, currentPlayer)) {
                        nextPlayer();
                    }
                    try {
                        playersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
                    } catch(Exception ignored) {}
                }
            } else {
                try {
                    playersView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
                } catch(Exception ignored) {}
            }
        }
    }

    /**
     * Handles the cannon usage choice by the player.
     *
     * @param playerName       the name of the player making the choice
     * @param doubleCannonPower power from double cannons
     * @param batteriesToUse   list of batteries used
     */
    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
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
                penaltyIfLose.initializePenalty(game, playersView, currentPlayer);
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
                    playersView.setClientState(ClientState.ACTION);
                } catch(Exception ignored) {}
            }
        } else if (cannonPower == cannonStrength) {
            nextPlayer();
        } else {
            won = -1;
            if (!penaltyIfLose.initializePenalty(game, playersView, currentPlayer)) {
                nextPlayer();
            }
            try {
                playersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            } catch(Exception ignored) {}
        }
    }

    /**
     * Handles the final decision of the player after winning the encounter.
     *
     * @param playerName name of the player
     * @param decision   true to accept reward, false to skip
     */
    @Override
    public void choice(String playerName, boolean decision) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || won != 1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
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
     * Removes the specified crew members from the player's ship.
     *
     * @param playerName   name of the player
     * @param crewToRemove coordinates of crew members to remove
     */
    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName()) || won != -1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        ArrayList<Tile> updated = penaltyIfLose.removeCrew(player, playersView, crewToRemove);
        if (updated != null) {
            notifyModifiedTiles(playerName, updated);
            nextPlayer();
        } else {
            try {
                playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
    }

    /**
     * Returns the number of crew members lost if the player loses.
     *
     * @return number of lost crew members
     */
    @Override
    public int getCrewNumber() {
        return lostCrew;
    }

    /**
     * Returns the number of credits gained if the player wins.
     *
     * @return number of credits gained
     */
    @Override
    public int getGainedCredits() {
        return rewardCredits;
    }

    /**
     * Handles a disconnection event from a player.
     *
     * @param playerName name of the disconnected player
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            if (won == 1) {
                choice(playerName,false);
            }
            else if (won == -1) {
                if (penaltyIfLose.initializePenalty(game, playersView, currentPlayer)) {
                    System.out.println("Error applying automatic penalty");
                }
                nextPlayer();
            }
            else{
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
                choice(playerName,false);
            }
            else if (won == -1) {
                if (penaltyIfLose.initializePenalty(game, playersView, currentPlayer)) {
                    System.out.println("Error applying automatic penalty");
                }
                nextPlayer();
            }
            else{
                nextPlayer();
            }
        }
    }
}
