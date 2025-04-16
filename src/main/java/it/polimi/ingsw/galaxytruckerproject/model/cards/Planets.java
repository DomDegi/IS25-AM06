package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.ManageGoodsResponse;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.PlanetChoiceResponse;
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
    private boolean won;
    private GoodsChecker goodsChecker;

    @JsonCreator
    public Planets(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("listOfPlanets") ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays);
        this.listOfPlanets = listOfPlanets;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = false;
    }
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        broadcastMessage(printListOfPlanets());
        broadcastMessage(" input from 1 to " + listOfPlanets.size() + " to pick which to land on, input 'no' to ignore\n");
        this.nextPlayer();
    }
    //for each player asks if they want to spend the required days to occupy the planet they choose
    @Override
    public void executeCard(Message message) {
        String playerName = message.getNickname();

        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        if (!won) {
            int choice;
            if (message.getMessageType().equals(MessageType.PLANET_CHOICE_RESPONSE)) {
                PlanetChoiceResponse messageReceived = (PlanetChoiceResponse) message;
                choice = messageReceived.getChoosenPlanet();
            }
            else {
                return;
            }
            if (choice > 0 && choice <= listOfPlanets.size() && !listOfPlanets.get(choice - 1).getOccupationStatus()) {
                listOfPlanets.get(choice - 1).setOccupationStatus();
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                goodsChecker =new GoodsChecker(currentPlayer,listOfPlanets.get(choice - 1).getListOfGoods());
                won=true;
                sendMessageToPlayer(currentPlayerView, goodsChecker.goodsPrinter(listOfPlanets.get(choice - 1).getListOfGoods()));
                sendMessageToPlayer(currentPlayerView, "Choose for each good where to put it, " +
                        "input 'done' to stop,'pick x y' to pick one good from your cargo");
            } else if (choice > 0 && choice <= listOfPlanets.size() && listOfPlanets.get(choice - 1).getOccupationStatus()) {
                sendMessageToPlayer(currentPlayerView, "Error, planet "+ choice +" is already taken");
                sendMessageToPlayer(currentPlayerView, printListOfPlanets());
                sendMessageToPlayer(currentPlayerView, currentPlayer.getPlayerName()+" " +
                        "input from 1 to "+ listOfPlanets.size() +" to pick which to land on, input 'no' to ignore\n");
            } else if (choice == 0) {
                sendMessageToPlayer(currentPlayerView, "No action performed");
                nextPlayer();
            } else {
                sendMessageToPlayer(currentPlayerView,"Invalid choice: " + choice);
            }
        }else{
            if (message.getMessageType().equals(MessageType.MANAGE_GOODS_REQUEST)) {
                ManageGoodsResponse messageReceived = (ManageGoodsResponse) message;
                if (goodsChecker.check(messageReceived.getClientGoodsValue(), messageReceived.getCargoHoldsToUpdate())) {
                    nextPlayer();
                }
            }
        }
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
        won = false;
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

        if (currentPlayer.IsDisconnected()) {
            nextPlayer();
            return;
        }
        currentPlayerView.asksPlanetChoice();
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
}