package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class ManageGoodsRequest extends Message{
    public ManageGoodsRequest() {
        super(Server.SERVER_NAME, MessageType.MANAGE_GOODS_REQUEST);
    }
}
