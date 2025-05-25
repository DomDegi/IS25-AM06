package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Direction;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.Optional;

/**
 * Represents a large meteor projectile traveling in a specific direction.
 * It determines the outcome of the impact based on the ship's defenses
 * and game mode, returning the corresponding {@link Defense} status.
 */
public class LargeMeteor extends Projectile {

    /**
     * Creates a LargeMeteor projectile with the specified direction.
     *
     * @param direction the direction of meteor travel
     */
    @JsonCreator
    public LargeMeteor(@JsonProperty("direction") Direction direction) {
        super(direction);
    }

    /**
     * Applies the effect of the large meteor on the player's ship based on the dice roll.
     * The logic varies depending on the direction and game mode (e.g., TRIAL).
     *
     * @param player   the target player
     * @param diceRoll the result of the dice roll
     * @param game     the game interface
     * @return the result of the defense (HIT, PROTECTED, or CHOOSETOUSEBATTERY)
     */
    public Defense throwProjectile(Player player, int diceRoll, GameInterface game) {
        Throw(player, diceRoll);

        if (coordinatesToDestroy == null) {
            return Defense.PROTECTED;
        } else if (game.getMode().equals(GameMode.TRIAL)) {
            if (direction.equals(Direction.NORTH))
                return checkNorthLev1(player, diceRoll);
            if (direction.equals(Direction.SOUTH))
                return checkSouthLev1(player, diceRoll);
            if (direction.equals(Direction.EAST))
                return checkEastLev1(player, diceRoll);
            if (direction.equals(Direction.WEST))
                return checkWestLev1(player, diceRoll);
        }

        if (direction.equals(Direction.NORTH)) {
            return checkNorthLev1(player, diceRoll);
        }
        if (direction.equals(Direction.SOUTH)) {
            return checkSouthLev1(player, diceRoll);
        }
        if (direction.equals(Direction.EAST)) {
            return checkEastLev2(player, diceRoll);
        }
        return checkWestLev2(player, diceRoll);
    }

    /**
     * Checks defense on the north side using trial level 1 logic.
     */
    private Defense checkNorthLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile>[][] tileTable = ship.getTilesTable();
        boolean doubleCannon = false;
        diceRoll -= 4;

        for (int i = 0; i <= 4; i++) {
            Optional<Tile> temp = tileTable[i][diceRoll];
            if (temp.isPresent() && temp.get().fillable()) {
                if (temp.get().getStrength() == 2)
                    doubleCannon = true;
                else if (temp.get().getStrength() == -2) {
                    return Defense.PROTECTED;
                }
            }
        }

        return doubleCannon ? Defense.CHOOSETOUSEBATTERY : Defense.HIT;
    }

    /**
     * Checks defense on the south side using trial level 1 logic.
     */
    private Defense checkSouthLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile>[][] tileTable = ship.getTilesTable();
        boolean doubleCannon = false;
        diceRoll -= 4;

        for (int i = 4; i >= 0; i--) {
            Optional<Tile> temp = tileTable[i][diceRoll];
            if (temp.isPresent() && temp.get().fillable()) {
                if (temp.get().getStrength() == 1 && temp.get().getDirection() == Direction.SOUTH)
                    doubleCannon = true;
                else if (temp.get().getStrength() == -1 && temp.get().getDirection() == Direction.SOUTH) {
                    return Defense.PROTECTED;
                }
            }
        }

        return doubleCannon ? Defense.CHOOSETOUSEBATTERY : Defense.HIT;
    }

    /**
     * Checks defense on the east side using trial level 1 logic.
     */
    private Defense checkEastLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile>[][] tileTable = ship.getTilesTable();
        boolean doubleCannon = false;
        diceRoll -= 5;

        for (int i = 6; i >= 0; i--) {
            Optional<Tile> temp = tileTable[diceRoll][i];
            if (temp.isPresent() && temp.get().fillable()) {
                if (temp.get().getStrength() == 1 && temp.get().getDirection() == Direction.EAST)
                    doubleCannon = true;
                else if (temp.get().getStrength() == -1 && temp.get().getDirection() == Direction.EAST) {
                    return Defense.PROTECTED;
                }
            }
        }

        return doubleCannon ? Defense.CHOOSETOUSEBATTERY : Defense.HIT;
    }

    /**
     * Checks defense on the west side using trial level 1 logic.
     */
    private Defense checkWestLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile>[][] tileTable = ship.getTilesTable();
        boolean doubleCannon = false;
        diceRoll -= 5;

        for (int i = 0; i <= 6; i++) {
            Optional<Tile> temp = tileTable[diceRoll][i];
            if (temp.isPresent() && temp.get().fillable()) {
                if (temp.get().getStrength() == 1 && temp.get().getDirection() == Direction.WEST)
                    doubleCannon = true;
                else if (temp.get().getStrength() == -1 && temp.get().getDirection() == Direction.WEST) {
                    return Defense.PROTECTED;
                }
            }
        }

        return doubleCannon ? Defense.CHOOSETOUSEBATTERY : Defense.HIT;
    }

    /**
     * Checks defense on the east side using extended (level 2) logic.
     * Considers adjacent lanes for improved protection.
     */
    private Defense checkEastLev2(Player player, int diceRoll) {
        Defense defense = checkEastLev1(player, diceRoll);
        if (defense == Defense.PROTECTED) return defense;

        if (defense == Defense.CHOOSETOUSEBATTERY) {
            for (int offset : new int[]{1, -1}) {
                if ((diceRoll + offset) >= 0 && (diceRoll + offset) <= 9) {
                    Defense temp = checkEastLev1(player, diceRoll + offset);
                    if (temp == Defense.PROTECTED) return temp;
                    if (temp == Defense.CHOOSETOUSEBATTERY) defense = temp;
                }
            }
        }
        return defense;
    }

    /**
     * Checks defense on the west side using extended (level 2) logic.
     * Considers adjacent lanes for improved protection.
     */
    private Defense checkWestLev2(Player player, int diceRoll) {
        Defense defense = checkWestLev1(player, diceRoll);
        if (defense == Defense.PROTECTED) return defense;

        if (defense == Defense.CHOOSETOUSEBATTERY) {
            for (int offset : new int[]{1, -1}) {
                if ((diceRoll + offset) >= 0 && (diceRoll + offset) <= 9) {
                    Defense temp = checkWestLev1(player, diceRoll + offset);
                    if (temp == Defense.PROTECTED) return temp;
                    if (temp == Defense.CHOOSETOUSEBATTERY) defense = temp;
                }
            }
        }
        return defense;
    }

    /**
     * Returns the direction of the meteor.
     *
     * @return the direction in which the meteor travels
     */
    @Override
    public Direction getDirection() {
        return super.getDirection();
    }

    /**
     * Returns a string representation of this meteor.
     *
     * @return description including meteor type and direction
     */
    @Override
    public String toString() {
        return "Large Meteor " + direction + " ";
    }
}
