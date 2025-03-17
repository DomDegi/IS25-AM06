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
    private LinkedHashMap<Player, Integer> savedValues;
    private Optional<ChallengeType> currentChallenge = Optional.empty();
    private boolean losingPlayerDecided = false;


    public CombatZone(int level, LinkedHashMap<ChallengeType, Penalty> listOfPenalties) {
        super(level, 0);
        this.listOfChallenges = listOfPenalties;
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
            playerToPlay = Optional.of(Collections.min(savedValues.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey());
            losingPlayerDecided = true;
            listOfChallenges.get(currentChallenge.get()).printInfo(playerToPlay.get());
        }

        if (currentChallenge.isEmpty()) {
            currentChallenge = Optional.of(listOfChallenges.keySet().iterator().next());
        }

        playerToPlay = Optional.of(game.getListOfPlayers().get(playerIndex));
        switch(currentChallenge.get()) {
            case MINIMUM_CANNON_STRENGTH: {
                System.out.println("You can decide to try the challenge with the single cannon strength or activate the double cannons\n");
                playerToPlay.get().printCurrentInfoCannons();
                playerToPlay.get().printCurrentInfoBatteries();
                break;
            }
            case MINIMUM_CREW_NUMBER: {
                System.out.println("You have this many crew members" + playerToPlay.get().getTotalCrew());
                savedValues.put(playerToPlay, playerToPlay.get().getTotalCrew());
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
                }
                case ChallengeType.MINIMUM_CREW_NUMBER: {

                }
                case ChallengeType.MINIMUM_CANNON_STRENGTH: {

                }
            }
        }
    }

    public void minimumEngineStrength (Game game, Player playerToPlay, String[] input) {
        if (input[0].equalsIgnoreCase("no")) {
            useDoubleEngines(new ArrayList<>());
            playerIndex++;
            initializeCard(game);
            return;
        }
        ArrayList<Coordinates> coordinates = new ArrayList<>(parseCoordinates(input));

        //wasn't able to parse the input to coordinates correctly
        if (coordinates.isEmpty()) {
            System.out.println("try typing the input again\n");
            return;
        }

        //next input will actually give us the engine strength
        int engineStrength =useDoubleEngines(coordinates);
        if (engineStrength == - 2){
            System.out.println("input another set of coordinates to choose batteries");
            return;
        }

        //coordinates didn't correspond to doubleCannons or batteries
        if (engineStrength == -1){
            System.out.println("you picked wrong coordinates, re-enter both engines and batteries");
            return;
        }
        //saves the engine strength of current player and goes to next one
        savedValues.put(playerToPlay, engineStrength);
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