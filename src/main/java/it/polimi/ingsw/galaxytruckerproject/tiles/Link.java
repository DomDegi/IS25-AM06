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
        if(this.connectorsType == other.connectorsType)
            return true;
        if(this.connectorsType == Connectors.UNIVERSAL && (other.connectorsType == Connectors.DOUBLE || other.connectorsType == Connectors.SINGLE))
            return true;
        if(other.connectorsType == Connectors.UNIVERSAL && (this.connectorsType == Connectors.DOUBLE || this.connectorsType == Connectors.SINGLE))
            return true;

        return false;
    }
    public void setConnectorType(Connectors connectorType) {
        this.connectorsType = connectorType;
    }

}
