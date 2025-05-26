package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the "StarDust" encounter card.
 * <p>
 * When executed, each in‐flight player’s ship is delayed by a number of days
 * equal to the count of their ship’s exposed connectors. Players are processed
 * in reverse turn order so that the last player is delayed first.
 * </p>
 */
public class StarDust extends Card {

    /**
     * Constructs a StarDust card with an image.
     *
     * @param level     the difficulty level of the card
     * @param filePath  path to the card’s image resource
     */
    @JsonCreator
    public StarDust(@JsonProperty("level") int level,
                    @JsonProperty("imagePath") String filePath) {
        super(level, 0, filePath);
    }

    /**
     * Constructs a StarDust card without specifying an image.
     *
     * @param level  the difficulty level of the card
     */
    public StarDust(@JsonProperty("level") int level) {
        super(level, 0, null);
    }

    /**
     * Initializes the card event by storing references to the game and views,
     * then immediately executes the Stardust effect.
     *
     * @param game      the game engine interface
     * @param viewsMap  mapping from player names to their virtual views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        executeCard();
    }

    /**
     * Executes the Stardust effect on all in-flight players:
     * <ul>
     *   <li>Iterates through the list of players in reverse order.</li>
     *   <li>For each player, counts the number of exposed connectors on their ship.</li>
     *   <li>Moves their flight board backward by that count of days.</li>
     *   <li>Notifies the view of movement if any delay occurred.</li>
     * </ul>
     * After processing all players, ends the card event.
     */
    public void executeCard() {
        ArrayList<Player> players = game.getListOfInFlightPlayers();
        for (int i = players.size() - 1; i >= 0; i--) {
            Player currentPlayer = players.get(i);
            int exposedCount = currentPlayer.getShipBoard().countExposedConnectors();
            // Delay the player's flight by the number of exposed connectors
            game.getFlightBoard().moveBackward(currentPlayer, exposedCount);
            if (exposedCount > 0) {
                notifyMovement(currentPlayer);
            }
        }
        game.endCardEvent();
    }

    /**
     * Returns a string representation including the card type and its ID.
     *
     * @return a descriptive string of this card
     */
    @Override
    public String toString() {
        return "StarDust id " + id;
    }
}
