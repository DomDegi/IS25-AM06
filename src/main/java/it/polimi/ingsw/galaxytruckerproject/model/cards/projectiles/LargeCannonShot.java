package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Direction;

/**
 * Represents a projectile fired by a large cannon.
 * The projectile travels in a specified direction and attempts to damage the player's ship.
 */
public class LargeCannonShot extends Projectile {

    /**
     * Constructs a LargeCannonShot with the specified direction.
     *
     * @param direction the direction in which the projectile travels
     */
    @JsonCreator
    public LargeCannonShot(@JsonProperty("direction") Direction direction) {
        super(direction);
    }

    /**
     * Attempts to apply the projectile effect to the given player based on the dice roll.
     * If no tile is hit, the projectile is considered blocked.
     *
     * @param player    the target player
     * @param DiceRoll  the result of the dice roll
     * @param game      the game interface
     * @return the result of the defense (hit or protected)
     */
    @Override
    public Defense throwProjectile(Player player, int DiceRoll, GameInterface game) {
        Throw(player, DiceRoll);
        if (coordinatesToDestroy == null) {
            return Defense.PROTECTED;
        }
        return Defense.HIT;
    }

    /**
     * Returns a string representation of the projectile.
     *
     * @return a string describing the large cannon shot and its direction
     */
    @Override
    public String toString() {
        return "Large CannonShot " + direction + " ";
    }
}
