package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class RefuseMessage extends Message{

    public RefuseMessage (String playerName){
        super(playerName, MessageType.REFUSE_MESSAGE);
    }
}
