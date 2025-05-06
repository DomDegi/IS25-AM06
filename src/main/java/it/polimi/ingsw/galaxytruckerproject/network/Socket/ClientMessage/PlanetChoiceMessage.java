package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class PlanetChoiceMessage extends ClientMessage {

    private int choice;

    public PlanetChoiceMessage(int choice) {
        this.choice = choice;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().choosePlanet(choice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
