package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class StarDust extends Card {

    public StarDust(int level) {
        super(level, 0);
    }

    //This cards doesn't need any input, so it gets instantly executed when initialized
    public void initializeCard(Game game) {
        String[] input = {"any", "input"};
        executeCard(game, game.getListOfPlayers().getFirst().getPlayerName(), input);
    }

    //makes so that the player loses as many days as their exposedConnectors
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        ArrayList<Player> players = game.getListOfPlayers();

        //when moving backward starts from the last
        for (int i = players.size() - 1; i >= 0; i--) {
            int playerExposedConnectors = players.get(i).getShipBoard().countExposedConnectors();

            if (playerExposedConnectors > 0) {
                game.getFlightBoard().moveBackward(players.get(i), playerExposedConnectors);
                System.out.println(players.get(i).getPlayerName() + " moved backward as many steps as their exposed connectors: " + playerExposedConnectors + "\n");
            }
            else {
                System.out.println(players.get(i).getPlayerName() + " has no exposed connectors. StarDust doesn't affect them\n");
            }
        }
    }

    @Override
    public String toString(){
        return "StarDust";
    }
}

