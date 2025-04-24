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
        public void askColor() {
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
    public void showJoinableGamesList(Map<String, GameController> joinableGames) {
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
    public void showGenericMessage(String genericMessage) {
        System.out.println(genericMessage);
    }
    @Override
    public void showErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }
    @Override
    public void showInGamePlayers(ArrayList<Player> players) {
        for(Player player : players){
            System.out.println(player);
        }
    }
    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) {
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
    public void showBookedTiles(ArrayList<Tile> bookedTiles) {
        for(Tile bookedTile : bookedTiles){
            System.out.println(bookedTile.toString()+"\n-\n");
        }
    }
    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) {
        for(Card card : inGameCards){
            System.out.println(card.toString());
        }
    }
    @Override
    public void showWrongInputMessage (){
        System.out.println("you entered a wrong input");
    }
    @Override
    public void asksToRollTheDices() {
        System.out.println("Roll the dices");
    }
    @Override
    public void showDiceRoll(int diceRoll) {
        System.out.println("the dice roll is: " + diceRoll);
    }

    @Override
    public void asksToChooseStartingPosition() {
        System.out.println("Choose starting position");
    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) {
        System.out.println(coordReqType.toString());
    }

    @Override
    public void asksToTurnTheHourglass() {
        System.out.println("Turn the hourglass");
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() {
        System.out.println("You can draw this card deck");
    }

    @Override
    public void notifyYourShipIsCorrect() {
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
    public void asksToRemoveGoods() {
        System.out.println("Choose good/goods to remove");
    }

    @Override
    public void asksToRemoveCrew() {
        System.out.println("Choose crew to remove");
    }

    @Override
    public void showScores(ArrayList<Player> players) {
        for(Player player : players){
            System.out.println(player.getPlayerName() + ": " + player.getCredit() );
        }
    }
    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) {
        for(LightPlayer player: lightFlightboard.getInGamePlayers())
        {
            System.out.println("Player: " + player.toString());
        }
    }
    @Override
    public void printShipboard(LightShipBoard lightShipBoard) {
        System.out.println("this is your shipboard now:\n"+lightShipBoard.toString());
    }

    @Override
    public void printProjectile(Projectile projectile) {
        System.out.println("you're under attack from:\n"+projectile.toString());
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
    public void wrongLocalInput() {
        System.out.println("You entered a wrong input");
    }
    @Override
    public void showCard(Card card) {
        System.out.println(card.toString());
    }


}
