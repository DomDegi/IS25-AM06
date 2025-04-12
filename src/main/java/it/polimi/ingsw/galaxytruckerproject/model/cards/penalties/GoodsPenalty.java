package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.SendCoordinatesResponse;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;

public class GoodsPenalty extends Penalty {
    private final int numberOfLostGoods;
    private int numberPlayerOfLostGoods;

    @JsonCreator
    public GoodsPenalty(@JsonProperty("numberOfLostGoods") int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
        this.numberPlayerOfLostGoods = numberOfLostGoods;
    }

    public int getNumberOfLostGoods() {
        return numberOfLostGoods;
    }

    @Override
    public int applyPenalty(GameInterface game, Player player, ViewInterface playersView, Message message){
        if (!message.getMessageType().equals(MessageType.SEND_COORDINATES_RESPONSE)) {
            return 0;
        }
        SendCoordinatesResponse messageReceived = (SendCoordinatesResponse) message;
        ArrayList<Coordinates> toRemove = messageReceived.getCoordinates();
        if (toRemove.size() < numberOfLostGoods) {
            return 0;
        }
        int availableGoods = player.getShipBoard().getAllGoods().size();
        if (availableGoods < numberOfLostGoods) {
            if (!player.removeGoods(messageReceived.getTheseMany(availableGoods))) {
                return 0;
            }
            for (int i = 0; i < availableGoods; i++) {
                toRemove.removeFirst();
            }
            while (toRemove.size() > player.getShipBoard().getNumBatteries()) {
                toRemove.removeLast();
            }
            if (!player.chooseBatteriesUse(toRemove)) {
                return 0;
            }
        }
        else {
            if(!player.removeGoods(toRemove)) {
                return 0;
            }
        }
        return 1;
    }

    public void automaticPenalty(GameInterface game, Player disconnectedPlayer, ViewInterface view) {
        int counter = numberOfLostGoods;
        ShipBoard playerShip = disconnectedPlayer.getShipBoard();

        ArrayList<Coordinates> redGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED));
        ArrayList<Coordinates> yellowGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW));
        ArrayList<Coordinates> greenGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN));
        ArrayList<Coordinates> blueGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE));

        while (counter > 0) {
            if (!redGoods.isEmpty()) {
                playerShip.removeGood(new Goods(GoodsColor.RED), redGoods.removeFirst());
            }
            else if (!yellowGoods.isEmpty()) {
                playerShip.removeGood(new Goods(GoodsColor.YELLOW), yellowGoods.removeFirst());
            }
            else if (!greenGoods.isEmpty()) {
                playerShip.removeGood(new Goods(GoodsColor.GREEN), greenGoods.removeFirst());
            }
            else if (!blueGoods.isEmpty()) {
                playerShip.removeGood(new Goods(GoodsColor.BLUE), blueGoods.removeFirst());
            }
            counter--;
        }
    }

    @Override
    public String toString() {
        return "GoodsPenalty " +  numberPlayerOfLostGoods;
    }

    public int getNumber(){
        return numberOfLostGoods;
    }

    public boolean initializePenalty(ViewInterface view, Player player) {
        if (player.getShipBoard().isCargoEmpty()) {
            if (player.getShipBoard().getNumBatteries() == 0) {
                view.showGenericMessage("no goods or batteries found");
                return false;
            }
            view.showGenericMessage("You don't have goods, so remove batteries until you've removed " +
                    numberOfLostGoods + " batteries or until you've run out");
            view.asksToUseBatteries();
        }
        view.showGenericMessage("You have to remove " + numberOfLostGoods);
        view.asksToRemoveGoods();
        return true;
    }
}
