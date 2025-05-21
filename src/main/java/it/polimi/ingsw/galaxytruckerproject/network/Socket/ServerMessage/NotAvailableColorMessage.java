package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.util.ArrayList;

public class NotAvailableColorMessage extends ServerMessage {

    private ArrayList<PlayersColor> color;

    public NotAvailableColorMessage(ArrayList<PlayersColor> color) {
        this.color = color;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getClientController().colorsNotAvailable(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
