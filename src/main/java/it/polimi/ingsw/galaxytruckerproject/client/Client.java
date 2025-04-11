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
    private LightPlayer me;
    private ArrayList<LightPlayer> playersList;
    private ArrayList<ArrayList<Card>> deck;
    private LightFlightboard flightBoard;
    private HashMap<Integer, Tile> drawnTiles;
    private Tile tileInHand;
    private Timer timer;

    public LightPlayer getMe() {
        return me;
    }

    public ArrayList<LightPlayer> getPlayersList() {
        return playersList;
    }

    public ArrayList<ArrayList<Card>> getDeck() {
        return deck;
    }

    public Tile getTileInHand() {
        return tileInHand;
    }

    public void setTileInHand(Tile tileInHand) {
        this.tileInHand = tileInHand;
    }
}
