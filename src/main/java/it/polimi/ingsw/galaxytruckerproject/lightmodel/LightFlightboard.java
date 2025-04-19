package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import java.util.ArrayList;

public class LightFlightboard {

    private ArrayList<LightPlayer> ingamePlayers;

    public LightFlightboard() {

    }
    public ArrayList<LightPlayer> getIngamePlayers() {
        return ingamePlayers;

    }

    public LightPlayer getIngamePlayer(String playerName) {
        for (LightPlayer player : ingamePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
    }
}
