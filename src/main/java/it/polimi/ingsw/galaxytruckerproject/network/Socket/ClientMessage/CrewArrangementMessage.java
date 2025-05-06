package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

public class CrewArrangementMessage extends ClientMessage {

    private ArrayList<Tile> updatedCabins;

    public CrewArrangementMessage(ArrayList<Tile> updatedCabins) {
        this.updatedCabins = updatedCabins;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try{
            clientHandler.getController().pickCrewMembers(updatedCabins);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
