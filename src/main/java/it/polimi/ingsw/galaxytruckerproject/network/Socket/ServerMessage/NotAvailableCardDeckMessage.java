package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.util.ArrayList;

public class NotAvailableCardDeckMessage extends ServerMessage {

    private ArrayList<Integer> lockedSmallDecks;

    public NotAvailableCardDeckMessage(ArrayList<Integer> lockedSmallDecks) {
        this.lockedSmallDecks = lockedSmallDecks;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getClientController().decksNotAvailable(lockedSmallDecks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
