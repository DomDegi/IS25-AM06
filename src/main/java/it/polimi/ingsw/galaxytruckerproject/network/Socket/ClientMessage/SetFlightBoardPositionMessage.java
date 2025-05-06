package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class SetFlightBoardPositionMessage extends ClientMessage {

    private int position;

    public SetFlightBoardPositionMessage(int position) {
        this.position = position;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().setPosition(position);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
