package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to choose a planet in the game.
 * This message includes the client's choice of the planet based on its index.
 */
public class PlanetChoiceMessage extends ClientMessage {

    /**
     * The index representing the planet chosen by the client.
     */
    private int choice;

    /**
     * Constructs a new PlanetChoiceMessage with the specified planet choice.
     *
     * @param choice The index of the planet chosen by the client.
     */
    public PlanetChoiceMessage(int choice) {
        this.choice = choice;
    }

    /**
     * Processes the planet choice message by invoking the appropriate method on the client
     * handler's controller to handle the planet selection.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().choosePlanet(choice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
