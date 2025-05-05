package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class TileBookingMessage extends ClientMessage {

    public TileBookingMessage(){
        ;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().bookTile();
        } catch (Exception e) {
            ;
        }
    }
}
