package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class LightFlightboard implements Serializable {
    private final ArrayList<LightPlayer> inGamePlayers;

    private final ArrayList<LightPlayer> inLobbyPlayers;

    public LightFlightboard(FlightBoard flightBoard) {
        this.inGamePlayers = new ArrayList<>();
        for(Player player: flightBoard.getAllPlayers()){
            LightPlayer inGamePlayer = new LightPlayer(player);
            inGamePlayers.add(inGamePlayer);
        }
        this.inLobbyPlayers = new ArrayList<>();
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

    public void addInGamePlayer(LightPlayer player) {
        if(inGamePlayers.size() <=4){
            inGamePlayers.add(player);
        }
    }

    public ArrayList<String>  getInGamePlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for(LightPlayer player : inLobbyPlayers){
            playerNames.add(player.getPlayerName());
        }
        return playerNames;
    }

    public void addInLobbyPlayer(ArrayList<LightPlayer> players) {
        for (LightPlayer player: players) {
            if (!this.getInGamePlayers().contains(player.getPlayerName())) {
                this.inLobbyPlayers.add(player);
                LightShipBoard lightShipBoard = new LightShipBoard(player);
                lightShipBoard.initializeLevel2();
            }
        }
    }

    public ArrayList<LightPlayer> getInLobbyPlayers() {
        return inLobbyPlayers;
    }

    public LightPlayer getInLobbyPlayer(String playerName) {
        for (LightPlayer player : inLobbyPlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }
}
