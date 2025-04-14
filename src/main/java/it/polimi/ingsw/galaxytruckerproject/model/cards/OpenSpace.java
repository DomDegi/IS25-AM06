package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.UseEngineResponse;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class OpenSpace extends Card {
    private Player currentPlayer = null;
    private ViewInterface currentPlayerView = null;
    private int playerIndex = 0;
    ArrayList<Player> playersToEarlyLand = new ArrayList<>();
    

    //the subclass OpenSpace needs the same parameters as the superclass
    @JsonCreator
    public OpenSpace(@JsonProperty("level") int level) {
        super(level, 0);
    }

    //Initialize card for the player at the index playerIndex of the flightBoard ranking
    @Override
    public void initializeCard(GameInterface game, Map<String, ViewInterface> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    //makes so that the player gain as many days as their engineStrength
    @Override
    public void executeCard(Message message) {
        String playerName = message.getNickname();
        if (!Objects.equals(playerName, currentPlayer.getPlayerName()) || currentPlayer == null) {
            return;
        }
        if (message.getMessageType().equals(MessageType.USE_ENGINE_RESPONSE)) {
            UseEngineResponse messageReceived = (UseEngineResponse) message;
            int engineStrength = currentPlayer.useEngines(messageReceived.getNumEngine(), messageReceived.getCoordinates());
            if (engineStrength == -1) {
                return;
            }
            else {
                this.moveOrEarlyLand(engineStrength);
                this.nextPlayer();
            }
        }
    }

    //moves player forward; early lands if engineStrength == 0
    private void moveOrEarlyLand(int engineStrength) {
        System.out.println("\n");
        if (engineStrength == 0) {
            playersToEarlyLand.add(currentPlayer);
            nextPlayer();
            return;
        }
        game.getFlightBoard().moveForward(currentPlayer, engineStrength);
        nextPlayer();
    }

    public void nextPlayer() {
        if (currentPlayer != null) {
            playerIndex++;
        }
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            for (Player player: playersToEarlyLand) {
                game.getFlightBoard().earlyLanding(player);
            }
            game.endCardEvent();
        }
        this.currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentPlayerView = viewsMap.get(currentPlayer.getPlayerName());
        if (currentPlayer.IsDisconnected()) {
            executeCard(new UseEngineResponse(currentPlayer.getPlayerName(), 0, new ArrayList<>()));
            return;
        }
        currentPlayerView.asksToUseEngines();
    }

    public String toString() {
        return "OpenSpace";
    }
}