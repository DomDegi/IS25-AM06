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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


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

    public void nextPlayer() {
        playerIndex++;
        if (playerIndex < game.getNumberOfPlayers()) {
            this.initializeCurrentPlayer();
        }
        nextChallenge();
    }

    public void initializeCurrentPlayer() {
        this.currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentView = viewsMap.get(currentPlayer.getPlayerName());
    }

    public void cannonStrengthCheck () {
        if (currentPlayer != null) {
            if (currentPlayer.IsDisconnected() || currentPlayer.getShipBoard().getDoubleCannon().isEmpty() || currentPlayer.getShipBoard().getNumBatteries() == 0) {
                float power = currentPlayer.getShipBoard().getSingleCannonPower();
                if (power > 0) {
                    power += 2*currentPlayer.getShipBoard().getNumPurpleAliens();
                }
                savedValues.put(currentPlayer, power);
            }
            else {
                try {
                    currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
                }catch(Exception ignored) {}
            }
        }
    }

    public void enginePowerCheck () {
        if (currentPlayer != null) {
            if (currentPlayer.IsDisconnected() || currentPlayer.getShipBoard().getDoubleEngine().isEmpty() || currentPlayer.getShipBoard().getNumBatteries() == 0) {
                float power = currentPlayer.getShipBoard().getNumSingleEngine();
                if (power > 0) {
                    power += 2*currentPlayer.getShipBoard().getNumBrownAliens();
                }
                savedValues.put(currentPlayer, power);
            }
            else {
                try {
                    currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
                }catch(Exception ignored) {}
            }
        }
    }

    public void crewNumberCheck () {
        for (Player player: game.getListOfInFlightPlayers()) {
            savedValues.put(player, (float) player.getTotalCrew());
        }
    }

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
        System.out.println(minPlayer.getPlayerName());
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
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        Map<Float,ArrayList<Tile>> valueMap = currentPlayer.useCannons(doubleCannonPower, batteries);
        if (valueMap == null) {
            try{
                currentView.showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        float strength = valueMap.keySet().iterator().next();
        ArrayList<Tile> updatedTiles = valueMap.get(strength);

        savedValues.put(currentPlayer, strength);
        if (!updatedTiles.isEmpty()) {
            notifyModifiedTiles(playerName, valueMap.get(strength));
        }
        nextPlayer();
    }

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
            try{
                currentView.showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        int strength = valueMap.keySet().iterator().next();
        ArrayList<Tile> updatedTiles = valueMap.get(strength);

        savedValues.put(currentPlayer, (float) strength);
        if (!updatedTiles.isEmpty()) {
            notifyModifiedTiles(playerName, valueMap.values().iterator().next());
        }
        nextPlayer();
    }

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

    @Override
    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(minPlayer.getPlayerName())) {
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
}