package it.polimi.ingsw.galaxytruckerproject.network.message;

public class SetColorRequest extends Message{
    private final String color;

    public SetColorRequest(String playerName, String color) {
        super(playerName, MessageType.SET_COLOR_REQUEST);
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
