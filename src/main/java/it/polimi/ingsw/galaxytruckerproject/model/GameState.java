package it.polimi.ingsw.galaxytruckerproject.model;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the various phases/states of a Galaxy Trucker game.
 * Each state is associated with a single-character code used for serialization/deserialization.
 *
 * <ul>
 *     <li>{@code LOBBY_PHASE} - Players are connecting and waiting in the lobby</li>
 *     <li>{@code START_GAME} - Game setup is initialized</li>
 *     <li>{@code SHIPS_CREATION} - Players build their ships</li>
 *     <li>{@code VERIFY_SHIP_CORRECTNESS} - Ships are verified for correctness</li>
 *     <li>{@code DRAW_CARD} - A new card is drawn from the deck</li>
 *     <li>{@code CARD_EVENT} - An event or challenge is executed</li>
 *     <li>{@code CONCLUDE_GAME} - Game is ending and final scores are calculated</li>
 * </ul>
 */
public enum GameState {
    LOBBY_PHASE("L"),
    START_GAME("S"),
    SHIPS_CREATION("C"),
    VERIFY_SHIP_CORRECTNESS("V"),
    DRAW_CARD("D"),
    CARD_EVENT("E"),
    CONCLUDE_GAME("G");

    /**
     * The short code associated with the game state, used for compact serialization.
     */
    private final String code;

    /**
     * A static map used to convert code strings back into their corresponding GameState.
     */
    private static final Map<String, GameState> fromCodeMap = new HashMap<>();

    static {
        for (GameState state : GameState.values()) {
            fromCodeMap.put(state.code, state);
        }
    }

    /**
     * Constructs a GameState with its associated short code.
     *
     * @param code the short string representing the state
     */
    GameState(String code) {
        this.code = code;
    }

    /**
     * Returns the string code representing this game state.
     *
     * @return a single-character string code
     */
    @Override
    public String toString() {
        return code;
    }

    /**
     * Converts a string code to the corresponding GameState.
     *
     * @param code the short string code (e.g., "L", "S", etc.)
     * @return the corresponding GameState
     * @throws IllegalArgumentException if the code is not recognized
     */
    public static GameState fromString(String code) {
        GameState state = fromCodeMap.get(code);
        if (state == null) {
            throw new IllegalArgumentException("Unknown GameState code: " + code);
        }
        return state;
    }
}

