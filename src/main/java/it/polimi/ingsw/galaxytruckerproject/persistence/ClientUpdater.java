package it.polimi.ingsw.galaxytruckerproject.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.StringWriter;
/**
 * Utility class used to generate a serialized snapshot of the current game state
 * to be sent to clients for synchronization purposes.
 *
 * This class cannot be instantiated.
 */
public class ClientUpdater {
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ClientUpdater(){}
    /**
     * Serializes the current game state, including controller state, game model,
     * and all players, into a single string.
     *
     * @param gameController the game controller containing game and player information
     * @return a string representing the serialized game state for client update
     */
    public static String currentGameStatus(GameController gameController) {
        StringWriter sw = new StringWriter();

        ControllerSerializerDeserializer.saveForUpdates(gameController,sw);
        GameSerializerDeserializer.saveForUpdates(gameController.getGame(),sw);

        for (Player player: gameController.getAllPlayers()) {
            PlayerSerializerDeserializer.save(player,sw);
        }
        return sw.toString();
    }
}