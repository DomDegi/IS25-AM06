package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class UpdateDrawnCard extends Message{

    private final Card drawnCard;

    public UpdateDrawnCard(Card drawnCard) {
        super(Server.SERVER_NAME, MessageType.UPDATE_DRAWN_CARD);
        this.drawnCard = drawnCard;
    }

    public Card getDrawnCard() {
        return drawnCard;
    }
}
