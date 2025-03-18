package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.*;

public class CombatZone extends Card {
    private final LinkedHashMap<ChallengeType, Penalty> listOfChallenges ;
    private int playerIndex;
    private Optional<Player> playerToPlay =  Optional.empty();
    private LinkedHashMap<Player, Float> savedValues;
    private Optional<ChallengeType> currentChallenge = Optional.empty();
    private boolean losingPlayerDecided = false;


    public CombatZone(int level, LinkedHashMap<ChallengeType, Penalty> listOfPenalties) {
        super(level, 0);
        this.listOfChallenges = listOfPenalties;
        this.playerIndex = 0;
        this.savedValues = new LinkedHashMap<>();
    }

    @Override
    public void initializeCard(Game game) {
        if (listOfChallenges.isEmpty()) {
            System.out.println("You have completed all the challenges of the combat zone\n");
            game.DrawCard();
            return;
        }

        if (playerIndex > game.getPlayerCount() - 1 && !losingPlayerDecided) {
            //this means the losing player still has to get chosen
            playerToPlay = Optional.of(Collections.min(savedValues.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey());
            losingPlayerDecided = true;
            savedValues.clear();
            listOfChallenges.get(currentChallenge.get()).printInfo(playerToPlay.get());
        }

        if (currentChallenge.isEmpty()) {
            currentChallenge = Optional.of(listOfChallenges.entrySet().iterator().next().getKey());
        }

        playerToPlay = Optional.of(game.getListOfPlayers().get(playerIndex));
        switch(currentChallenge.get()) {
            case MINIMUM_CANNON_STRENGTH: {
                System.out.println("You can decide to try the challenge with the single cannon strength or activate the double cannons\n");
                playerToPlay.get().printCurrentInfoCannons();
                playerToPlay.get().printCurrentInfoBatteries();
                break;
            }

            //This case is automatically saves the values because it doesn't need inputs
            case MINIMUM_CREW_NUMBER: {
                System.out.println("You have this many crew members" + playerToPlay.get().getTotalCrew());
                savedValues.put(playerToPlay.get(), (float) playerToPlay.get().getTotalCrew());
                playerIndex++;
                initializeCard(game);
                break;
            }
            case MINIMUM_ENGINE_POWER: {
                System.out.println("You can decide to try the challenge with the single engine power or activate the double engines\n");
                playerToPlay.get().printCurrentInfoCannons();
                playerToPlay.get().printCurrentInfoBatteries();
                break;
            }
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (losingPlayerDecided && playerToPlay.isPresent() && playerName.equalsIgnoreCase(playerToPlay.get().getPlayerName())) {
            int penaltyReturn = listOfChallenges.get(currentChallenge.get()).applyPenalty(game, playerToPlay.get(), input);
            if (penaltyReturn == 1) {
                System.out.println("Penalty has been applied to " + playerToPlay.get().getPlayerName() + "\n");
                losingPlayerDecided = false;
                playerIndex = 0;
                listOfChallenges.remove(currentChallenge.get());
                initializeCard(game);
            }
            else {
                System.out.println("Penalty needs more input to get completed\n");
            }
        } else if (playerToPlay.isPresent() && playerName.equalsIgnoreCase(playerToPlay.get().getPlayerName())) {
            switch (currentChallenge.get()) {
                case ChallengeType.MINIMUM_ENGINE_POWER: {
                    minimumEngineStrength(game, playerToPlay.get(), input);
                    break;
                }
                case ChallengeType.MINIMUM_CREW_NUMBER: {
                    System.out.println("you shouldn't get here because finding the loser doesn't require inputs\n");
                    break;
                }
                case ChallengeType.MINIMUM_CANNON_STRENGTH: {
                    minimumCannonStrength(game, playerToPlay.get(), input);
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
            float cannonStrength = playerToPlay.useDoubleCannons(new ArrayList<Coordinates>());
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