package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The PingPong class is responsible for regularly pinging active players to check the connectivity
 * in a multiplayer game. It is implemented as a runnable task, which can be executed in a separate
 * thread to periodically call the ping method for all active players in all the games.
 */
public class PingPong implements Runnable {

    /**
     * A map holding all the games currently active, where the key is the game name and the value
     * is the corresponding {@link GameController}.
     */
    private ConcurrentHashMap<String, GameController> games;

    /**
     * Constructs a PingPong object that will periodically check the connectivity of players in the given
     * games map.
     *
     * @param games A map of all active games, with game names as keys and game controllers as values.
     */
    public PingPong(ConcurrentHashMap<String, GameController> games) {
        this.games = games;
    }

    /**
     * Periodically sends a ping to all active players in all the games to check their connectivity.
     * The ping is sent every 15 seconds. If a player's connection is lost, they are handled accordingly
     * by the {@link GameController}.
     * <p>
     * The method will run in an infinite loop, checking every game and player, then sleeping for 15 seconds
     * before repeating the process.
     * </p>
     */
    @Override
    public void run() {
        while (true) {
            if (games != null) {
                // Iterates through each active game
                for (GameController game : games.values()) {
                    if (game.getActivePlayers() != null) {
                        // Iterates through each player in the active game
                        for (String playerName : game.getActivePlayers().keySet()) {
                            // Sends a ping to the player
                            VirtualView view = game.getViewFromNickname(playerName);
                            if (view != null) {
                                game.pingPong(playerName, view);
                            }
                        }
                    }
                }
            }

            try {
                // Sleep for 1.5 seconds before the next round of pings
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                // If the thread is interrupted, break the loop and stop the task
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
