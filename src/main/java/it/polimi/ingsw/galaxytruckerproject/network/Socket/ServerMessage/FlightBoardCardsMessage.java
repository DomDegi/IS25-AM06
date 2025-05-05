package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.util.ArrayList;
import java.util.Map;

public class FlightBoardCardsMessage extends ServerMessage {

    Map<Integer,ArrayList<Card>> flightBoardCards;

    public FlightBoardCardsMessage(Map<Integer,ArrayList<Card>> flightBoardCards) {
        this.flightBoardCards = flightBoardCards;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try{
            serverHandler.getClientController().setDeck(flightBoardCards);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
