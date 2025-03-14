package it.polimi.ingsw.galaxytruckerproject.tiles;

public class Link {
    Connectors connectorsType;
    public Link(Connectors connectorsType){
        this.connectorsType = connectorsType;
    }
    public Connectors getConnectorsType(){
        return connectorsType;
    }

    public boolean isConnected(Link other){
        if(this.connectorsType == other.connectorsType){
            return true;
        }
        return this.connectorsType == Connectors.UNIVERSAL && (other.connectorsType == Connectors.DOUBLE || other.connectorsType == Connectors.SINGLE);
    }
}
