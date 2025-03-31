package it.polimi.ingsw.galaxytruckerproject.network.message;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

public class LoginRequestMessage extends Message {

    public LoginRequestMessage (String nickname, MessageType messageType) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

}
