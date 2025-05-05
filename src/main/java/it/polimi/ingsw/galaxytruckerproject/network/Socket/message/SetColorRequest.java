package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;

public class SetColorRequest extends Message{
    private final PlayersColor color;

    public SetColorRequest(String playerName, PlayersColor color) {
        super(playerName, MessageType.SET_COLOR_REQUEST);
        this.color = color;
    }

    public PlayersColor getColor() {
        return color;
    }
}
