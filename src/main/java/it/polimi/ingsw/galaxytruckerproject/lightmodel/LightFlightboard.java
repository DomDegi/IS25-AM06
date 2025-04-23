package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.util.ArrayList;

public class LightFlightboard {
    private FlightBoard flightBoard;
    private ArrayList<LightPlayer> inGamePlayers;

    public LightFlightboard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
        this.inGamePlayers = new ArrayList<>();
        for(Player player: flightBoard.getInGamePlayers()){
            LightPlayer inGamePlayer = new LightPlayer(player);
            inGamePlayers.add(inGamePlayer);
        }
    }

    public ArrayList<LightPlayer> getIngamePlayers() {
        return inGamePlayers;

    }

    public LightPlayer getIngamePlayer(String playerName) {
        for (LightPlayer player : inGamePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
    //per ora metto return null
    return null;
    }
}
