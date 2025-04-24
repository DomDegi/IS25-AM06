package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.*;


public class CombatZone extends Card {
    private final LinkedHashMap<ChallengeType, Penalty> listOfChallenges ;
    private final LinkedHashMap<Player, Float> savedValues;
    private Player minPlayer = null;
    private VirtualView minPlayerView = null;
    private int playerIndex = -1;
    private Player currentPlayer =  null;
    private VirtualView currentView = null;
    private ChallengeType currentChallenge = null;
    private Penalty currentPenalty = null;


    @JsonCreator
    public CombatZone(
            @JsonProperty("level") int level,
            @JsonProperty("listOfChallenges") LinkedHashMap<ChallengeType, Penalty> listOfChallenges
    ) {
        super(level, 0);
        this.listOfChallenges = listOfChallenges;
        this.savedValues = new LinkedHashMap<>();
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    public void nextChallenge () {
        if (listOfChallenges.isEmpty()) {
            game.endCardEvent();
        }
        if (currentChallenge == null) {
            currentChallenge = listOfChallenges.sequencedKeySet().getFirst();
            currentPenalty = listOfChallenges.get(currentChallenge);
            playerIndex = 0;
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

    public void nextPlayer() {
        playerIndex++;
        this.currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentView = viewsMap.get(currentPlayer.getPlayerName());
        nextChallenge();
    }

    public void cannonStrengthCheck () {
        if (currentPlayer != null) {
            if (currentPlayer.IsDisconnected()) {
                float power = currentPlayer.getShipBoard().getSingleCannonPower();
                if (power == 0) {
                    power += currentPlayer.getShipBoard().getNumPurpleAliens();
                }
                savedValues.put(currentPlayer, power);
            }
            else {
                currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
            }
        }
    }

    public void enginePowerCheck () {
        if (currentPlayer != null) {
            if (currentPlayer.IsDisconnected()) {
                float power = currentPlayer.getShipBoard().getNumSingleEngine();
                if (power == 0) {
                    power += currentPlayer.getShipBoard().getNumBrownAliens();
                }
                savedValues.put(currentPlayer, power);
            }
            else {
                currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
            }
        }
    }

    public void crewNumberCheck () {
        for (Player player: game.getListOfInFlightPlayers()) {
            savedValues.put(player, (float) player.getTotalCrew());
        }
    }

    public void findMinPlayer() {
        if (savedValues.size() != game.getNumberOfPlayers())
            return;
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
        if (!currentPenalty.initializePenalty(game,minPlayerView, minPlayer)) {
            resetForNextPenalty();
        }
    }


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

    public void cannonChoice (String playerName, float doubleCannonPower, ArrayList<Coordinates> batteries) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        Map<Float,ArrayList<Tile>> valueMap = currentPlayer.useCannons(doubleCannonPower, batteries);
        float strength = valueMap.keySet().iterator().next();
        if (strength != -1) {
            savedValues.put(currentPlayer, strength);
            notifyModifiedTiles(playerName,valueMap.values().iterator().next());
            nextPlayer();
        }
        else {
            currentView.showWrongInputMessage();
        }
    }

    @Override
    public void engineChoice(String playerName, int numDoubleEngine, ArrayList<Coordinates> batteriesToUse) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        Map<Integer,ArrayList<Tile>> valueMap = currentPlayer.useEngines(numDoubleEngine, batteriesToUse);
        float strength = valueMap.keySet().iterator().next();
        if (strength != -1) {
            savedValues.put(currentPlayer, strength);
            notifyModifiedTiles(playerName,valueMap.values().iterator().next());
            nextPlayer();
        }
        else {
            currentView.showWrongInputMessage();
        }
    }

    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        ArrayList<Tile> updated = currentPenalty.removeCrew(minPlayer, minPlayerView, crewToRemove);
        if (updated != null) {
            notifyModifiedTiles(playerName, updated);
            resetForNextPenalty();
        }
        else
            currentView.showWrongInputMessage();
    }

    @Override
    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        ArrayList<Tile> updatedTiles = currentPenalty.removeGoods(minPlayer,minPlayerView, goodsToRemove);
        if (updatedTiles == null) {
            currentView.showWrongInputMessage();
        }
        else {
            notifyModifiedTiles(playerName, updatedTiles);
            resetForNextPenalty();
        }
    }

    @Override
    public void rollTheDices(String playerName) {
        if (!playerName.equals(minPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
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

    @Override
    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        ArrayList<Coordinates> removedTiles = currentPenalty.chooseToMaintain(player, branchChoices);
        if (removedTiles == null) {
            currentView.showWrongInputMessage();
        }
        else {
            notifyBrokenTiles(playerName, removedTiles);
            if (!currentPenalty.initializePenalty(game,minPlayerView, minPlayer)) {
                resetForNextPenalty();
            }
        }
    }

    @Override
    public void useBatteries(String playerName, ArrayList<Coordinates> batteries) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        Tile batteryComponent = currentPenalty.playerUsesBatteryToDefend(player,batteries);
        ArrayList<Tile> modifiedTiles = new ArrayList<>();
        modifiedTiles.add(batteryComponent);
        if (batteryComponent != null) {
            notifyModifiedTiles(playerName, modifiedTiles);
            if (!currentPenalty.initializePenalty(game,minPlayerView,minPlayer)) {
                resetForNextPenalty();
            }
        }
        else {
            currentView.showWrongInputMessage();
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Combat Zone: ");
        for (ChallengeType challengeType: listOfChallenges.keySet()) {
            string.append(challengeType).append(" ").append(listOfChallenges.get(challengeType).toString()).append(" ");;
            }
        return string.toString();
    }

    @Override
    public LinkedHashMap<ChallengeType, Penalty> getChallenges() {
        return listOfChallenges;
    }
}