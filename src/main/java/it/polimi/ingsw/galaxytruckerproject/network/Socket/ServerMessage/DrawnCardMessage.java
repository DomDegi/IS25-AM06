package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class DrawnCardMessage extends ServerMessage {

    private Card card;

    public DrawnCardMessage(Card card) {
        this.card = card;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyDrawnCard(card);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        serverHandler.getClientController().setState(ClientState.WAIT);
        serverHandler.getClientController().setDisplayedCard(card);
    }
}
