package it.polimi.ingsw.galaxytruckerproject.network.message;

public class ShowPlayersShipboardRequest extends Message{
    private final String playerName;

    public ShowPlayersShipboardRequest(String requester,  String playerName) {
        super(requester, MessageType.SHOW_PLAYERS_SHIPBOARD_REQUEST);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
