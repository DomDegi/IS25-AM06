package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import java.util.ArrayList;

public class LightFlightboard {

    private ArrayList<LightPlayer> inGamePlayers;

    public LightFlightboard() {

    }
    public ArrayList<LightPlayer> getInGamePlayers() {
        return inGamePlayers;

    }

    public LightPlayer getIngamePlayer(String playerName) {
        for (LightPlayer player : ingamePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
    }
}
