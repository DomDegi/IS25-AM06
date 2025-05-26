package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Enum representing the different types of crew that can occupy a cabin or tile.
 */
public enum TypeOfCrew {

    /**
     * Standard human crew member, usually occupying cabins in pairs.
     */
    Human,

    /**
     * Purple alien crew member, can occupy specialized cabins or require support systems.
     */
    Purple,

    /**
     * Brown alien crew member, can also require support systems to be valid.
     */
    Brown
}
