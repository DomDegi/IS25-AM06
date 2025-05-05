package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class TurnHourglassMessage extends ClientMessage{

    public TurnHourglassMessage() {
        ;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().turnHourglass();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
