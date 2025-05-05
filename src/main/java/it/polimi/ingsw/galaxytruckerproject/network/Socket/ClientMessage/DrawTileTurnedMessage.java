package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class DrawTileTurnedMessage extends ClientMessage {

    private int index;

    public DrawTileTurnedMessage(int index){
        this.index = index;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawTileFromTurned(index);
        } catch (Exception e) {
            ;
        }
    }


}
