package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import java.util.ArrayList;

public class LightFlightboard {

    private ArrayList<LightPlayer> inGamePlayers;

    public LightFlightboard() {

    }
    public ArrayList<LightPlayer> getInGamePlayers() {
        return inGamePlayers;

    }

    public LightPlayer getInGamePlayer(String playerName) {
        for (LightPlayer player : inGamePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }
}
