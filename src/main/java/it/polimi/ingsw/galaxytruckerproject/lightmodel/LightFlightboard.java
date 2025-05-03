package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class LightFlightboard implements Serializable {
    private ArrayList<LightPlayer> inGamePlayers;

    public LightFlightboard(FlightBoard flightBoard) {
        this.inGamePlayers = new ArrayList<>();
        for(Player player: flightBoard.getAllPlayers()){
            LightPlayer inGamePlayer = new LightPlayer(player);
            inGamePlayers.add(inGamePlayer);
        }
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
