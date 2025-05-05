package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class ChooseColorMessage extends ClientMessage {

    private PlayersColor color;

    public ChooseColorMessage(PlayersColor color) {
        this.color = color;
    }

    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().chooseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
