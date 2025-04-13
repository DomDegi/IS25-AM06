package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class BookTileRequest extends Message {
    public BookTileRequest(String nickname) {
        super(nickname, MessageType.BOOK_TILE_REQUEST);
    }
}
