package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client that a player has landed on a planet.
 * This message includes the player's name and the index of the planet they landed on.
 */
public class PlayerLandedOnPlanetMessage extends ServerMessage {

    /**
     * The name of the player who landed on the planet.
     */
    private String playerName;

    /**
     * The index of the planet the player landed on.
     */
    private int planet;

    /**
     * Constructs a new PlayerLandedOnPlanetMessage with the specified player name and planet index.
     *
     * @param playerName The name of the player who landed on the planet.
     * @param planet The index of the planet the player landed on.
     */
    public PlayerLandedOnPlanetMessage(String playerName, int planet) {
        this.playerName = playerName;
        this.planet = planet;
    }

    /**
     * Processes the player landing on planet message by updating the client's state if the current player has landed.
     * It also notifies the client about the player landing on the specified planet.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        // If the current player has landed on a planet, change their state to "MANAGE_GOODS"
        serverHandler.getClientController().setPlanets(planet);
        try {
            // Notify the client about the player landing on the planet
            serverHandler.getView().notifyPlayerLandedOnPlanet(playerName, planet);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
