package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.util.Optional;

/**
 * Represents a small meteor projectile.
 * Determines whether the meteor hits the ship, is blocked by a smooth connector,
 * or can be deflected by shields.
 */
public class SmallMeteor extends Projectile {

    /**
     * Constructs a SmallMeteor with the specified direction.
     *
     * @param direction the direction from which the meteor approaches
     */
    @JsonCreator
    public SmallMeteor(@JsonProperty("direction") Direction direction) {
        super(direction);
    }

    /**
     * Applies the meteor effect to the player's ship, based on direction and ship defenses.
     *
     * @param player    the player being targeted
     * @param diceRoll  the dice roll determining impact location
     * @param game      the game context (not used here)
     * @return the result of the defense (PROTECTED, CHOOSETOUSEBATTERY, or HIT)
     */
    @Override
    public Defense throwProjectile(Player player, int diceRoll, GameInterface game) {
        Throw(player, diceRoll);

        if (coordinatesToDestroy == null) {
            return Defense.PROTECTED;
        }

        if (this.direction.equals(Direction.NORTH)) {
            return checkNorth(player, diceRoll);
        } else if (this.direction.equals(Direction.SOUTH)) {
            return checkSouth(player, diceRoll);
        } else if (this.direction.equals(Direction.EAST)) {
            return checkEast(player, diceRoll);
        } else if (this.direction.equals(Direction.WEST)) {
            return checkWest(player, diceRoll);
        }

        return Defense.PROTECTED;
    }

    /**
     * Checks defense against a meteor coming from the north.
     * Returns PROTECTED if connector is smooth, CHOOSETOUSEBATTERY if shields cover the area,
     * otherwise HIT.
     *
     * @param player   the target player
     * @param diceRoll the dice roll (unused here)
     * @return the defense outcome
     */
    private Defense checkNorth(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getTile(coordinatesToDestroy).getNorth().getConnectorsType() == Connectors.SMOOTH) {
            return Defense.PROTECTED;
        }
        if (ship.getCoverageShields().contains(Coverage.NORTH_EAST) ||
                ship.getCoverageShields().contains(Coverage.NORTH_WEST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Checks defense against a meteor coming from the south.
     *
     * @param player   the target player
     * @param diceRoll the dice roll (unused here)
     * @return the defense outcome
     */
    private Defense checkSouth(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getTile(coordinatesToDestroy).getSouth().getConnectorsType() == Connectors.SMOOTH) {
            return Defense.PROTECTED;
        }
        if (ship.getCoverageShields().contains(Coverage.SOUTH_EAST) ||
                ship.getCoverageShields().contains(Coverage.SOUTH_WEST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Checks defense against a meteor coming from the east.
     *
     * @param player   the target player
     * @param diceRoll the dice roll (unused here)
     * @return the defense outcome
     */
    private Defense checkEast(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getTile(coordinatesToDestroy).getEast().getConnectorsType() == Connectors.SMOOTH) {
            return Defense.PROTECTED;
        }
        if (ship.getCoverageShields().contains(Coverage.SOUTH_EAST) ||
                ship.getCoverageShields().contains(Coverage.NORTH_EAST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Checks defense against a meteor coming from the west.
     *
     * @param player   the target player
     * @param diceRoll the dice roll (unused here)
     * @return the defense outcome
     */
    private Defense checkWest(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if (ship.getTile(coordinatesToDestroy).getWest().getConnectorsType() == Connectors.SMOOTH) {
            return Defense.PROTECTED;
        }
        if (ship.getCoverageShields().contains(Coverage.SOUTH_WEST) ||
                ship.getCoverageShields().contains(Coverage.NORTH_WEST)) {
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }

    /**
     * Returns a string representation of the small meteor.
     *
     * @return a string describing the projectile and its direction
     */
    @Override
    public String toString() {
        return "Small Meteor " + direction + " ";
    }
}
