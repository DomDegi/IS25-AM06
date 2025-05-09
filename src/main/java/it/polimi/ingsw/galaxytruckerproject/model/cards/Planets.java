package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Planets extends Card{
    private final ArrayList<Planet> listOfPlanets;
    private Player currentPlayer;
    private ViewInterface currentPlayerView = null;
    private boolean chosen;
    private Map<String,GoodsChecker> goodsChecker;
    public Map<String,Planet> playerChosenPlanets = new HashMap<>();
    private int playerIndex = -1;

    @JsonCreator
    public Planets(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("listOfPlanets") ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays);
        this.goodsChecker=new HashMap<>();
        this.listOfPlanets = listOfPlanets;
        this.currentPlayer = null;
        this.chosen = false;
    }
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    @Override
    public void manageGoods (String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || !chosen) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        if (goodsChecker.get(playerName).check(clientCredits, updatedCargos)) {
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            playerIndex++;
            notifyMovement(currentPlayer);
            ArrayList<Tile> updatedTiles = new ArrayList<>(updatedCargos);
            notifyModifiedTiles(playerName, updatedTiles);
            if (playerIndex==goodsChecker.size()) {
                nextPlayer();
            }
        }
        else {
            try {
                currentPlayerView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
    }


    //planet choice is from 1 to total planets, but the array indexes start from 0
    //input 0 to not choose anything
    @Override
    public void planetChoice(String playerName, int planet) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || chosen) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        if (planet <= 0 ) {
            nextPlayer();
            return;
        }
        if (listOfPlanets.get(planet - 1).getOccupationStatus()) {
            try {
                currentPlayerView.showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        listOfPlanets.get(planet - 1).setOccupationStatus();
        playerChosenPlanets.put(playerName, listOfPlanets.get(planet - 1));
        notifyPlayerLanded(playerName, planet);
        goodsChecker.put(currentPlayer.getPlayerName(), new GoodsChecker(currentPlayer,listOfPlanets.get(planet - 1).getListOfGoods()));
        if(playerIndex==game.getListOfInFlightPlayers().size()-1) {
            chosen = true;
            for (Player player: game.getListOfInFlightPlayers()) {
                if (goodsChecker.containsKey(player.getPlayerName())) {
                    try {
                        currentPlayerView.setClientState(ClientState.MANAGE_GOODS);
                    } catch (Exception ignored) {
                    }
                }else {
                    try {
                        currentPlayerView.setClientState(ClientState.WAIT);
                    } catch (Exception ignored) {
                    }
                }
            }
            playerIndex=0;
            return;
        }
        nextPlayer();
    }

    public void notifyPlayerLanded(String playerName, int planet) {
        for (VirtualView view:  viewsMap.values()) {
            try {
                view.notifyPlayerLandedOnPlanet(playerName, planet);
            } catch (Exception ignored) {}
        }
    }

    public void nextPlayer() {
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        chosen = false;
        AtomicInteger i = new AtomicInteger();
        listOfPlanets.forEach(planet -> {
            if (planet.getOccupationStatus())
                i.getAndIncrement();
        });
        if (i.get() == listOfPlanets.size()) {
            game.endCardEvent();
            return;
        }
        currentPlayer= game.getListOfInFlightPlayers().get(playerIndex);
        currentPlayerView=viewsMap.get(currentPlayer.getPlayerName());
        this.goodsChecker = null;

        if (currentPlayer.IsDisconnected()) {
            nextPlayer();
            return;
        }
        try {
            currentPlayerView.setClientState(ClientState.PLANET_CHOICE);
        }catch(Exception ignored) {}
    }

    @Override
    public ArrayList<Goods> getGoodsList(String playerName) {
        return playerChosenPlanets.get(playerName).getListOfGoods();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Planets: "). append(super.toString()).append(" ");
        int i = 0;
        for (Planet planet : listOfPlanets) {
            i++;
            string.append("planet ").append(i).append(" - ").append(planet.toString());
        }
        return string.toString();
    }

    public String printListOfPlanets() {
        int index = 1;
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED= "\u001B[31m";

        StringBuilder sb = new StringBuilder();
        for (Planet planet : listOfPlanets) {
            if (!planet.getOccupationStatus()) {
                sb.append("Planet: ").append(index).append(" ");
                for (Goods goods : planet.getListOfGoods()) {
                    if(goods.getColor() == GoodsColor.BLUE){
                        sb.append(ANSI_BLUE).append(goods.getColor())
                                .append("(").append(goods.getValue())
                                .append(") ").append(ANSI_RESET);
                    }else if(goods.getColor()==GoodsColor.GREEN){
                        sb.append(ANSI_GREEN).append(goods.getColor())
                                .append("(").append(goods.getValue())
                                .append(") ").append(ANSI_RESET);
                    }else if(goods.getColor()==GoodsColor.YELLOW){
                        sb.append(ANSI_YELLOW).append(goods.getColor())
                                .append("(").append(goods.getValue())
                                .append(") ").append(ANSI_RESET);
                    }else if(goods.getColor()==GoodsColor.RED){
                        sb.append(ANSI_RED).append(goods.getColor())
                                .append("(").append(goods.getValue())
                                .append(") ").append(ANSI_RESET);
                    }
                }
                sb.append("\n");
            }
            index++;
        }
        return sb.toString();
    }

    public ArrayList<Planet> getListOfPlanets() {
        return listOfPlanets;
    }
}