package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Represents the compatibility of a tile with alien crew types.
 * Used primarily to determine which alien (if any) can be hosted by a tile,
 * such as a Cabin or AlienLifeSupportSystem.
 */
public enum AlienOptions {

    /**
     * The tile does not support any alien crew.
     */
    NO,

    /**
     * The tile supports only brown alien crew members.
     */
    BROWN,

    /**
     * The tile supports only purple alien crew members.
     */
    PURPLE,

    /**
     * The tile supports both brown and purple alien crew members.
     */
    BOTH
}
