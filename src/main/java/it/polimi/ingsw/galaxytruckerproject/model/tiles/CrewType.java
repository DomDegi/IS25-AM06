package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Enum representing the type of crew that can be assigned to a cabin.
 * Includes special values to represent non-cabin or unsupported tiles.
 */
public enum CrewType {

    /**
     * Standard human crew member.
     */
    HUMAN,

    /**
     * Purple alien crew member, requires appropriate support system.
     */
    PURPLE,

    /**
     * Brown alien crew member, requires appropriate support system.
     */
    BROWN,

    /**
     * Indicates the tile is not a cabin.
     */
    NotAcabin,

    /**
     * Indicates the tile is not a support system for aliens.
     */
    NotSupportSystem;

    /**
     * Returns a short string representation used in serialization or display.
     *
     * @return "H" for HUMAN, "P" for PURPLE, "B" for BROWN, "/" for NotAcabin, and "▓" for NotSupportSystem
     */
    @Override
    public String toString() {
        return switch (this) {
            case HUMAN -> "H";
            case PURPLE -> "P";
            case BROWN -> "B";
            case NotAcabin -> "/";
            default -> "▓";
        };
    }

    /**
     * Converts a short string representation to the corresponding {@code CrewType}.
     *
     * @param value the serialized form of the crew type ("H", "P", "B", "/", "▓")
     * @return the corresponding CrewType enum value
     * @throws IllegalArgumentException if the input is not a valid crew type
     */
    public static CrewType fromString(String value) {
        return switch (value) {
            case "H" -> HUMAN;
            case "P" -> PURPLE;
            case "B" -> BROWN;
            case "/" -> NotAcabin;
            case "▓" -> NotSupportSystem;
            default -> throw new IllegalArgumentException("Unknown crew type: " + value);
        };
    }
}
