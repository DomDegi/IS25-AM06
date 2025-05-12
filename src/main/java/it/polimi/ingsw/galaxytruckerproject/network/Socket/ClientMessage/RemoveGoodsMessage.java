package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class RemoveGoodsMessage extends ClientMessage {
    private ArrayList<Coordinates> fromHere;
    public RemoveGoodsMessage(ArrayList<Coordinates> fromHere) throws RemoteException {
        this.fromHere = fromHere;
    }
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().removeGoods(fromHere);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
