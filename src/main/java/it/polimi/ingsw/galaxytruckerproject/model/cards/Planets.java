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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Planets extends Card{
    private final ArrayList<Planet> listOfPlanets;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private ViewInterface currentPlayerView = null;
    private boolean initialized;
    private boolean chosen;
    private GoodsChecker goodsChecker;
    public Map<String,Planet> playerChosenPlanets = null;

    @JsonCreator
    public Planets(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("listOfPlanets") ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays);
        this.listOfPlanets = listOfPlanets;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
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
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (goodsChecker.check(clientCredits, updatedCargos)) {
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            notifyMovement(currentPlayer);
            ArrayList<Tile> updatedTiles = new ArrayList<>(updatedCargos);
            notifyModifiedTiles(playerName, updatedTiles);
            nextPlayer();
        }
        else {
            currentPlayerView.showWrongInputMessage();
        }
    }


    //planet choice is from 1 to total planets, but the array indexes start from 0
    @Override
    public void planetChoice(String playerName, int planet) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (listOfPlanets.get(planet - 1).getOccupationStatus() || chosen) {
            currentPlayerView.showWrongInputMessage();
            return;
        }
        listOfPlanets.get(planet - 1).setOccupationStatus();
        playerChosenPlanets.put(playerName, listOfPlanets.get(planet - 1));
        notifyPlayerLanded(playerName, planet);
        this.goodsChecker = new GoodsChecker(currentPlayer, listOfPlanets.get(planet-1).getListOfGoods());
        chosen = true;
    }

    public void notifyPlayerLanded(String playerName, int planet) {
        for (VirtualView view:  viewsMap.values()) {
            try {
                view.notifyPlayerLandedOnPlanet(playerName, planet);
            } catch (Exception ignored) {}
        }
    }

    public void nextPlayer() {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfInFlightPlayers());
            initialized=true;
        }
        else {
            if (playerToInteract.isEmpty()) {
                game.endCardEvent();
                return;
            }
            playerToInteract.removeFirst();
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
        currentPlayer= playerToInteract.getFirst();
        currentPlayerView=viewsMap.get(currentPlayer.getPlayerName());
        this.goodsChecker = null;

        if (currentPlayer.IsDisconnected()) {
            nextPlayer();
            return;
        }
        currentPlayerView.setClientState(ClientState.PLANET_CHOICE);
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