package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;

import java.util.ArrayList;

public class ManageGoodsResponse extends Message{
    private final int clientGoodsValue;
    private final ArrayList<CargoHold> cargoHoldsToUpdate;

    public ManageGoodsResponse(String playerName, int clientGoodsValue, ArrayList<CargoHold> cargoHoldsToUpdate) {
        super(playerName, MessageType.MANAGE_GOODS_RESPONSE);
        this.clientGoodsValue = clientGoodsValue;
        this.cargoHoldsToUpdate = cargoHoldsToUpdate;
    }

    public int getClientGoodsValue() {
        return clientGoodsValue;
    }
}
