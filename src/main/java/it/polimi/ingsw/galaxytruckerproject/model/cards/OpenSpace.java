package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

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
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {}
    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {}
    @Override
    public void choice(String playerName, boolean decision) {}
    @Override
    public void planetChoice(String playerName, int planet) {}

    @Override
    public void engineChoice(String playerName, int numDoubleEngines, ArrayList<Coordinates> batteriesToUse) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        int engineStrength =  currentPlayer.useEngines(numDoubleEngines, batteriesToUse);
        if (engineStrength == -1) {
            currentPlayerView.showWrongInputMessage();
            return;
        }
        this.moveOrEarlyLand(engineStrength);
        this.nextPlayer();
    }

    //moves player forward; early lands if engineStrength == 0
    private void moveOrEarlyLand(int engineStrength) {
        if (engineStrength == 0) {
            playersToEarlyLand.add(currentPlayer);
            nextPlayer();
            return;
        }
        game.getFlightBoard().moveForward(currentPlayer, engineStrength);
        notifyMovement(currentPlayer);
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
            engineChoice(currentPlayer.getPlayerName(), 0, new ArrayList<>());
            return;
        }
        currentPlayerView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
    }

    public String toString() {
        return "OpenSpace";
    }
}