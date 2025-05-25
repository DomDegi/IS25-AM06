package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the "Abandoned Ship" encounter card.
 * <p>
 * Players in flight may be asked to send a required number of crew members
 * in order to gain credits while delaying their flight.
 * </p>
 */
public class AbandonedShip extends Card {
    /** Number of crew members required to send on the ship. */
    private final int crewNumberRequired;
    /** Credits awarded if the player sends the crew. */
    private final int possibleCreditGains;
    /** Index tracking which player’s turn it is for this card event. */
    private int playerIndex;
    /** The current player who must decide on this card. */
    private Player playerToPlay = null;
    /** The view associated with the current player. */
    private VirtualView playersView = null;
    /** Whether the current player has accepted to send crew. */
    private boolean playerAccepted;

    /**
     * Constructs an AbandonedShip card with an image path.
     *
     * @param level                the difficulty level of the card
     * @param requiredDays         the number of days to delay if crew is sent
     * @param crewNumberRequired   how many crew members must be sent
     * @param possibleCreditGains  credits gained by sending the crew
     * @param filePath             path to the card’s image resource
     */
    @JsonCreator
    public AbandonedShip(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("crewNumberRequired") int crewNumberRequired,
            @JsonProperty("possibleCreditGains") int possibleCreditGains,
            @JsonProperty("imagePath") String filePath
    ) {
        super(level, requiredDays, filePath);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleCreditGains = possibleCreditGains;
        this.playerIndex = 0;
        this.playerAccepted = false;
    }

    /**
     * Constructs an AbandonedShip card without specifying an image path.
     *
     * @param level                the difficulty level of the card
     * @param requiredDays         the number of days to delay if crew is sent
     * @param crewNumberRequired   how many crew members must be sent
     * @param possibleCreditGains  credits gained by sending the crew
     */
    public AbandonedShip(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("crewNumberRequired") int crewNumberRequired,
            @JsonProperty("possibleCreditGains") int possibleCreditGains
    ) {
        super(level, requiredDays, null);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleCreditGains = possibleCreditGains;
        this.playerIndex = 0;
        this.playerAccepted = false;
    }

    /**
     * Initializes the card event, storing references to the game state and views,
     * and advances to the first eligible player.
     *
     * @param game      the game engine interface
     * @param viewsMap  mapping from player names to their views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    /**
     * Processes the removal of crew tiles from the current player’s board.
     * If the correct number of tiles is provided and removal succeeds, awards credits,
     * moves the flight board backward, and ends the card event.
     *
     * @param playerName    name of the player performing the removal
     * @param crewToRemove  list of tile coordinates to remove
     */
    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!playerAccepted || !player.equals(playerToPlay)) {
            try { viewsMap.get(playerName).showWrongInputMessage(); } catch(Exception ignored) {}
            return;
        }
        if (crewToRemove.size() != this.crewNumberRequired) {
            try { playersView.showWrongInputMessage(); } catch(Exception ignored) {}
            return;
        }

        ArrayList<Tile> updatedTile = player.removeCrew(crewToRemove);
        if (updatedTile != null) {
            notifyModifiedTiles(playerName, updatedTile);
            game.getFlightBoard().moveBackward(playerToPlay, requiredDays);
            notifyMovement(player);
            player.gainCredit(possibleCreditGains);
            notifyGainedCredits(playerName, player.getCredit());
            game.endCardEvent();
        } else {
            try { playersView.showWrongInputMessage(); } catch(Exception ignored) {}
        }
    }

    /**
     * Handles the player’s yes/no choice to send crew.
     * If accepted, prompts the view to ask for crew coordinates;
     * otherwise proceeds to the next player.
     *
     * @param playerName  name of the player making the choice
     * @param choice      true to send crew, false to skip
     */
    public void choice(String playerName, boolean choice) {
        if (!playerName.equals(playerToPlay.getPlayerName())) {
            try { viewsMap.get(playerName).showWrongInputMessage(); } catch(Exception ignored) {}
            return;
        }
        if (choice) {
            try { playersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW); } catch(Exception ignored) {}
            playerAccepted = true;
        } else {
            nextPlayer();
        }
    }

    /**
     * Advances to the next in-flight player who is eligible to respond to this card.
     * Skips disconnected players and those without sufficient crew.
     * Ends the event if all players have been considered.
     */
    public void nextPlayer() {
        if (playerToPlay != null) {
            playerIndex++;
        }
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }

        this.playerToPlay = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = playerToPlay.getPlayerName();
        this.playersView = viewsMap.get(playerName);

        if (playerToPlay.IsDisconnected()) {
            this.nextPlayer();
            return;
        }
        if (playerToPlay.getTotalCrew() < crewNumberRequired) {
            this.nextPlayer();
        }
        else {
            try {
                playersView.setClientState(ClientState.ACTION);
            } catch(Exception ignored) {}
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the number of crew required by this card
     */
    @Override
    public int getCrewNumber() {
        return crewNumberRequired;
    }

    /**
     * {@inheritDoc}
     *
     * @return the credits awarded by this card
     */
    @Override
    public int getGainedCredits() {
        return possibleCreditGains;
    }

    /**
     * Returns a string representation including level, delay days,
     * required crew, and possible credits.
     *
     * @return a detailed string of this card’s properties
     */
    @Override
    public String toString() {
        return "AbandonedShip: " + super.toString()
                + " crewNumberRequired " + crewNumberRequired
                + " possibleCreditsGain " + possibleCreditGains;
    }

    /**
     * Gets the player who is currently being asked to resolve this card.
     *
     * @return the current player
     */
    public Player getPlayerToPlay() {
        return playerToPlay;
    }

    /**
     * Handles a player disconnecting mid-resolution.
     * If they had accepted to send crew, applies an automatic penalty and credits,
     * moves the flight board, and ends the event; otherwise reconsiders players.
     *
     * @param playerName  name of the disconnected player
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (playerToPlay != null && playerToPlay.getPlayerName().equals(playerName)) {
            playerIndex--;
            playerAccepted = false;
            nextPlayer();
        }
    }
}
