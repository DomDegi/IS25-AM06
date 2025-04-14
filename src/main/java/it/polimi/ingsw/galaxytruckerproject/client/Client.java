package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.*;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class Client {

    public LightPlayer getMe() {
        return me;
    }

    public String getName() {
        return me.getPlayerName();
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

    public HashMap<Integer, Tile> getDrawnTiles() {
        return drawnTiles;
    }
}