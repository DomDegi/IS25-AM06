package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class TUI implements DisplayableView {
    public TUI() {
    }

    @Override
    public void showLoginResponse(boolean success) {
        if (success) {
            System.out.println("You are logged in");
        } else {
            System.out.println("login failed, retry");
        }
    }

    @Override
    public void setClientState(ClientState newState) {
        System.out.println(newState.toString());
    }

    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        for (GameInfo game : joinableGames) {
            System.out.println(game.toString());
        }

    }

    @Override
    public void showGenericMessage(String genericMessage) throws RemoteException {
        System.out.println(genericMessage);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }


    @Override
    public void showDrawnTile(Tile drawnTile) {
        System.out.println(drawnTile.toString());
    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {
        for (int i : turnedTiles.keySet()) {
            System.out.println(i + " " + turnedTiles.get(i).toString());
            System.out.println("\n");
        }
    }

    @Override
    public void showWrongInputMessage() {
        System.out.println("you entered a wrong input");
    }

    @Override
    public void asksToRollTheDices() throws RemoteException {
        System.out.println("roll the dices");
    }

    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {
        System.out.println("the dice roll is: " + diceRoll);
    }

    @Override
    public void asksToChooseStartingPosition() throws RemoteException {
        System.out.println("choose starting position");
    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) {
        System.out.println(coordReqType.toString());
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        System.out.println("you can draw this card deck");
    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {
        System.out.println("your ship is correct");
    }

    @Override
    public void asksToMakeAChoice() {
        System.out.println("please, make a choice");
    }


    @Override
    public void showScores(ArrayList<Player> players) throws RemoteException {
        for (Player player : players) {
            System.out.println(player.getPlayerName() + ": " + player.getCredit());
        }
    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard){
        for (LightPlayer player : lightFlightboard.getInGamePlayers()) {
            System.out.println("Player: " + player.toString());
        }
    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard){
        System.out.println("this is your shipboard now:\n" + lightShipBoard.toString());
    }

    @Override
    public void printProjectile(Projectile projectile) {
        System.out.println("you're under attack from:\n" + projectile.toString());
    }

    @Override
    public void connected() {
        System.out.println("you are connected");
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        System.out.println("your gameMode is: " + gameMode.toString());
    }


    @Override
    public void goodsPrinter(ArrayList<Goods> goodsArray) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED = "\u001B[31m";
        int i = 1;
        for (Goods goods : goodsArray) {
            if (goods.getColor() == GoodsColor.BLUE) {
                System.out.println(i + " - " + ANSI_BLUE + goods.getColor() + ANSI_RESET + " good: it equals to " + goods.getValue() + " cosmic credits");
            } else if (goods.getColor() == GoodsColor.GREEN) {
                System.out.println(i + " - " + ANSI_GREEN + goods.getColor() + ANSI_RESET + " good: it equals to " + goods.getValue() + " cosmic credits");
            } else if (goods.getColor() == GoodsColor.YELLOW) {
                System.out.println(i + " - " + ANSI_YELLOW + goods.getColor() + ANSI_RESET + " good: it equals to " + goods.getValue() + " cosmic credits");
            } else if (goods.getColor() == GoodsColor.RED) {
                System.out.println(i + " - " + ANSI_RED + goods.getColor() + ANSI_RESET + " good: it equals to " + goods.getValue() + " cosmic credits");
            }
            i++;
        }
    }

    @Override
    public void notifyTurnedHourglass(int i) throws RemoteException {
        System.out.println("hourglass has been turned for the "+i+" time");
    }

    @Override
    public void notifyEndOfTime() throws RemoteException {
        System.out.println("end of time");
    }

    @Override
    public void printCabins(Tile cabins) {
        System.out.println("choose equip type of:" + cabins.toString());
    }

    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return new TUI();
    }


    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        System.out.println("a new card has been drawn\n " + card.toString());
    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        System.out.println(playerName + "landed on planet " + planet);
    }

    @Override
    public void wrongLocalInput() {
        System.out.println("You entered a wrong input");
    }

    @Override
    public void showCard(ArrayList<Card> cards) {
        for(Card card : cards) {
            System.out.println(card.toString());
        }
    }


}
