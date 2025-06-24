package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;

/**
 * The {@code Server} interface defines the methods required for server-side operations
 * in a multiplayer game. It provides a method for handling the connection process
 * between the server and the game controller, allowing the server to interact with the
 * game logic and manage multiple games.
 */
public interface Server {

    /**
     * Connects the server to the given {@link MultiGameController}, which manages multiple games
     * on the server. This method is responsible for establishing the connection between
     * the server and the game controller, allowing communication and management of games.
     *
     * @param multiGameController The controller that manages multiple games on the server.
     */
    void connect(String ip,MultiGameController multiGameController);
}
