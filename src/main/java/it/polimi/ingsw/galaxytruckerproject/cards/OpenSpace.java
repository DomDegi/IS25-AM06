package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;
import java.util.Optional;

public class OpenSpace extends Card {
    private Optional<Player> playerTurn = Optional.empty();
    private int playerIndex = 0;
    private ArrayList<Coordinates> doubleEngines;
    private ArrayList<Coordinates> batteryComponents;
    private int totalEngineStrength = 0;
    private Optional<Coordinates> pickedEngine =  Optional.empty();

    //the subclass OpenSpace needs the same parameters as the superclass
    public OpenSpace(int level) {
        super(level, 0);
    }

    //Initialize card for the player at the index playerIndex of the flightBoard ranking
    public void initializeCard(Game game) {
        //if index is higher than the number of player in the game -1, it will go out of bounds
        if (playerIndex > game.getPlayerCount() - 1) {
            //draw next card
            game.DrawCard();
        }

        playerTurn = Optional.of(game.getListOfPlayers().get(playerIndex));
        totalEngineStrength = playerTurn.get().getShipBoard().getNumSingleEngine();
        if (totalEngineStrength > 0) {
            totalEngineStrength = totalEngineStrength + 2 * playerTurn.get().getShipBoard().getNumBrownAliens();
        }
        System.out.println("Your current engine Strength is: " + totalEngineStrength + "\n");
        doubleEngines = playerTurn.get().getShipBoard().getDoubleEngine();
        if (!doubleEngines.isEmpty()) {
            System.out.printf("You also have %d double engines. " +
                    "For every battery you use you activate a double engine and increase your engine Strength by 2\n",
                    doubleEngines.size());
            for (Coordinates coordinates : doubleEngines) {
                System.out.printf("double engine at coordinates: x: %d  y: %d\n",  coordinates.getX(), coordinates.getY());
            }
        }
    }

    //makes so that the player gain as many days as their engineStrength
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (playerTurn.isPresent() && playerTurn.get().getPlayerName().equals(playerName)) {
            if (input[0].equalsIgnoreCase("stop")) {
                System.out.printf("You will advance of as many days as your totalEngineStrength: %d\n", totalEngineStrength);
                game.getFlightBoard().moveForward();
                playerIndex++;
                initializeCard(game);
            } else {
                try {
                    int x = Integer.parseInt(input[0]);
                    int y = Integer.parseInt(input[1]);
                    Coordinates coordinates = new Coordinates(x, y);

                    if (pickedEngine.isEmpty()) {
                        if (doubleEngines.contains(coordinates)) {
                            pickedEngine = Optional.of(new Coordinates(x,y));
                            batteryComponents = playerTurn.get().getShipBoard().getBatteryCoordinates();
                            if (batteryComponents.isEmpty()) {
                                System.out.println("No full battery components found\n");
                                String[] stop = {"stop"};
                                executeCard(game, playerName, stop);
                            }
                            else {
                            System.out.printf("You have %d battery components with still batteries in them\n", batteryComponents.size());
                            for (Coordinates coord : batteryComponents) {
                                System.out.printf("Battery component: x: %d y: %d\n", coord.getX(), coord.getY());
                            }
                            }
                        }
                        else  {
                            System.out.println("Invalid coordinates for doubleEngines\n");
                        }
                    }
                    else {
                        
                    }
                } catch (NumberFormatException e) {
                    System.out.println("either input coordinates or STOP\n");
                }
            }
        }
    }
}