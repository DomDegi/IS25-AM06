package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the "Epidemic" encounter card.
 * <p>
 * When executed, this card inflicts an epidemic on all in-flight ships,
 * causing each player’s ship to suffer an outbreak of disease in their cabins.
 * All affected tiles are updated and notified, and then the event ends.
 * </p>
 */
public class Epidemic extends Card {

    /**
     * Constructs an Epidemic card with a specified image.
     *
     * @param level     the difficulty level of the card
     * @param filePath  path to the card’s image resource
     */
    @JsonCreator
    public Epidemic(@JsonProperty("level") int level,
                    @JsonProperty("imagePath") String filePath) {
        super(level, 0, filePath);
    }

    /**
     * Constructs an Epidemic card without specifying an image.
     *
     * @param level  the difficulty level of the card
     */
    public Epidemic(@JsonProperty("level") int level) {
        super(level, 0);
    }

    /**
     * Initializes the card event by storing references to the game and views,
     * then immediately executes the epidemic effect.
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
     * Executes the epidemic effect on all in-flight players:
     * for each player, triggers an epidemic on their ship board,
     * notifies the view of modified cabin tiles, and then ends the event.
     */
    public void executeCard() {
        for (Player player : game.getListOfInFlightPlayers()) {
            ArrayList<Tile> modifiedCabins = player.getShipBoard().epidemic();
            notifyModifiedTiles(player.getPlayerName(), modifiedCabins);
        }
        game.endCardEvent();
    }

    /**
     * Returns a string representation including the card type and base properties.
     *
     * @return a descriptive string of this card
     */
    @Override
    public String toString() {
        return "Epidemic " + super.toString();
    }
}
