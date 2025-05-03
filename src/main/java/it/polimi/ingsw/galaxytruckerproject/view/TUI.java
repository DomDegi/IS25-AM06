package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
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

public class TUI implements DisplayableView{
    @Override
    public void askNickname() throws IOException {
            System.out.println("enter your nickname");
        }
        @Override
        public void askColor() throws RemoteException {
            System.out.println("enter your color");

        }

    @Override
    public void showLoginResponse(boolean success) {
        if(success) {
            System.out.println("You are logged in");
            System.out.println("");
        }
        else {
            System.out.println("login failed, retry");
        }
    }

    @Override
    public void setClientState(ClientState newState) {
        System.out.println(newState.toString());
    }
    @Override
    public void showJoinableGamesList(Map<String, GameController> joinableGames) throws RemoteException {
        for(String nameGame : joinableGames.keySet()){
            System.out.println(joinableGames.get(nameGame).toString());
        }
    }
    @Override
    public void askPlayerCount() throws IOException {
        System.out.println("How many players will the game contain?");
    }
    @Override
    public void createGame() throws IOException {

    }
    @Override
    public void joinGame() throws IOException {

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
    public void showInGamePlayers(ArrayList<Player> players) throws RemoteException {
        for(Player player : players){
            System.out.println(player);
        }
    }
    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) throws RemoteException {
        System.out.println(player + "'s ShipBoard: " + shipBoard.toString());
    }
    @Override
    public void showDrawnTile(Tile drawnTile) {
        System.out.println(drawnTile.toString());
    }
    @Override
    public void showTurnedTiles(Map<Integer,Tile> turnedTiles) {
        for(int i: turnedTiles.keySet()) {
            System.out.println(i+" "+turnedTiles.get(i).toString());
            System.out.println("\n");
        }
    }
    @Override
    public void showBookedTiles(ArrayList<Tile> bookedTiles) throws RemoteException {
        for(Tile bookedTile : bookedTiles){
            System.out.println(bookedTile.toString()+"\n-\n");
        }
    }
    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) throws RemoteException {
        for(Card card : inGameCards){
            System.out.println(card.toString());
        }
    }
    @Override
    public void showWrongInputMessage (){
        System.out.println("you entered a wrong input");
    }
    @Override
    public void asksToRollTheDices() throws RemoteException {
        System.out.println("Roll the dices");
    }
    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {
        System.out.println("the dice roll is: " + diceRoll);
    }

    @Override
    public void asksToChooseStartingPosition() throws RemoteException {
        System.out.println("Choose starting position");
    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) {
        System.out.println(coordReqType.toString());
    }

    @Override
    public void asksToTurnTheHourglass() throws RemoteException {
        System.out.println("Turn the hourglass");
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        System.out.println("You can draw this card deck");
    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {
        System.out.println("Your ship is correct");
    }
    @Override
    public void asksToMakeAChoice() {
        System.out.println("Please, make a choice");
    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) {
        for(Goods good : goods){
            System.out.println(good.toString());
        }
    }

    @Override
    public void asksToRemoveGoods() throws RemoteException {
        System.out.println("Choose good/goods to remove");
    }

    @Override
    public void asksToRemoveCrew() throws RemoteException {
        System.out.println("Choose crew to remove");
    }

    @Override
    public void showScores(ArrayList<Player> players) throws RemoteException {
        for(Player player : players){
            System.out.println(player.getPlayerName() + ": " + player.getCredit() );
        }
    }
    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) throws RemoteException {
        for(LightPlayer player: lightFlightboard.getInGamePlayers())
        {
            System.out.println("Player: " + player.toString());
        }
    }
    @Override
    public void printShipboard(LightShipBoard lightShipBoard) throws RemoteException {
        System.out.println("this is your shipboard now:\n"+lightShipBoard.toString());
    }

    @Override
    public void printProjectile(Projectile projectile) throws RemoteException {
        System.out.println("you're under attack from:\n"+projectile.toString());
    }

    @Override
    public void goodsPrinter(ArrayList<Goods> goodsArray) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED= "\u001B[31m";
        int i=1;
        for(Goods goods: goodsArray){
            if(goods.getColor()==GoodsColor.BLUE){
                System.out.println(i+" - " + ANSI_BLUE + goods.getColor() + ANSI_RESET+" good: it equals to " + goods.getValue()+" cosmic credits");
            }else if(goods.getColor()==GoodsColor.GREEN){
                System.out.println(i+" - " + ANSI_GREEN + goods.getColor() + ANSI_RESET+" good: it equals to " + goods.getValue()+" cosmic credits");
            }else if(goods.getColor()==GoodsColor.YELLOW){
                System.out.println(i+" - " + ANSI_YELLOW + goods.getColor() + ANSI_RESET+" good: it equals to " + goods.getValue()+" cosmic credits");
            }else if(goods.getColor()==GoodsColor.RED){
                System.out.println(i+" - " + ANSI_RED + goods.getColor() + ANSI_RESET+" good: it equals to " + goods.getValue()+" cosmic credits");
            }
            i++;
        }
    }

    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return new TUI();
    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        System.out.println("a new card has been drawn\n "+card.toString());
    }
    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        System.out.println(playerName + "landed on planet " + planet);
    }

    @Override
    public void wrongLocalInput() throws RemoteException {
        System.out.println("You entered a wrong input");
    }
    @Override
    public void showCard(Card card) throws RemoteException {
        System.out.println(card.toString());
    }

}
