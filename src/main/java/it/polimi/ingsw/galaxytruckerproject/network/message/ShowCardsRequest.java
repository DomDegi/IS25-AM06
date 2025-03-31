package it.polimi.ingsw.galaxytruckerproject.network.message;

public class ShowCardsRequest extends Message{

    private final int cardsToLookAt;

    public ShowCardsRequest(String playerName, int cardsToLookAt) {
        super(playerName, MessageType.SHOW_CARDS_REQUEST);
        this.cardsToLookAt = cardsToLookAt;
    }

    public int getCardsToLookAt () {
        return cardsToLookAt;
    }
}
