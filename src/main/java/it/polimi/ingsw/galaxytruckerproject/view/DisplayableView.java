package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface DisplayableView extends ViewInterface{
    /**
     * shows the player the tiles that got turned from player refusing them
     * @param turnedTiles the current array in the model
     */
    void showTurnedTiles (Map<Integer, Tile> turnedTiles);

    void wrongLocalInput();

    void showCard(ArrayList<Card> cards);

    void printFlightboard(LightFlightboard lightFlightboard);

    void printShipboard(LightShipBoard lightShipBoard);

    void printProjectile(Projectile projectile);

    void printCabins(Tile cabins);

    public void printDrawnTiles(Map<Integer, Tile> drawnTiles);

    void goodsPrinter (ArrayList<Goods> goodsArray);

    void printBooked(LightShipBoard shipBoard);

    void crewPositioned();

    void showGenericMessage (String genericMessage);
}
