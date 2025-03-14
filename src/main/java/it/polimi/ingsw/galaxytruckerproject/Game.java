package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.cards.CardDeck;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Tile;

import java.util.ArrayList;

public class Game {
    private GameState gameState;
    private final ArrayList<Player> playerList;
    private ArrayList<Card> cardList;
    private ArrayList<Tile> tileList;
    private ArrayList<Tile> drawnTiles;
    private final FlightBoard flightBoard;
    public Game() {
        this.gameState = GameState.LISTING_PLAYERS;
        this.flightBoard=new FlightBoard();
        this.playerList= new ArrayList<>();
        this.cardList = new ArrayList<>();
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public void ListPlayer(Player player) {
        playerList.add(player);
    }
    public void CreateDeck(String deckFile) {
        CardDeck totalDeck=new CardDeck(deckFile);
        cardList=totalDeck.getTier2FlightCards();
    }
    public void CreateTile(String tilesFile) {
        //waiting for factory
    }
    public void DrawTiles(Tile tile) {
        drawnTiles.add(tile);
    }


}
