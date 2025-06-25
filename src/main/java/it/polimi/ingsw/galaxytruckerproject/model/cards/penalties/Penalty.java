package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Defense;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Abstract class representing a generic penalty applied to a player during the game.
 * Subclasses define specific behaviors (e.g., loss of crew, goods, or being hit by a projectile).
 * Penalties can be interactive (require player response) or automatic (for disconnected players).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrewPenalty.class, name = "CrewPenalty"),
        @JsonSubTypes.Type(value = ProjectilePenalty.class, name = "CannonPenalty"),
        @JsonSubTypes.Type(value = GoodsPenalty.class, name = "GoodsPenalty"),
        @JsonSubTypes.Type(value = FlightDaysPenalty.class, name = "FlightDaysPenalty")
})
public abstract class Penalty implements Serializable {

    /** Reference to the game engine (used to access game state). */
    GameInterface game;

    /**
     * Returns a string representation of the penalty.
     *
     * @return A human-readable description of the penalty.
     */
    @Override
    public abstract String toString();

    /**
     * Applies the effect of a projectile hit or miss, if applicable.
     *
     * @param view   The virtual view of the affected player.
     * @param player The player receiving the penalty.
     * @return The coordinates of the affected tile, or null if not applicable.
     */
    public Coordinates hitOrMiss(VirtualView view, Player player) { return null; }

    /**
     * Initializes the penalty, prompting the user if needed.
     *
     * @param game   Reference to the game engine.
     * @param view   The view associated with the player.
     * @param player The player to apply the penalty to.
     * @return True if further interaction is expected; false if the penalty is resolved.
     */
    public abstract boolean initializePenalty(GameInterface game, VirtualView view, Player player);

    /**
     * Removes goods from the player's ship.
     *
     * @param player    The player penalized.
     * @param view      View to notify.
     * @param toRemove  Coordinates from which to remove goods.
     * @return List of tiles affected (can be empty or null).
     */
    public ArrayList<Tile> removeGoods(Player player, VirtualView view, ArrayList<Coordinates> toRemove) {
        return null;
    }

    /**
     * Removes crew members from the player's ship.
     *
     * @param player    The player penalized.
     * @param view      View to notify.
     * @param toRemove  Coordinates from which to remove crew.
     * @return List of tiles affected (can be empty or null).
     */
    public ArrayList<Tile> removeCrew(Player player, VirtualView view, ArrayList<Coordinates> toRemove) {
        return null;
    }

    /**
     * Automatically applies a goods penalty for a disconnected player.
     *
     * @param disconnectedPlayer The disconnected player.
     * @param view               The view to update.
     * @return List of affected tiles (can be empty or null).
     */
    public ArrayList<Tile> automaticGoodsPenalty(Player disconnectedPlayer, ViewInterface view) {
        return null;
    }

    /**
     * Automatically applies a crew penalty for a disconnected player.
     *
     * @param disconnectedPlayer The disconnected player.
     * @param view               The view to update.
     * @return List of affected tiles (can be empty or null).
     */
    public ArrayList<Tile> automaticCrewPenalty(Player disconnectedPlayer, ViewInterface view) {
        return null;
    }

    /**
     * Performs a dice roll and applies it to the penalty logic.
     *
     * @param view   The virtual view for interaction.
     * @param player The player affected.
     * @return Coordinates of affected tile if any.
     */
    public Coordinates randomRollForOne(VirtualView view, Player player) { return null; }

    /**
     * Handles the choice of which ship branch to maintain after a disconnect.
     *
     * @param player   The player making the choice.
     * @param received Coordinates representing the chosen branch.
     * @return List of coordinates of tiles to remove.
     */
    public ArrayList<Coordinates> chooseToMaintain(Player player, ArrayList<Coordinates> received){
        return null;
    }

    /**
     * Handles the use of batteries for defense by the player.
     *
     * @param player    The player defending.
     * @param batteries Coordinates of batteries to use.
     * @return The tile modified if battery use is valid; null otherwise.
     */
    public Tile playerUsesBatteryToDefend(Player player, ArrayList<Coordinates> batteries) { return null; }

    /**
     * Returns the value of the dice roll associated with this penalty, if any.
     *
     * @return The dice roll value.
     */
    public int getDiceRoll() { return 0; }

    /**
     * Returns the defense status (e.g., HIT, PROTECTED, etc.) if relevant.
     *
     * @return The defense status.
     */
    public Defense getDefenseStatus() { return null; }

    /**
     * Returns the ship branches available for selection after damage.
     *
     * @return List of coordinate sets representing branches.
     */
    public ArrayList<Set<Coordinates>> getBranch() { return null; }

    /**
     * Returns the tile that was destroyed, if applicable.
     *
     * @return Coordinates of the destroyed tile.
     */
    public Coordinates getDestroyedTile() { return null; }

    public int getNumberOfCrew() { return 0; }
}
