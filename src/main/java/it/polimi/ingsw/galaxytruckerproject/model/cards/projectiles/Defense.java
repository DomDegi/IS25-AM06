package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;

import java.io.Serializable;

/**
 * Represents the result of a projectile's interaction with a player's defenses.
 * It indicates whether the projectile was blocked, requires a battery to block, or causes damage.
 */
public enum Defense implements Serializable {

    /**
     * The projectile was successfully blocked (e.g., by a shield).
     */
    PROTECTED,

    /**
     * The player may choose to use a battery to block the projectile.
     */
    CHOOSETOUSEBATTERY,

    /**
     * The projectile hit the ship, causing damage.
     */
    HIT
}
