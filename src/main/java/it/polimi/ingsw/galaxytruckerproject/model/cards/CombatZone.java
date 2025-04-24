package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.UseCannonResponse;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.UseEngineResponse;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.LinkedHashMap;
import java.util.Map;


public class CombatZone extends Card {
    private final LinkedHashMap<ChallengeType, Penalty> listOfChallenges ;
    private final LinkedHashMap<Player, Float> savedValues;
    private Player minPlayer = null;
    private VirtualView minPlayerView = null;
    private int playerIndex = -1;
    private Player currentPlayer =  null;
    private ViewInterface currentView = null;
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
                currentView.asksToUseCannons();
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
                currentView.asksToUseEngines();
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
        if (!currentPenalty.initializePenalty(minPlayerView, minPlayer)) {
            resetForNextPenalty();
        }
    }

    public void executeCard (Message message) {
        String playerName = message.getNickname();
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            return;
        }
        if (minPlayer == null) {
            switch (currentChallenge) {
                case MINIMUM_CANNON_STRENGTH -> saveCannonValue(message);
                case MINIMUM_ENGINE_POWER -> saveEngineValue(message);
                case MINIMUM_CREW_NUMBER -> saveCrewValue();
            }
        }
        else {
            if (currentPenalty.applyPenalty(game, minPlayer, minPlayerView,  message) == 1) {
                resetForNextPenalty();
            }
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

    public void saveCannonValue (Message message) {
        if (message.getMessageType().equals(MessageType.USE_CANNON_RESPONSE)) {
            UseCannonResponse  useCannonResponse = (UseCannonResponse) message;
            float strength = currentPlayer.useCannons(useCannonResponse.getStrength(), useCannonResponse.getCoordinates());
            if (strength != -1) {
                savedValues.put(currentPlayer, strength);
                nextPlayer();
            }
            else {
                currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
            }
        }
    }

    public void saveEngineValue (Message message) {
        if (message.getMessageType().equals(MessageType.USE_ENGINE_RESPONSE)) {
            UseEngineResponse  useEngineResponse = (UseEngineResponse) message;
            float strength = currentPlayer.useEngines(useEngineResponse.getNumEngine(), useEngineResponse.getCoordinates());
            if (strength != -1) {
                savedValues.put(currentPlayer, strength);
                nextPlayer();
            }
            else {
                currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
            }
        }
    }

    public void saveCrewValue () {
        if (!savedValues.containsKey(currentPlayer)) {
            savedValues.put(currentPlayer, (float) currentPlayer.getTotalCrew());
        }
        nextPlayer();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Combat Zone: ");
        for (ChallengeType challengeType: listOfChallenges.keySet()) {
            string.append(challengeType).append(" ").append(listOfChallenges.get(challengeType).toString()).append(" ");;
            }
        return string.toString();
    }
}