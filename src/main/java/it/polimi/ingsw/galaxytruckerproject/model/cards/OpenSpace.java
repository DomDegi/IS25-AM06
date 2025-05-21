package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class OpenSpace extends Card {
    private Player currentPlayer = null;
    private ViewInterface currentPlayerView = null;
    private int playerIndex = -1;
    ArrayList<Player> playersToEarlyLand = new ArrayList<>();
    

    //the subclass OpenSpace needs the same parameters as the superclass
    @JsonCreator
    public OpenSpace(@JsonProperty("level") int level,  @JsonProperty("imagePath") String filePath) {
        super(level, 0,filePath);
    }


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
    public void engineChoice(String playerName, int numDoubleEngines, ArrayList<Coordinates> batteriesToUse) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            engineChoice(currentPlayer.getPlayerName(), 0, new ArrayList<>());
            return;
        }
        Map<Integer,ArrayList<Tile>> returned = currentPlayer.useEngines(numDoubleEngines, batteriesToUse);
        if (returned == null) {
            try {
                currentPlayerView.showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        int engineStrength =  returned.keySet().iterator().next();
        ArrayList<Tile> tiles = returned.get(engineStrength);
        if (!tiles.isEmpty()) {
            notifyModifiedTiles(playerName, tiles);
        }
        this.moveOrEarlyLand(engineStrength);
    }

    //moves player forward; early lands if engineStrength == 0
    private void moveOrEarlyLand(int engineStrength) {
        if (engineStrength == 0) {
            playersToEarlyLand.add(currentPlayer);
            try {
                currentPlayerView.notifyEarlyLanding();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            game.getFlightBoard().moveForward(currentPlayer, engineStrength);
            notifyMovement(currentPlayer);
        }
        nextPlayer();
    }

    public void nextPlayer() {
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            for (Player player: playersToEarlyLand) {
                game.getFlightBoard().earlyLanding(player);
            }
            game.endCardEvent();
            return;
        }
        this.currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentPlayerView = viewsMap.get(currentPlayer.getPlayerName());
        if (currentPlayer.IsDisconnected() || currentPlayer.getShipBoard().getDoubleEngine().isEmpty() || currentPlayer.getShipBoard().getNumBatteries() == 0) {
            engineChoice(currentPlayer.getPlayerName(), 0, new ArrayList<>());
        }
        else {
            try {
                currentPlayerView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
            } catch (Exception ignored) {
            }
        }
    }

    public String toString() {
        return "OpenSpace" + " id: " + id;
    }
}