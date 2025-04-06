package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class RefuseMessage extends Message{

    public RefuseMessage (String playerName){
        super(playerName, MessageType.REFUSE_MESSAGE);
    }
}
