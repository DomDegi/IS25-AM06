package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.*;

public class CombatZone extends Card {
    private final LinkedHashMap<ChallengeType, Penalty> listOfChallenges ;
    private int playerIndex;
    private Player currentPlayer = null;
    private final LinkedHashMap<Player, Float> savedValues;
    private ChallengeType currentChallenge = null;
    private boolean losingPlayerDecided = false;


    public CombatZone(int level, LinkedHashMap<ChallengeType, Penalty> listOfPenalties) {
        super(level, 0);
        this.listOfChallenges = listOfPenalties;
        this.playerIndex = 0;
        this.savedValues = new LinkedHashMap<>();
    }

    @Override
    public void initializeCard(Game game) {
        //if there is only one player still flying, combat>one cards get skipped
        if (game.getNumberOfPlayers() == 1) {
            game.endCardEvent();
        }
        if (listOfChallenges.isEmpty()) {
            System.out.println("You have completed all the challenges of the combat zone\n");
            game.endCardEvent();
            return;
        }

        if (playerIndex > game.getNumberOfPlayers() - 1 && !losingPlayerDecided) {
            //this means the losing player still has to get chosen
            currentPlayer = Collections.min(savedValues.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
            losingPlayerDecided = true;
            savedValues.clear();
            listOfChallenges.get(currentChallenge).printInfo(currentPlayer);
        }

        if (currentChallenge == null) {
            currentChallenge = listOfChallenges.entrySet().iterator().next().getKey();
        }

        currentPlayer = game.getListOfPlayers().get(playerIndex);
        switch(currentChallenge) {
            case MINIMUM_CANNON_STRENGTH: {
                System.out.println("You can decide to try the challenge with the single cannon strength or activate the double cannons\n");
                currentPlayer.printCurrentInfoCannons();
                currentPlayer.printCurrentInfoBatteries();
                break;
            }

            //This case is automatically saves the values because it doesn't need inputs
            case MINIMUM_CREW_NUMBER: {
                System.out.println("You have this many crew members" + currentPlayer.getTotalCrew());
                savedValues.put(currentPlayer, (float) currentPlayer.getTotalCrew());
                playerIndex++;
                initializeCard(game);
                break;
            }
            case MINIMUM_ENGINE_POWER: {
                System.out.println("You can decide to try the challenge with the single engine power or activate the double engines\n");
                currentPlayer.printCurrentInfoCannons();
                currentPlayer.printCurrentInfoBatteries();
                break;
            }
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (losingPlayerDecided && currentPlayer != null && playerName.equalsIgnoreCase(currentPlayer.getPlayerName())) {
            int penaltyReturn = listOfChallenges.get(currentChallenge).applyPenalty(game, currentPlayer, input);
            if (penaltyReturn == 1) {
                System.out.println("Penalty has been applied to " + currentPlayer.getPlayerName() + "\n");
                losingPlayerDecided = false;
                playerIndex = 0;
                listOfChallenges.remove(currentChallenge);
                initializeCard(game);
            }
            else {
                System.out.println("Penalty needs more input to get completed\n");
            }
        } else if (currentPlayer != null && playerName.equalsIgnoreCase(currentPlayer.getPlayerName())) {
            switch (currentChallenge) {
                case ChallengeType.MINIMUM_ENGINE_POWER: {
                    minimumEngineStrength(game, currentPlayer, input);
                    break;
                }
                case ChallengeType.MINIMUM_CREW_NUMBER: {
                    System.out.println("you shouldn't get here because finding the loser doesn't require inputs\n");
                    break;
                }
                case ChallengeType.MINIMUM_CANNON_STRENGTH: {
                    minimumCannonStrength(game, currentPlayer, input);
                    break;
                }
            }
        }
    }

    public void minimumEngineStrength (Game game, Player playerToPlay, String[] input) {
        if (input[0].equalsIgnoreCase("no")) {
            int engineStrength = playerToPlay.useDoubleEngines(new ArrayList<>());
            savedValues.put(playerToPlay, (float) engineStrength);
            playerIndex++;
            initializeCard(game);
            return;
        }
        ArrayList<Coordinates> coordinates = new ArrayList<>(playerToPlay.parseCoordinates(input));

        //wasn't able to parse the input to coordinates correctly
        if (coordinates.isEmpty()) {
            System.out.println("try typing the input again\n");
            return;
        }

        //next input will actually give us the engine strength
        int engineStrength = playerToPlay.useDoubleEngines(coordinates);
        if (engineStrength == - 2){
            System.out.println("input another set of coordinates to choose batteries\n");
            return;
        }

        //coordinates didn't correspond to doubleCEngines or batteries
        if (engineStrength == -1){
            System.out.println("you picked wrong coordinates, re-enter both engines and batteries\n");
            return;
        }
        //saves the engine strength of current player and goes to the next one
        savedValues.put(playerToPlay, (float) engineStrength);
        playerIndex++;
        initializeCard(game);
    }

    public void minimumCannonStrength (Game game, Player playerToPlay, String[] input) {
        if (input[0].equalsIgnoreCase("no")) {
            float cannonStrength = playerToPlay.useDoubleCannons(new ArrayList<>());
            savedValues.put(playerToPlay, cannonStrength);
            playerIndex++;
            initializeCard(game);
            return;
        }
        ArrayList<Coordinates> coordinates = new ArrayList<>(playerToPlay.parseCoordinates(input));

        if (coordinates.isEmpty()) {
            System.out.println("try typing the input again\n");
            return;
        }

        //next input will actually give us the cannon strength
        float cannonStrength = playerToPlay.useDoubleCannons(coordinates);
        if (cannonStrength == - 2){
            System.out.println("input another set of coordinates to choose batteries\n");
            return;
        }

        //coordinates didn't correspond to cannons or batteries
        if (cannonStrength == -1) {
            System.out.println("you picked wrong coordinates, re-enter both cannons and batteries\n");
        }
        //saves the engine strength of current player and goes to the next one
        savedValues.put(playerToPlay, cannonStrength);
        playerIndex++;
        initializeCard(game);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Combat Zone: ");
        for (ChallengeType challengeType: listOfChallenges.keySet()) {
            string.append(challengeType).append(" ").append(listOfChallenges.get(challengeType).toString()).append(" ");
        }
        string.append("\n");
        return string.toString();
    }
}