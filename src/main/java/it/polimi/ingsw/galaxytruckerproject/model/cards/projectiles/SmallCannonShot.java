package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Direction;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;

/**
 * Represents a small cannon projectile shot in a given direction.
 * Evaluates whether the shot hits or can be blocked using shield coverage.
 */
public class SmallCannonShot extends Projectile {

    /**
     * Constructs a SmallCannonShot with the specified direction.
     *
     * @param direction the direction in which the projectile travels
     */
    @JsonCreator
    public SmallCannonShot(@JsonProperty("direction") Direction direction) {
        super(direction);
    }

    /**
     * Applies the projectile effect to the player's ship.
     * Determines the hit result based on ship shield coverage.
     *
     * @param player    the targeted player
     * @param diceRoll  the dice roll value
     * @param game      the game context (not used in this implementation)
     * @return the result of the defense (PROTECTED, CHOOSETOUSEBATTERY, or HIT)
     */
    @Override
    public Defense throwProjectile(Player player, int diceRoll, GameInterface game) {
        Throw(player, diceRoll);

        if (coordinatesToDestroy == null) {
            // Missed the ship entirely
            return Defense.PROTECTED;
        }

        if (direction.equals(Direction.NORTH)) {
            return checkNorth(player, diceRoll);
        }
        if (direction.equals(Direction.SOUTH)) {
            return checkSouth(player, diceRoll);
        }
        if (direction.equals(Direction.EAST)) {
            return checkEast(player, diceRoll);
        }
        return checkWest(player, diceRoll);
    }

    /**
     * Checks if the ship has shield coverage for a shot from the north.
     */
    private Defense checkNorth(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getCoverageShields().contains(Coverage.NORTH_EAST) ||
                ship.getCoverageShields().contains(Coverage.NORTH_WEST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Checks if the ship has shield coverage for a shot from the south.
     */
    private Defense checkSouth(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getCoverageShields().contains(Coverage.SOUTH_EAST) ||
                ship.getCoverageShields().contains(Coverage.SOUTH_WEST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Checks if the ship has shield coverage for a shot from the east.
     */
    private Defense checkEast(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getCoverageShields().contains(Coverage.SOUTH_EAST) ||
                ship.getCoverageShields().contains(Coverage.NORTH_EAST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Checks if the ship has shield coverage for a shot from the west.
     */
    private Defense checkWest(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getCoverageShields().contains(Coverage.SOUTH_WEST) ||
                ship.getCoverageShields().contains(Coverage.NORTH_WEST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Returns a string representation of the projectile.
     *
     * @return a string describing the projectile and its direction
     */
    @Override
    public String toString() {
        return "Small CannonShot " + direction + " ";
    }
}
