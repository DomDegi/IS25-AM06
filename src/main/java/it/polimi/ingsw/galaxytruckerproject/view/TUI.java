package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.*;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
        switch(newState) {
            case CHOOSE_UI -> System.out.println("you want to use TUI or GUI?");
            case CHOOSE_CONNECTION_TYPE -> System.out.println("you want to use RMI connection or Socket connection?");
            case LOBBY -> System.out.println("you're in the lobby");
            case LOGIN -> System.out.println("choose your nickname");
            case COLOR_CHOICE -> System.out.println("choose your color");
            case ACTION -> System.out.println("you can accept or deny");
            case COORD_REQUEST -> System.out.println("coordinates requested");
            case MANAGE_GOODS -> System.out.println("going to manage goods of the planet ");
            case PLANET_CHOICE -> System.out.println("choose one of the planets to land");
            case S_END_DRAW_TILE_CARD -> System.out.println("now you can draw a tile ore a tile ");
            case S_MANAGE_CARDS -> System.out.println("you're seeing the cards");
            case S_MANAGE_DRAWN_TILE -> System.out.println("you can rotate, position, book or refuse this tile ");
            case S_FINISHED -> System.out.println("you've finished the ship creation, wait to know if your ship is correct");
            case WAIT_OTHER_PLAYER_ACTION -> System.out.println("another player turn");
            case START_SHIP_CREATION -> System.out.println("turn the hourglass to start the ship creation");

        }
    }
    @Override
    public void asksJoinOrCreate() {
        System.out.println("You want to join a game or create a new game");
    }
    @Override
    public void showJoinableGamesList(Map<String, GameController> joinableGames) {
        for(String nameGame : joinableGames.keySet()){
            System.out.println(joinableGames.get(nameGame).toString());
        }
    }
    @Override
    public void askPlayerCount() throws IOException {
        System.out.println("How many players does the game have?");
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
    public void asksPlayersInfo() {

    }

    @Override
    public void showShipsErrors() {

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
            System.out.println(bookedTile.toString());
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
        switch (coordReqType){
            case CHOOSE_BATTERY -> System.out.println("choose your battery to use");
            case CHOOSE_TO_MAINTAIN -> System.out.println("choose a tile of the branch you want to keep");
            case CHOOSE_TO_BREAK -> System.out.println("choose the tiles you want to destroy");
            case CHOOSE_DOUBLE_CANNON -> System.out.println("choose the double cannons you want to use, and they're batteries");
            case CHOOSE_DOUBLE_ENGINE -> System.out.println("choose the double engine you want to use, and they're batteries");
        }
    }

    @Override
    public void asksToTurnTheHourglass() {
        System.out.println("Turn the hourglass");
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() {

    }

    @Override
    public void notifyYourShipIsCorrect() {

    }

    @Override
    public void asksToSetPosition() {

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
    public void asksWhichBranchToKeep(ArrayList<Set<Coordinates>> branch) {

    }

    @Override
    public void showScores(ArrayList<Player> players) {
        for(Player player : players){
            System.out.println(player.getPlayerName() + ": " + player.getCredit() );
        }
    }

    @Override
    public void asksChosenMode() {
        System.out.println("Choose a mode");
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
    public void notifyDrawnCard(Card card) throws RemoteException {
        System.out.println("a new card has been drawn\n "+card.toString());
    }
    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        System.out.println(playerName + "landed on planet " + planet);
    }


}
