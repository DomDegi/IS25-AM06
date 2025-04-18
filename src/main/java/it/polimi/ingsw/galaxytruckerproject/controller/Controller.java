package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.SetColorRequest;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;

/*
 * This controller class is the one that each client
 * is linked to. It handles the interactions between
 * each player and the multiGame server
 */




public class Controller implements ControllerInterface {

    private String nickname;

    private final MultiGameController multiGameController;

    private final VirtualView view;

    private GameController gameController;


    public Controller(MultiGameController multiGameController, VirtualView view) {
        this.multiGameController = multiGameController;
        this.view = view;
    }


    /**
     * useful method to set the gameController for the joined game
     * @param gameController gameController to set
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * this method calls on the multiGameController to login with the chosen nickname
     * @param nickname nickname of player to login
     */
    public void login (String nickname) {
        if (multiGameController.login(nickname, view, this)) {
            this.nickname = nickname;
        }
    }

    /**
     * this method calls on the multiGameController for the create game method
     * @param gameName name chosen for the new game
     * @param playerCount chosen playerCount to reach
     * @param chosenMode chosen gameMode between LVL2 flight and trialFlight
     */
    public void createGame (String gameName ,int playerCount, GameMode chosenMode) {
        multiGameController.createGame(nickname, gameName, playerCount, this, chosenMode);
    }

    /**
     * this method calls on the multiGameController for the joinGame method
     * @param gameName name of the game to enter
     */
    public void joinGame (String gameName) {
        multiGameController.joinGame(nickname, gameName, this);
    }

    /**
     * this method calls on the multiGameController for the leave game method
     */
    public void leaveGame () {
        multiGameController.leaveGame(nickname);
    }

    public void leave() {
        multiGameController.leave(nickname);
    }

    public void chooseColor(String color) {
        if (gameController.checkColorAvailable(nickname, view, color)) {
            gameController.playerAddition(new SetColorRequest(nickname, color));
        }
    }

    public void turnHourglass () {
        gameController.turnHourglass(this.nickname);
    }

    public void drawTileFromStack () {
        gameController.drawTile(view ,nickname,0, false);
    }

    public void drawTileFromTurned (int index) {
        gameController.drawTile(view, nickname, index, true);
    }

    public void refuseTile(){
        gameController.refuseTile(nickname);
    }

    public void lookGameCards(int index) {
        gameController.lookGameCards(nickname, view, index);
    }

    public void stopLookingAtCards(){
        gameController.stopLookingAtCards(view, nickname);
    }

    public void setTile(Tile tile) {
        gameController.setTile(view,nickname,tile);
    }
    public void bookTile(){
        gameController.bookTile(view, nickname);
    }

    public void shipErrorManagement(ArrayList<Coordinates> toRemove) {
        gameController.shipErrorManagement(nickname, view, toRemove);
    }

    public void completedShip() {
        gameController.completed(nickname, view);
    }

    public void drawCard() {
        gameController.drawCard(nickname, view);
    }

    public void earlyLanding () {
        gameController.earlyLanding(nickname, view);
    }
}
