package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.*;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Client {
    private ClientState state;
    private Map<LightShipboard,String> players;
    private LightPlayer me;
    private LightShipboard myShipboard;
    private ArrayList<ArrayList<Card>> deck;
    private final LightFlightboard
    private HashMap<Integer, Tile> drawnTiles;
    private Tile tileInHand;
    private Timer timer;
    public ClientState getState() {
        return state;
    }
}
