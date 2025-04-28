package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
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
    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * this method calls on the multiGameController to login with the chosen nickname
     * @param nickname nickname of player to login
     */
    @Override
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
    @Override
    public void createGame (String gameName ,int playerCount, GameMode chosenMode) {
        multiGameController.createGame(nickname, gameName, playerCount, this, chosenMode);
    }

    /**
     * this method calls on the multiGameController for the joinGame method
     * @param gameName name of the game to enter
     */
    @Override
    public void joinGame (String gameName) {
        multiGameController.joinGame(nickname, gameName, this);
    }

    /**
     * this method calls on the multiGameController for the leave game method
     */
    @Override
    public void leaveGame () {
        multiGameController.leaveGame(nickname);
    }

    @Override
    public void leave() {
        multiGameController.leave(nickname);
    }

    @Override
    public void chooseColor(PlayersColor color) {
        if (gameController.checkColorAvailable(nickname, view, color)) {
            gameController.playerAddition(nickname, color);
        }
    }

    @Override
    public void turnHourglass () {
        gameController.turnHourglass(this.nickname);
    }

    @Override
    public void drawTileFromStack () {
        gameController.drawTile(view ,nickname,0, false);
    }

    @Override
    public void drawTileFromTurned (int index) {
        gameController.drawTile(view, nickname, index, true);
    }

    @Override
    public void refuseTile(){
        gameController.refuseTile(nickname);
    }

    @Override
    public void lookGameCards(int index) {
        gameController.lookGameCards(nickname, view, index);
    }

    @Override
    public void stopLookingAtCards(){
        gameController.stopLookingAtCards(view, nickname);
    }

    @Override
    public void setTile(Tile tile) {
        gameController.setTile(view,nickname,tile);
    }

    @Override
    public void bookTile(){
        gameController.bookTile(view, nickname);
    }

    @Override
    public void shipErrorManagement(ArrayList<Coordinates> toRemove) {
        gameController.shipErrorManagement(nickname, view, toRemove);
    }

    @Override
    public void completedShip() {
        gameController.completed(nickname, view);
    }

    @Override
    public void drawCard() {
        gameController.drawCard(nickname, view);
    }

    @Override
    public void earlyLanding () {
        gameController.earlyLanding(nickname, view);
    }

    @Override
    public void useCannons(float doublePower, ArrayList<Coordinates> batteries) {
        gameController.playerUsesCannons(nickname, doublePower, batteries);
    }

    @Override
    public void useEngines(int numberOfDoubleEngines, ArrayList<Coordinates> batteries) {
        gameController.playerUsesEngines(nickname, numberOfDoubleEngines, batteries);
    }

    @Override
    public void manageGoods(int clientCreditsToVerify, ArrayList<CargoHold> updatedCargos) {
        gameController.playerManagesGoods(nickname, clientCreditsToVerify, updatedCargos);
    }

    @Override
    public void makeAChoice(boolean choice) {
        gameController.playerMakesAChoice(nickname, choice);
    }

    @Override
    public void choosePlanet(int planet) {
        gameController.playerChoosesPlanet(nickname, planet);
    }

    @Override
    public void pickCrewMembers(ArrayList<Tile> cabins) {
        gameController.playerPicksCrewMembers(nickname, view, cabins);
    }

    @Override
    public void setPosition(int position) {
        gameController.setPosition(nickname, view, position);
    }

    public void removeCrew (ArrayList<Coordinates> toRemoveFrom) {
        gameController.playerRemovesCrew(nickname, toRemoveFrom);
    }

    public void removeGoods(ArrayList<Coordinates> fromHere) {

    }

    public void useBatteries(ArrayList<Coordinates> batteries) throws Exception {

    }

    @Override
    public void rollTheDices() throws Exception {

    }

    @Override
    public void chooseBranch(ArrayList<Coordinates> thisOne) throws Exception {

    }
}

