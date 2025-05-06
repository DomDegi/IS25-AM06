package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class GoodsArrangementMessage extends ClientMessage {

    private int clientGoodsValue;

    private ArrayList<CargoHold> updatedCargos;

    public GoodsArrangementMessage(int clientGoodsValue, ArrayList<CargoHold> updatedCargos) {
        this.clientGoodsValue = clientGoodsValue;
        this.updatedCargos = updatedCargos;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().manageGoods(clientGoodsValue, updatedCargos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
