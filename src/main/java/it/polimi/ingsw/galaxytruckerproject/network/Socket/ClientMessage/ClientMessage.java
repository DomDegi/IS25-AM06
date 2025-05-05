package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;

public abstract class ClientMessage extends Message{
    private int index;

    public abstract void processMessage(ClientHandler clientHandler);

    public void setIndex(int index){
        this.index = index;
    }
    public int getIndex(){
        return index;
    }
}
