package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class GenericMessage extends Message {
    private final String genericMessage;

    public GenericMessage (String genericMessage) {
        super(Server.SERVER_NAME, MessageType.GENERIC_MESSAGE);
        this.genericMessage = genericMessage;
    }

    public String getGenericMessage () {
        return genericMessage;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
                string.append("GenericMessage{")
                .append("genericMessage='")
                .append(genericMessage).append('\'')
                .append(", nickname='")
                .append(nickname).append('\'')
                .append(", messageType=")
                .append(messageType).append('}').toString();
        return string.toString();
    }
}
