package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Direction;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.io.Serializable;
import java.util.Optional;

/**
 * Abstract class representing a generic projectile fired during gameplay.
 * Each projectile travels in a specific direction and may damage a tile on the player's ship.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LargeMeteor.class, name = "LargeMeteor"),
        @JsonSubTypes.Type(value = LargeCannonShot.class, name = "LargeCannonShot"),
        @JsonSubTypes.Type(value = SmallMeteor.class, name = "SmallMeteor"),
        @JsonSubTypes.Type(value = SmallCannonShot.class, name = "SmallCannonShot")
})
public abstract class Projectile implements Serializable {

    /**
     * The direction the projectile travels in.
     */
    protected Direction direction;

    /**
     * The result of the dice roll determining projectile impact location.
     */
    protected int diceRoll;

    /**
     * The coordinates of the tile to be destroyed by the projectile.
     */
    protected Coordinates coordinatesToDestroy = null;

    /**
     * Constructs a projectile with the specified direction.
     *
     * @param direction the direction in which the projectile travels
     */
    public Projectile(Direction direction) {
        this.direction = direction;
    }

    /**
     * Returns the direction of the projectile.
     *
     * @return the direction of travel
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Applies the projectile effect to a player's ship.
     * Should be overridden by concrete subclasses.
     *
     * @param player    the player being targeted
     * @param DiceRoll  the dice roll that determines the path
     * @param game      the game interface context
     * @return the result of the defense (e.g., HIT, PROTECTED)
     */
    public Defense throwProjectile(Player player, int DiceRoll, GameInterface game) {
        System.out.println("You shouldn't be here ninja");
        return Defense.PROTECTED;
    }

    /**
     * Returns the coordinates of the tile to be destroyed by the projectile.
     *
     * @return the target coordinates, or (0,0) if none assigned
     */
    public Coordinates getCoordinatesToDestroy() {
        if (coordinatesToDestroy == null) {
            return new Coordinates(0, 0);
        } else {
            return coordinatesToDestroy;
        }
    }

    /**
     * Determines the tile hit by the projectile based on its direction and dice roll.
     * Updates {@code coordinatesToDestroy} if a valid tile is found.
     *
     * @param player   the player being targeted
     * @param diceRoll the result of the dice roll
     */
    protected void Throw(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile> Temp;
        Optional<Tile>[][] tileTable = ship.getTilesTable();
        int i;

        if (direction == Direction.SOUTH) {
            if (diceRoll < 4 || diceRoll > 10) {
                coordinatesToDestroy = null;
                return;
            }
            diceRoll = diceRoll - 4;
            i = 4;
            Temp = tileTable[i][diceRoll];
            while (Temp.isEmpty() || !Temp.get().fillable()) {
                i--;
                if (i < 0) {
                    coordinatesToDestroy = null;
                    return;
                }
                Temp = tileTable[i][diceRoll];
            }
            coordinatesToDestroy = Temp.get().getCoordinates();
            return;
        }

        if (direction == Direction.NORTH) {
            if (diceRoll < 4 || diceRoll > 10) {
                coordinatesToDestroy = null;
                return;
            }
            diceRoll = diceRoll - 4;
            i = 0;
            Temp = tileTable[i][diceRoll];
            while (Temp.isEmpty() || !Temp.get().fillable()) {
                i++;
                if (i > 4) {
                    coordinatesToDestroy = null;
                    return;
                }
                Temp = tileTable[i][diceRoll];
            }
            System.out.println("Ain't no luck baby");
            coordinatesToDestroy = Temp.get().getCoordinates();
            System.out.println(coordinatesToDestroy);
            return;
        }

        if (direction == Direction.EAST) {
            if (diceRoll < 5 || diceRoll > 9) {
                coordinatesToDestroy = null;
                return;
            }
            diceRoll = diceRoll - 5;
            i = 6;
            Temp = tileTable[diceRoll][i];
            while (Temp.isEmpty() || !Temp.get().fillable()) {
                i--;
                if (i < 0) {
                    coordinatesToDestroy = null;
                    return;
                }
                Temp = tileTable[diceRoll][i];
            }
            coordinatesToDestroy = Temp.get().getCoordinates();
            return;
        }

        if (direction == Direction.WEST) {
            if (diceRoll < 5 || diceRoll > 9) {
                coordinatesToDestroy = null;
                return;
            }
            diceRoll = diceRoll - 5;
            i = 0;
            Temp = tileTable[diceRoll][i];
            while (Temp.isEmpty() || !Temp.get().fillable()) {
                i++;
                if (i > 6) {
                    coordinatesToDestroy = null;
                    return;
                }
                Temp = tileTable[diceRoll][i];
            }
            coordinatesToDestroy = Temp.get().getCoordinates();
            return;
        }

        coordinatesToDestroy = null;
    }

    /**
     * Returns a string representation of the projectile.
     *
     * @return a string identifying the projectile
     */
    @Override
    public abstract String toString();
}
