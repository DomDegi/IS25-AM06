package it.polimi.ingsw.galaxytruckerproject.client;

/**
 * Enum representing the different types of coordinate requests in the game.
 * <p>
 * Each value in this enum corresponds to a specific action or choice that the player
 * can make during the game, such as choosing tiles to break, selecting equipment to use,
 * or managing goods.
 * </p>
 */
public enum CoordReqType {
    /**
     * Request to choose tiles to break for correctness.
     */
    CHOOSE_TO_BREAK,

    /**
     * Request to choose tiles of the branch the player wants to maintain (for destruction).
     */
    CHOOSE_TO_MAINTAIN,

    /**
     * Request to choose a battery to use.
     */
    CHOOSE_BATTERY,

    /**
     * Request to choose double cannons to use and their associated batteries.
     */
    CHOOSE_DOUBLE_CANNON,

    /**
     * Request to choose double engines to use and their associated batteries.
     */
    CHOOSE_DOUBLE_ENGINE,

    /**
     * Request to choose the crew to remove.
     */
    CHOOSE_CREW,

    /**
     * Request to choose the goods to remove.
     */
    REMOVE_GOODS;

    /**
     * Returns a description of the action for each coordinate request type.
     *
     * @return a string description of the action associated with the coordinate request type
     */
    @Override
    public String toString() {
        switch (this) {
            case CHOOSE_BATTERY:
                return "choose your battery to use";
            case CHOOSE_TO_MAINTAIN:
                return "choose a tile of the branch you want to keep";
            case CHOOSE_TO_BREAK:
                return "choose the tiles you want to destroy";
            case CHOOSE_DOUBLE_CANNON:
                return "choose the double cannons you want to use, and their batteries";
            case CHOOSE_DOUBLE_ENGINE:
                return "choose the double engine you want to use, and their batteries";
            case CHOOSE_CREW:
                return "choose the crew you want to remove";
            case REMOVE_GOODS:
                return "choose your goods to remove";
            default:
                return "error in coord request toString";
        }
    }
}
