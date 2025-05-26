package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a message sent by the client to arrange goods in the cargo hold.
 * This message includes the client's total goods value and the updated cargo hold information.
 */
public class GoodsArrangementMessage extends ClientMessage {

    /**
     * The total value of the goods for the client.
     */
    private int clientGoodsValue;

    /**
     * The list of updated cargo holds with the arranged goods.
     */
    private ArrayList<CargoHold> updatedCargos;

    /**
     * Constructs a new GoodsArrangementMessage with the specified goods value and updated cargo holds.
     *
     * @param clientGoodsValue The total value of the client's goods.
     * @param updatedCargos The list of updated cargo holds with arranged goods.
     */
    public GoodsArrangementMessage(int clientGoodsValue, ArrayList<CargoHold> updatedCargos) {
        this.clientGoodsValue = clientGoodsValue;
        this.updatedCargos = updatedCargos;
    }

    /**
     * Processes the goods arrangement message by invoking the appropriate method on the client
     * handler's controller to manage the goods arrangement in the cargo hold.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().manageGoods(clientGoodsValue, updatedCargos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
