package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class ManageGoodsRequest extends Message{
    public ManageGoodsRequest() {
        super(Server.SERVER_NAME, MessageType.MANAGE_GOODS_REQUEST);
    }
}
