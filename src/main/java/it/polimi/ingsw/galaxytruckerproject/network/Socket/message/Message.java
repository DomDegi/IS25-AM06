package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;


public abstract class Message {
    protected String nickname;
    protected MessageType messageType;

    public Message(String nickname, MessageType messageType) {
        this.nickname = nickname;
        this.messageType = messageType;
    }

    public String getNickname() {
        return nickname;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Message{ ").append("nickname=").append(nickname).append(", messageType=").append(messageType).append("}");
        return builder.toString();
    }

}
