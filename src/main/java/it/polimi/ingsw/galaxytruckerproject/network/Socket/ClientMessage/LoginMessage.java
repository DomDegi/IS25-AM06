package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request logging into the game.
 * This message includes the client's nickname for authentication.
 */
public class LoginMessage extends ClientMessage {

    /**
     * The nickname chosen by the client for logging into the game.
     */
    private String nickname;

    /**
     * Constructs a new LoginMessage with the specified nickname.
     *
     * @param nickname The nickname chosen by the client for logging in.
     */
    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Processes the login message by invoking the appropriate method on the client
     * handler's controller to handle the login process with the given nickname.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().login(nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
