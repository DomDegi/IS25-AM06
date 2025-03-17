package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;
import java.util.Optional;

public class OpenSpace extends Card {
    private Optional<Player> playerToPlay = Optional.empty();
    private int playerIndex = 0;
    private ArrayList<Coordinates> chosenDoubleEngines;
    private ArrayList<Coordinates> chosenBatteryComponents;

    //the subclass OpenSpace needs the same parameters as the superclass
    public OpenSpace(int level) {
        super(level, 0);
    }

    //Initialize card for the player at the index playerIndex of the flightBoard ranking
    @Override
    public void initializeCard(Game game) {
        //if index is higher than the number of player in the game -1, it will go out of bounds
        if (playerIndex > game.getPlayerCount() - 1) {
            game.getFlightBoard().concludeMovement();
            //draw next card
            game.DrawCard();
        }

        playerToPlay = Optional.of(game.getListOfPlayers().get(playerIndex));
        chosenDoubleEngines = new ArrayList<>();
        chosenBatteryComponents = new ArrayList<>();

        playerToPlay.get().printCurrentInfoEngines();
        playerToPlay.get().printCurrentInfoBatteries();
    }

    //makes so that the player gain as many days as their engineStrength
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        //playerName has to be the same as
        if (playerToPlay.isPresent() && playerToPlay.get().getPlayerName().equals(playerName)) {
            if (input[0].equalsIgnoreCase("no")) {
                playerIndex++;
                initializeCard(game);
                return;
            }
            ArrayList<Coordinates> temp = new ArrayList<>();
            temp = new ArrayList<>(parseCoordinates(input));
            if (temp.isEmpty()) {
                System.out.println("Invalid input\n");
                return;
            }
            if (chosenDoubleEngines.isEmpty()) {
                chosenDoubleEngines = new ArrayList<>(temp);
            }
            else {
                chosenBatteryComponents = new ArrayList<>(temp);
                int engineStrength = playerToPlay.get().calculateEngineStrength(chosenDoubleEngines, chosenBatteryComponents);
                if (engineStrength == -1) {
                    System.out.println("Engine Strength inputs are wrong: " +
                            "input again chosen double engines and battery components\n");
                    chosenBatteryComponents = new ArrayList<>();
                    chosenDoubleEngines = new ArrayList<>();
                    return;
                }
                game.getFlightBoard().moveForward(playerToPlay.get(), engineStrength);
                playerIndex++;
                initializeCard(game);
            }
        }
    }

    public String toString() {
        return "OpenSpace";
    }
}