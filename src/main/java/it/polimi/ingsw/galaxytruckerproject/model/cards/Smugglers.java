package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.GoodsPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.*;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class Smugglers extends Enemies{
    private final GoodsPenalty lostGoods;
    private final ArrayList<Goods> rewardGoods;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private ViewInterface playersView;
    private boolean initialized;
    private int won;
    private GoodsChecker goodsChecker;

    @JsonCreator
    public Smugglers(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("cannonStrength") int cannonStrength, @JsonProperty("lostGoods") int lostGoods, @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods) {
        super(level, requiredDays, cannonStrength);
        this.lostGoods =new GoodsPenalty(lostGoods);
        this.rewardGoods = rewardGoods;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = 0;
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, ViewInterface> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    @Override
    public void executeCard(Message message) {
        String playerName = message.getNickname();
        if (!playerName.equals(currentPlayer.getPlayerName()) || currentPlayer == null)
            return;

        if (won == 0) {
            if (message.getMessageType().equals(MessageType.USE_CANNON_RESPONSE)) {
                UseCannonResponse messageReceived = (UseCannonResponse) message;
                float totalStrength = currentPlayer.useCannons(messageReceived.getStrength(), messageReceived.getCoordinates());
                if (totalStrength != -1) {
                    if (totalStrength > cannonStrength) {
                        won = 1;
                        broadcastMessage(playerName + "has beaten the smugglers!");
                        sendMessageToPlayer(playersView, "Input yes or no to decide if you want the reward");
                        playersView.asksToMakeAChoice();
                    }
                    else if (totalStrength == cannonStrength) {
                        nextPlayer();
                    }
                    else {
                        won = -1;
                        sendMessageToPlayer(playersView, "You've lost the smugglers, decide which goods to lose");
                        if (!lostGoods.initializePenalty(playersView, currentPlayer)) {
                            nextPlayer();
                        }
                    }
                }
            }
        }
        else if (won == 1){
            if (message.getMessageType().equals(MessageType.REFUSE_MESSAGE)) {
                game.endCardEvent();
            }
            else if (message.getMessageType().equals(MessageType.ACCEPT_MESSAGE)) {
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                playersView.asksToManageGoods(rewardGoods);
                won = 2;
            }
        }
        else if (won == 2) {
            if (message.getMessageType().equals(MessageType.MANAGE_GOODS_RESPONSE)) {
                ManageGoodsResponse manageGoodsResponse = (ManageGoodsResponse) message;
                if (goodsChecker.check(manageGoodsResponse.getClientGoodsValue(), manageGoodsResponse.getCargoHoldsToUpdate()))
                    game.endCardEvent();
            }
        }
        else if (won == -1){
            if (lostGoods.applyPenalty(game, currentPlayer, playersView, message) == 1) {
                nextPlayer();
            }
            else {
                lostGoods.initializePenalty(playersView, currentPlayer);
            }
        }
    }

    public void nextPlayer() {
        if (!initialized) {
            this.initialized = true;
            this.playerToInteract = game.getListOfInFlightPlayers();
        } else {
            if (playerToInteract.isEmpty()) {
                game.endCardEvent();
                return;
            }
            playerToInteract.removeFirst();
        }
        currentPlayer = playerToInteract.getFirst();
        String playerName = currentPlayer.getPlayerName();
        this.playersView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
        sendMessageToPlayer(playersView, "WATCH OUT, SMUGGLERS!! \nIf you don't have at least a Cannon Strength of "
                +cannonStrength+" you will loose "+lostGoods+ "\nDESTROY THEM and you will loose"+requiredDays+
                "to fill your cargo with the following goods:\n");
        goodsChecker =new GoodsChecker(currentPlayer,rewardGoods);
        sendMessageToPlayer(playersView, goodsChecker.goodsPrinter(rewardGoods));

        if (currentPlayer.IsDisconnected()) {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                executeCard(new RefuseMessage(playerName));
            }
            else if (singleCannonPower == cannonStrength) {
                nextPlayer();
            }
            else {
                won = -1;
                lostGoods.automaticPenalty(game, currentPlayer, playersView);
                nextPlayer();
            }
        }
        else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                sendMessageToPlayer(playersView, "You've beaten the smugglers, input yes or no" +
                        " to claim the rewards or not");
                playersView.asksToMakeAChoice();
            }
            else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    sendMessageToPlayer(playersView,"You've lost to the smugglers:\n");
                    sendMessageToPlayer(playersView,"input a pair of number x y for every good to remove\n");
                    lostGoods.initializePenalty(playersView, currentPlayer);
                }
            }
            else {
                sendMessageToPlayer(playersView, "You can't beat the slavers with only your single cannons, " +
                        "use your double cannons in exchange for batteries?");
                playersView.asksToUseCannons();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Smugglers: ").append(super.toString()).append(" lostGoods: ").append(this.lostGoods).append(" ");
        string.append("rewardGoods: ");
        for (Goods goods : rewardGoods) {
            string.append(goods.toString()).append(" ");
        }
        return string.toString();
    }
}