package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a light version of the flight board in the game.
 * It contains a list of players currently in the game and provides methods to manage them.
 * <p>
 * The `LightFlightboard` is designed to hold a simplified version of the flight board data,
 * allowing for efficient handling of player data during the game.
 * </p>
 */
public class LightFlightboard implements Serializable {

    /**
     * A list of players currently in the game.
     */
    private final ArrayList<LightPlayer> inGamePlayers;

    /**
     * Constructs a `LightFlightboard` from the given `FlightBoard` instance.
     * The `FlightBoard` is used to initialize the list of players currently in the game.
     *
     * @param flightBoard the `FlightBoard` to extract player data from
     */
    public LightFlightboard(FlightBoard flightBoard) {
        this.inGamePlayers = new ArrayList<>();
        for (Player player : flightBoard.getAllPlayers()) {
            LightPlayer inGamePlayer = new LightPlayer(player);
            inGamePlayers.add(inGamePlayer);
        }
    }

    /**
     * Returns the list of players currently in the game.
     *
     * @return the list of players in the game
     */
    public ArrayList<LightPlayer> getInGamePlayers() {
        return inGamePlayers;
    }

    /**
     * Returns the `LightPlayer` corresponding to the specified player name.
     * If no player with the given name is found, `null` is returned.
     *
     * @param playerName the name of the player to find
     * @return the `LightPlayer` object if found, `null` otherwise
     */
    public LightPlayer getInGamePlayer(String playerName) {
        for (LightPlayer player : inGamePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Adds a new player to the list of players in the game if the number of players does not exceed 4.
     *
     * @param player the `LightPlayer` to add
     */
    public void addInGamePlayer(LightPlayer player) {
        if (inGamePlayers.size() <= 4) {
            inGamePlayers.add(player);
        }
    }
}
