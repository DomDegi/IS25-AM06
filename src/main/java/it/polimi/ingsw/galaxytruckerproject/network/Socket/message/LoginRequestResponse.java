package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class LoginRequestResponse extends Message{
    private final boolean loginSuccessful;
    private final boolean isConnected;

    public LoginRequestResponse(boolean loginSuccessful, boolean isConnected) {
        super(Server.SERVER_NAME, MessageType.LOGIN_RESPONSE);
        this.loginSuccessful = loginSuccessful;
        this.isConnected = isConnected;
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
