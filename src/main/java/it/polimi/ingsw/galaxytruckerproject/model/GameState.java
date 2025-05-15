package it.polimi.ingsw.galaxytruckerproject.model;

import java.util.HashMap;
import java.util.Map;

public enum GameState {
    LOBBY_PHASE("L"),
    START_GAME("S"),
    SHIPS_CREATION("C"),
    VERIFY_SHIP_CORRECTNESS("V"),
    DRAW_CARD("D"),
    CARD_EVENT("E"),
    CONCLUDE_GAME("G");

    private final String code;

    private static final Map<String, GameState> fromCodeMap = new HashMap<>();

    static {
        for (GameState state : GameState.values()) {
            fromCodeMap.put(state.code, state);
        }
    }

    GameState(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static GameState fromString(String code) {
        GameState state = fromCodeMap.get(code);
        if (state == null) {
            throw new IllegalArgumentException("Unknown GameState code: " + code);
        }
        return state;
    }
}

