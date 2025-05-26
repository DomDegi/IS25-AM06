package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;

/**
 * This interface defines a minimal contract for any class that represents a player
 * in the game by exposing their assigned player color.
 */
public interface PlayerInterface {

    /**
     * Returns the color associated with the player.
     *
     * @return the {@link PlayersColor} of the player
     */
    PlayersColor getPlayerColor();
}
