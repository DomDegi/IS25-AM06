package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TUI implements ViewInterface{
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

    }

    @Override
    public void showErrorMessage(String errorMessage) {

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
    public void showTurnedTiles(ArrayList<Tile> turnedTiles) {
        for(Tile turnedTile : turnedTiles){
            System.out.println(turnedTile.toString());
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
    public void asksToRollTheDices() {
        System.out.println("Roll the dices");
    }

    @Override
    public void asksToChooseStartingPosition() {
        System.out.println("Choose starting position");
    }

    @Override
    public void asksToInputCoordinates() {
        System.out.println("Input coordinates");
    }

    @Override
    public void asksToTurnTheHourglass() {
        System.out.println("Turn the hourglass");
    }

    @Override
    public void asksToMakeAChoice() {
        System.out.println("Please, make a choice");
    }

    @Override
    public void asksPlanetChoice() {
        System.out.println("Choose a planet");
    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) {
        for(Goods good : goods){
            System.out.println(good.toString());
        }
    }

    @Override
    public void asksToUseEngines() {
        System.out.println("Choose double engines");
    }

    @Override
    public void asksToUseCannons() {
        System.out.println("Choose double cannons");
    }

    @Override
    public void asksToUseBatteries() {
        System.out.println("Choose batteries to consume");
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
    public void showScores() {
        System.out.println("Your score is:");
    }

    @Override
    public void asksChosenMode() {
        System.out.println("Choose a mode");
    }

    @Override
    public void updateLightModel(Message message) {
        System.out.println(message.toString());
    }
}
