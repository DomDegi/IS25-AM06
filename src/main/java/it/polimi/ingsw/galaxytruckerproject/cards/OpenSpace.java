package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;

public class OpenSpace extends Card {
    private Player currentPlayer = null;
    private int playerIndex = 0;
    ArrayList<Player> playersToEarlyLand = new ArrayList<>();
    

    //the subclass OpenSpace needs the same parameters as the superclass
    public OpenSpace(int level) {
        super(level, 0);
    }

    //Initialize card for the player at the index playerIndex of the flightBoard ranking
    @Override
    public void initializeCard(Game game) {
        //if index is higher than the number of player in the game -1, it will go out of bounds
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            for (Player player: playersToEarlyLand) {
                game.getFlightBoard().earlyLanding(player);
            }
            game.getFlightBoard().concludeMovement();
            //draw next card
            game.endCardEvent();
        }

        currentPlayer = game.getListOfPlayers().get(playerIndex);
        currentPlayer.printCurrentInfoEngines();
        currentPlayer.printCurrentInfoBatteries();
    }

    //makes so that the player gain as many days as their engineStrength
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        //playerName has to be the same as the current one to face the card's adventure
        if (currentPlayer != null && currentPlayer.getPlayerName().equalsIgnoreCase(playerName)) {

            //decides against using double engines
            if (input[0].equals("no")) {
                int engineStrength = currentPlayer.useDoubleEngines(new ArrayList<>());
                MoveOrEarlyLand(game, engineStrength);
            }
            else {
                ArrayList<Coordinates> coordinates =  new ArrayList<>(currentPlayer.parseCoordinates(input));
                if (coordinates.isEmpty()) {
                    return;
                }
                int engineStrength = currentPlayer.useDoubleEngines(coordinates);
                //one more input for batteries
                if (engineStrength == -2) {
                    return;
                }
                //both inputs were wrong
                if (engineStrength == -1) {
                    return;
                }
                MoveOrEarlyLand(game, engineStrength);
            }
        }
    }

    //moves player forward; early lands if engineStrength == 0
    private void MoveOrEarlyLand(Game game, int engineStrength) {
        if (engineStrength == 0) {
            playersToEarlyLand.add(currentPlayer);
            playerIndex++;
            initializeCard(game);
            return;
        }
        game.getFlightBoard().moveForward(currentPlayer, engineStrength);
        playerIndex++;
        initializeCard(game);
    }

    public String toString() {
        return "OpenSpace";
    }
}