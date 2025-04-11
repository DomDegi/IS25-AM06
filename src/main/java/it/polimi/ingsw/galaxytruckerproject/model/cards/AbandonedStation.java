package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.client.GoodsManager;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AbandonedStation extends Card {
    private final int crewNumberRequired;
    private final ArrayList<Goods> possibleGoodsGain;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private ViewInterface currentPlayersView = null;
    private boolean initialized;
    private boolean won;
    private GoodsManager goodsManager;
    private GameInterface game = null;
    private Map<String, ViewInterface> viewsMap =  new HashMap<String, ViewInterface>();

    @JsonCreator
    public AbandonedStation(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("crewNumberRequired") int crewNumberRequired, @JsonProperty("possibleGoodsGain") ArrayList<Goods> possibleGoodsGain) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = false;
    }
    //asks every player in order of ranking that meets the requirements if they want to spend days to gain the goods
    @Override
    public void initializeCard(Game game, Map<String, ViewInterface> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    @Override
    public void executeCard(Message message) {
        String playerName = message.getNickname();

        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;

        if (!won) {
            int choice = -1;
            if (message.getMessageType().equals(MessageType.ACCEPT_MESSAGE)) {
                choice = 1;
            }
            else if (message.getMessageType().equals(MessageType.REFUSE_MESSAGE)) {
                choice = 0;
            }
            if (currentPlayer.getTotalCrew() >= crewNumberRequired) {
                if (choice == 1) {
                    game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                    goodsManager=new GoodsManager(currentPlayer,possibleGoodsGain);
                    won=true;
                    goodsManager.goodsPrinter(possibleGoodsGain);
                    currentPlayersView.showGenericMessage("Choose for each good where to put it, " +
                            "input 'done' to stop,'pick x y' to pick one good from your cargo");
                    currentPlayersView.asksToInputCoordinates();
                    //exit for loop: the station has been claimed
                } else if (choice == 0) {
                    currentPlayersView.showGenericMessage("No action performed");
                    nextPlayer();
                } else {
                    currentPlayersView.showGenericMessage("Invalid choice");
                    currentPlayersView.asksToMakeAChoice();
                }
            } else {
                System.out.printf("\nSorry" + currentPlayer + "you can't land on the station, you need at least %d crew members\n", crewNumberRequired);
            }
        }else{
            if(goodsManager.getReward(message)){
                game.endCardEvent();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AbandonedStation: ").append(super.toString()).append(" ").
                append("crewNumberRequired: ").append(crewNumberRequired).
                append(" possibleGoodsGain: ");
        for (Goods good: possibleGoodsGain)
            sb.append(good.toString()).append(" ");
        return sb.toString();
    }

    /**
     * If the array has to be initialized, sets the non-landed player as an array
     * removes first player in the arraylist and sets the new first as current player.
     * if the arraylist is empty ends the card event.
     */
    public void nextPlayer() {
        if (!initialized) {
            playerToInteract = game.getListOfInFlightPlayers();
            initialized = true;
        } else {
            if (playerToInteract.isEmpty()) {
                game.endCardEvent();
                return;
            }
            playerToInteract.removeFirst();
        }
        currentPlayer = playerToInteract.getFirst();
        if (currentPlayer.IsDisconnected()) {
            nextPlayer();
        }
        this.currentPlayersView = viewsMap.get(currentPlayer.getPlayerName());
        currentPlayersView.showGenericMessage("Abandoned station: you will loose " +
                requiredDays +
                " flight days to gain the following goods:");
        this.goodsManager=new GoodsManager(currentPlayer,possibleGoodsGain);
        currentPlayersView.showGenericMessage(goodsManager.goodsPrinter(possibleGoodsGain));

        if (currentPlayer.getTotalCrew() < crewNumberRequired) {
            currentPlayersView.showGenericMessage("Sorry " + currentPlayer.getPlayerName() +
                    " you can't land on the station, you need at least " + crewNumberRequired +
                    " crew members and you have " + currentPlayer.getTotalCrew());
            nextPlayer();
        }
        else {
            currentPlayersView.showGenericMessage(currentPlayer.getPlayerName()+" congratulation, you have "+
                    currentPlayer.getTotalCrew() +
                    " which is more than " + crewNumberRequired + " input 'yes' to land on the station, " +
                    "input 'no' to ignore, you will loose " + requiredDays + " flight days\n");
            currentPlayersView.asksToMakeAChoice();
        }
    }
}