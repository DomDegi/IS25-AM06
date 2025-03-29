package it.polimi.ingsw.galaxytruckerproject.model.tiles;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {
    Connectors connectorsType;


    @JsonCreator
    public Link(@JsonProperty("connectorsType") Connectors connectorsType){
        this.connectorsType = connectorsType;
    }

    public Connectors getConnectorsType(){
        return connectorsType;
    }

    public void setConnectorsType(Connectors connectorsType) {
        this.connectorsType = connectorsType;
    }

    @Override
    public String toString() {
        return connectorsType.toString();
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
