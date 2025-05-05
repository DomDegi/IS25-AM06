package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class FlightboardCardsResponse extends ServerMessage {

    public FlightboardCardsResponse() {}


    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showCard(serverHandler.getClientController().getDisplayedCard());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
