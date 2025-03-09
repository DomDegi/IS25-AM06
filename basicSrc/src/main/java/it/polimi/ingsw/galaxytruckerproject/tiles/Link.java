package it.polimi.ingsw.galaxytruckerproject.tiles;

public class Link {
    Connectors connectorsType;
    public Link(Connectors connectorsType){
        this.connectorsType = connectorsType;
    }
    public Connectors getConnectorsType(){
        return connectorsType;
    }

}
