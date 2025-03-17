package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.Penalty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CombatZone extends Card {
    private final LinkedHashMap<ChallengeType, Penalty> listOfChallenges;
    private int playerIndex;
    private Player playerToPlay;
    private Map<Player, Integer> savedValues;

    public CombatZone(int level, LinkedHashMap<ChallengeType, Penalty> listOfPenalties) {
        super(level, 0);
        this.listOfChallenges = listOfPenalties;
    }

    @Override
    public void initializeCard(Game game) {
        if (listOfChallenges.isEmpty()) {

        }

        playerToPlay = game.getListOfPlayers().get(playerIndex);

    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        switch (listOfChallenges.keySet().iterator().next()) {
            case ChallengeType.MINIMUM_ENGINE_POWER -> {

            }
        }
    }
}