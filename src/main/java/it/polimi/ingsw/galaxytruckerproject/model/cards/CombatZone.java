package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a Combat Zone card that introduces one or more competitive challenges
 * among all players. The player with the weakest performance in each challenge
 * receives a corresponding penalty.
 */
public class CombatZone extends Card {
    /** Ordered map of challenges to their associated penalties. */
    private final LinkedHashMap<ChallengeType, Penalty> listOfChallenges;

    /** Map that saves each player's performance value for the current challenge. */
    private final LinkedHashMap<Player, Float> savedValues;

    /** Player identified as having the weakest performance in the current challenge. */
    private Player minPlayer = null;

    /** Virtual view associated with the weakest player. */
    private VirtualView minPlayerView = null;

    /** Index of the player currently being evaluated. */
    private int playerIndex = -1;

    /** The player currently being evaluated. */
    private Player currentPlayer = null;

    /** Virtual view associated with the current player. */
    private VirtualView currentView = null;

    /** The challenge currently being processed. */
    private ChallengeType currentChallenge = null;

    /** The penalty associated with the current challenge. */
    private Penalty currentPenalty = null;

    /**
     * Creates a CombatZone card with an image.
     *
     * @param level the card's level
     * @param listOfChallenges the list of challenges and their penalties
     * @param filePath the image path for the card
     */
    @JsonCreator
    public CombatZone(
            @JsonProperty("level") int level,
            @JsonProperty("listOfChallenges") LinkedHashMap<ChallengeType, Penalty> listOfChallenges,
            @JsonProperty("imagePath") String filePath
    ) {
        super(level, 0, filePath);
        this.listOfChallenges = listOfChallenges;
        this.savedValues = new LinkedHashMap<>();
    }

    /**
     * Creates a CombatZone card without an image.
     *
     * @param level the card's level
     * @param listOfChallenges the list of challenges and their penalties
     */
    public CombatZone(
            @JsonProperty("level") int level,
            @JsonProperty("listOfChallenges") LinkedHashMap<ChallengeType, Penalty> listOfChallenges
    ) {
        super(level, 0, null);
        this.listOfChallenges = listOfChallenges;
        this.savedValues = new LinkedHashMap<>();
    }
    /**
     * Initializes the card with the current game context and virtual views.
     *
     * @param game the game interface
     * @param viewsMap the map of player names to their views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }
    /**
     * Starts the evaluation of the next challenge, or ends the card event if no challenges remain.
     */
    public void nextChallenge () {
        if (listOfChallenges.isEmpty()) {
            game.endCardEvent();
            return;
        }
        if (currentChallenge == null) {
            currentChallenge = listOfChallenges.sequencedKeySet().getFirst();
            currentPenalty = listOfChallenges.get(currentChallenge);
            playerIndex = 0;
            initializeCurrentPlayer();
            minPlayer = null;
        }
        switch (currentChallenge) {
            case ChallengeType.MINIMUM_CANNON_STRENGTH -> {
                cannonStrengthCheck();
            }
            case ChallengeType.MINIMUM_CREW_NUMBER -> {
                crewNumberCheck();
            }
            case ChallengeType.MINIMUM_ENGINE_POWER ->{
                enginePowerCheck();
            }
        }
        findMinPlayer();
    }
    /**
     * Advances to the next player in the evaluation sequence.
     * If all players have been evaluated, the weakest is identified.
     */
    public void nextPlayer() {
        playerIndex++;
        if (playerIndex < game.getNumberOfPlayers()) {
            this.initializeCurrentPlayer();
        }
        else{
            findMinPlayer();
        }
        nextChallenge();
    }
    /**
     * Sets the current player and their view based on the current index.
     */
    public void initializeCurrentPlayer() {
        this.currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentView = viewsMap.get(currentPlayer.getPlayerName());
    }
    /**
     * Evaluates the cannon strength of the current player.
     * If the player has no usable double cannon, uses single cannon plus bonuses.
     */
    public void cannonStrengthCheck () {
        if (currentPlayer != null) {
            if (currentPlayer.IsDisconnected() || currentPlayer.getShipBoard().getDoubleCannon().isEmpty() || currentPlayer.getShipBoard().getNumBatteries() == 0) {
                float power = currentPlayer.getShipBoard().getSingleCannonPower();
                if (power > 0) {
                    power += 2*currentPlayer.getShipBoard().getNumPurpleAliens();
                }
                notifyStrength(currentPlayer.getPlayerName(),power);
                savedValues.put(currentPlayer, power);
                nextPlayer();
            }
            else {
                try {
                    currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
                }catch(Exception ignored) {}
            }
        }
    }
    /**
     * Evaluates the engine power of the current player.
     * If the player has no usable double engine, uses single engines plus bonuses.
     */
    public void enginePowerCheck () {
        if (currentPlayer != null) {
            if (currentPlayer.IsDisconnected() || currentPlayer.getShipBoard().getDoubleEngine().isEmpty() || currentPlayer.getShipBoard().getNumBatteries() == 0) {
                float power = currentPlayer.getShipBoard().getNumSingleEngine();
                if (power > 0) {
                    power += 2*currentPlayer.getShipBoard().getNumBrownAliens();
                }
                notifyEngine(currentPlayer.getPlayerName(),power);
                savedValues.put(currentPlayer, power);
                nextPlayer();
            }
            else {
                try {
                    currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
                }catch(Exception ignored) {}
            }
        }
    }
    /**
     * Evaluates the number of crew members for each player.
     */
    public void crewNumberCheck () {
        for (Player player: game.getListOfInFlightPlayers()) {
            notifyCrew(player.getPlayerName(),player.getTotalCrew());
            savedValues.put(player, (float) player.getTotalCrew());
        }
    }
    /**
     * Identifies the player with the lowest performance in the current challenge
     * and applies the associated penalty.
     */
    public void findMinPlayer() {
        if (savedValues.size() != game.getNumberOfPlayers()) {
            return;
        }
        float minValue = 150; //big number
        float currentValue = -1;
        for (Player player: savedValues.keySet()) {
            currentValue = savedValues.get(player);
            if (currentValue < minValue) {
                minValue = currentValue;
                minPlayer = player;
            }
        }
        minPlayerView = viewsMap.get(minPlayer.getPlayerName());
        notifyVictim(minPlayer.getPlayerName());
        if (!currentPenalty.initializePenalty(game,minPlayerView, minPlayer)) {
            resetForNextPenalty();
        }
    }

    /**
     * Resets the internal state to move on to the next challenge after the penalty is applied.
     */
    public void resetForNextPenalty() {
        savedValues.clear();
        playerIndex = -1;
        currentChallenge = null;
        minPlayer = null;
        minPlayerView = null;
        currentPlayer = null;
        listOfChallenges.sequencedKeySet().removeFirst();
        nextPlayer();
    }
    /**
     * Handles the player's choice of double cannon and batteries for the cannon challenge.
     *
     * @param playerName the name of the player making the choice
     * @param doubleCannonPower the chosen cannon power
     * @param batteries the list of battery coordinates used
     */
    public void cannonChoice (String playerName, float doubleCannonPower, ArrayList<Coordinates> batteries) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        Map<Float,ArrayList<Tile>> valueMap = currentPlayer.useCannons(doubleCannonPower, batteries);
        if (valueMap == null) {
            float power = currentPlayer.getShipBoard().getSingleCannonPower();
            if (power > 0) {
                power += 2*currentPlayer.getShipBoard().getNumPurpleAliens();
            }
            notifyStrength(currentPlayer.getPlayerName(),power);
            savedValues.put(currentPlayer, power);
            nextPlayer();
            return;
        }
        float strength = valueMap.keySet().iterator().next();
        ArrayList<Tile> updatedTiles = valueMap.get(strength);
        notifyStrength(currentPlayer.getPlayerName(),strength);
        savedValues.put(currentPlayer, strength);
        if (!updatedTiles.isEmpty()) {
            notifyModifiedTiles(playerName, valueMap.get(strength));
        }
        nextPlayer();
    }
    /**
     * Handles the player's choice of double engine and batteries for the engine challenge.
     *
     * @param playerName the name of the player making the choice
     * @param numDoubleEngine the number of double engines chosen
     * @param batteriesToUse the coordinates of batteries used
     */
    @Override
    public void engineChoice(String playerName, int numDoubleEngine, ArrayList<Coordinates> batteriesToUse) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        Map<Integer,ArrayList<Tile>> valueMap = currentPlayer.useEngines(numDoubleEngine, batteriesToUse);
        if (valueMap == null) {
            float power = currentPlayer.getShipBoard().getNumSingleEngine();
            if (power > 0) {
                power += 2*currentPlayer.getShipBoard().getNumBrownAliens();
            }
            notifyEngine(currentPlayer.getPlayerName(),power);
            savedValues.put(currentPlayer, power);
            nextPlayer();
            return;
        }
        int strength = valueMap.keySet().iterator().next();
        ArrayList<Tile> updatedTiles = valueMap.get(strength);
        notifyEngine(currentPlayer.getPlayerName(),strength);
        savedValues.put(currentPlayer, (float) strength);
        if (!updatedTiles.isEmpty()) {
            notifyModifiedTiles(playerName, valueMap.values().iterator().next());
        }
        nextPlayer();
    }
    /**
     * Handles manual crew removal for the penalty phase.
     *
     * @param playerName the player removing crew
     * @param crewToRemove the coordinates of crew to be removed
     */
    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Tile> updated = currentPenalty.removeCrew(minPlayer, minPlayerView, crewToRemove);
        if (updated != null) {
            notifyModifiedTiles(playerName, updated);
            resetForNextPenalty();
        }
        else {
            try {
                currentView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
    }
    /**
     * Handles manual goods removal for the penalty phase.
     *
     * @param playerName the player removing goods
     * @param goodsToRemove the coordinates of goods to be removed
     */
    @Override
    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (minPlayer == null || !player.getPlayerName().equals(minPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        ArrayList<Tile> updatedTiles = currentPenalty.removeGoods(minPlayer,minPlayerView, goodsToRemove);
        if (updatedTiles == null) {
            try {
                currentView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
        else {
            notifyModifiedTiles(playerName, updatedTiles);
            resetForNextPenalty();
        }
    }
    /**
     * Handles dice roll during projectile-related penalties.
     *
     * @param playerName the player rolling the dice
     */
    @Override
    public void rollTheDices(String playerName) {
        if (!playerName.equals(minPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> broken =  new ArrayList<>();
        Coordinates firstBrokenTile = currentPenalty.randomRollForOne(minPlayerView, minPlayer);
        try {
            minPlayerView.showDiceRoll(currentPenalty.getDiceRoll());
        } catch(Exception ignored) {}
        if (firstBrokenTile != null) {
            broken.add(firstBrokenTile);
            notifyBrokenTiles(playerName, broken);
            if (!currentPenalty.initializePenalty(game,minPlayerView, minPlayer)) {
                resetForNextPenalty();
            }
        }
    }
    /**
     * Handles player choice of ship branch to keep after tile destruction.
     *
     * @param playerName the player choosing the branch
     * @param branchChoices the coordinates belonging to the chosen branch
     */
    @Override
    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> removedTiles = currentPenalty.chooseToMaintain(player, branchChoices);
        if (removedTiles == null) {
            try {
                currentView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
        else {
            notifyBrokenTiles(playerName, removedTiles);
            if (!currentPenalty.initializePenalty(game,minPlayerView, minPlayer)) {
                resetForNextPenalty();
            }
        }
    }
    /**
     * Handles use of batteries to block a projectile during a penalty.
     *
     * @param playerName the player using batteries
     * @param batteries the coordinates of batteries to be used
     */
    @Override
    public void useBatteries(String playerName, ArrayList<Coordinates> batteries) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        Tile batteryComponent = currentPenalty.playerUsesBatteryToDefend(player,batteries);
        if (batteries.isEmpty() && batteryComponent == null) {
            currentPenalty.hitOrMiss(currentView, player);
            Coordinates destroyedTile = currentPenalty.getDestroyedTile();
            if (currentPenalty.getBranch() == null && destroyedTile != null) {
                ArrayList<Coordinates> toRemove = new ArrayList<>();
                toRemove.add(destroyedTile);
                notifyBrokenTiles(playerName, toRemove);
            }
            return;
        }
        ArrayList<Tile> modifiedTiles = new ArrayList<>();
        modifiedTiles.add(batteryComponent);
        if (batteryComponent != null) {
            notifyModifiedTiles(playerName, modifiedTiles);
            if (!currentPenalty.initializePenalty(game,minPlayerView,minPlayer)) {
                resetForNextPenalty();
            }
        }
        else {
            try{
                currentView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
    }
    /**
     * Returns a string representation of the CombatZone card.
     *
     * @return string describing the card and its challenges
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Combat Zone: ");
        string.append("id ").append(id).append(" ");
        for (ChallengeType challengeType: listOfChallenges.keySet()) {
            string.append(challengeType).append(" ").append(listOfChallenges.get(challengeType).toString()).append(" ");;
            }
        return string.toString();
    }

    @Override
    public LinkedHashMap<ChallengeType, Penalty> getChallenges() {
        return listOfChallenges;
    }
    /**
     * Notifies all players of a player's evaluated cannon strength.
     *
     * @param playerName the evaluated player
     * @param strength the cannon strength value
     */
    public void notifyStrength(String playerName, float strength) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyCombatZoneStrength(playerName,strength);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Notifies all players of a player's evaluated engine power.
     *
     * @param playerName the evaluated player
     * @param strength the engine strength value
     */
    public void notifyEngine(String playerName, float strength) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyCombatZoneEngine(playerName,strength);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Notifies all players of a player's crew count.
     *
     * @param playerName the evaluated player
     * @param crew the number of crew members
     */
    public void notifyCrew(String playerName, int crew) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyCombatZoneCrew(playerName,crew);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Returns the current list of challenges and penalties.
     *
     * @return the challenge-to-penalty mapping
     */
    @Override
    public Penalty getPenalty() {
        ChallengeType key = listOfChallenges.sequencedKeySet().getFirst();
        return listOfChallenges.remove(key);
    }
    /**
     * Handles player disconnection during challenge or penalty processing.
     *
     * @param playerName the name of the disconnected player
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (minPlayer != null && minPlayer.getPlayerName().equals(playerName)) {
            // if player is disconnected penalty should happen automatically
            if (!currentPenalty.initializePenalty(game,minPlayerView,minPlayer)) {
                resetForNextPenalty();
            }
            return;
        }
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            playerIndex--;
            nextPlayer();
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
        if (minPlayer != null && minPlayer.getPlayerName().equals(playerName)) {
            // if player is disconnected penalty should happen automatically
            if (!currentPenalty.initializePenalty(game, minPlayerView, minPlayer)) {
                resetForNextPenalty();
            }
            return;
        }
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            nextPlayer();
        }
    }
}