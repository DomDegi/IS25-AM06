package it.polimi.ingsw.galaxytruckerproject.player;

import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.tiles.Tile;

public class Player {
    private String playerName;
    private int playerPosition;
    private PlayersColor playerColor;
    private int credit;
    private ShipBoard playerShip;
    private Tile drawnTile;

    public Player(String playerName, PlayersColor playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerPosition = 0;
        this.credit = 0;
        this.playerShip = new ShipBoard(this);
        this.drawnTile = null;
    }

    //GETTER METHODS
    public String getPlayerName() {return playerName;}
    public int getPlayerPosition() {return playerPosition;}
    public PlayersColor getPlayerColor() {return playerColor;}
    public int getCredit() {return credit;}
    public ShipBoard getShipBoard() {return playerShip;}
    public Tile getDrawnTile() { return drawnTile; }

    //SETTER METHODS
    public Tile removeDrawnTile() {
        if (drawnTile != null) {
            Tile removed = drawnTile;
            drawnTile = null;
            return removed;
        }
        return null;
    }

    public void hasDrawnTile(Tile drawnTile) {

    }
}
