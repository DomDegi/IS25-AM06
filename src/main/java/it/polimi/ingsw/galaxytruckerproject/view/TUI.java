package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * A text-based user interface (TUI) for interacting with the game.
 * This class implements the `DisplayableView` interface and handles
 * the visual representation of the game's state in the terminal.
 */
public class TUI implements DisplayableView {
    public TUI() {
    }

    /**
     * Displays the result of the login attempt.
     *
     * @param success true if the login was successful, false otherwise.
     */
    @Override
    public void showLoginResponse(boolean success) {
        if (success) {
            System.out.println("You are logged in");
        } else {
            System.out.println("login failed, retry");
        }
    }

    /**
     * Updates the client state and displays it.
     *
     * @param newState the new state of the client.
     */
    @Override
    public void setClientState(ClientState newState) {
        System.out.println(newState.toString());
    }

    /**
     * Displays a list of games that are available to join.
     *
     * @param joinableGames a list of games available to join.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        for (GameInfo game : joinableGames) {
            System.out.println(game.toString());
        }

    }

    /**
     * Displays a generic message.
     *
     * @param genericMessage the message to be displayed.
     */
    @Override
    public void showGenericMessage(String genericMessage)  {
        System.out.println(genericMessage);
    }

    /**
     * Displays a penalty message indicating that the specified player has received a penalty.
     *
     * @param playerName the name of the player who received the penalty.
     * @param penalty the penalty that was applied.
     */
    @Override
    public void victimOfThePenalty(String playerName, Penalty penalty) {
        System.out.println(playerName +" has received the following penalty:"+ penalty.toString());
    }

    /**
     * Displays an error message.
     *
     * @param errorMessage the error message to be displayed.
     */
    @Override
    public void showErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }

    /**
     * Displays the drawn tile on the screen.
     *
     * @param drawnTile the tile that was drawn.
     */
    @Override
    public void showDrawnTile(Tile drawnTile) {
        System.out.println(drawnTile.toString());
    }

    /**
     * Indicates that the player is sending coordinates.
     */
    @Override
    public void sendingCoordinates() {
        System.out.println("i'm sending the coordinates");
    }

    /**
     * Displays the tiles that have been turned during the game.
     *
     * @param turnedTiles a map of turned tiles indexed by their identifier.
     */
    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {
        printDrawnTiles(turnedTiles);
    }

    /**
     * Displays the cargo selected by the player.
     *
     * @param coordinates the coordinates of the selected cargo.
     */
    @Override
    public void cargoSelected(Coordinates coordinates) {
        System.out.println("you selected the cargo in: "+ coordinates.toString());
    }
    /**
     * Displays a message indicating that the player entered a wrong input.
     */
    @Override
    public void showWrongInputMessage() {
        System.out.println("you entered a wrong input");
    }
    /**
     * Asks the player to roll the dice.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void asksToRollTheDices() throws RemoteException {
        System.out.println("roll the dices");
    }
    /**
     * Displays the result of the dice roll.
     *
     * @param diceRoll the value of the dice roll.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {
        System.out.println("the dice roll is: " + diceRoll);
    }
    /**
     * Asks the player to choose a starting position.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void asksToChooseStartingPosition() throws RemoteException {
        System.out.println("choose starting position");
    }
    /**
     * Asks the player to input coordinates based on the requested type.
     *
     * @param coordReqType the type of coordinate request.
     */
    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) {
        System.out.println(coordReqType.toString());
    }
    /**
     * Notifies the player that they can draw from the card deck.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        System.out.println("you can draw this card deck");
    }
    /**
     * Notifies the player that their ship configuration is correct.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {
        System.out.println("your ship is correct");
    }
    /**
     * Asks the player to make a choice.
     */
    @Override
    public void asksToMakeAChoice() {
        System.out.println("please, make a choice");
    }
    /**
     * Displays the scores of all players.
     *
     * @param scores a map of player names to their scores.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void showScores(Map<String, Integer> scores) throws RemoteException {
        for (String playerName : scores.keySet()) {
            System.out.println(playerName + ": " + scores.get(playerName));
        }
    }

    /**
     * Prints the flight board with the in-game players.
     *
     * @param lightFlightboard the light version of the flight board.
     */
    @Override
    public void printFlightboard(LightFlightboard lightFlightboard){
        for (LightPlayer player : lightFlightboard.getInGamePlayers()) {
            System.out.println("Player: " + player.toString());
        }
    }
    /**
     * Checks the state of the shipboard.
     *
     * @param lightShipBoard the light version of the shipboard.
     */
    @Override
    public void checkShipboard(LightShipBoard lightShipBoard) {
        printShipboard(lightShipBoard);
    }
    /**
     * Prints the shipboard in a readable format.
     *
     * @param lightShipBoard the light version of the shipboard.
     */
    @Override
    public void printShipboard(LightShipBoard lightShipBoard) {
        for(int x=0;x<=4;x++){
            if(x==0)
                System.out.println("         ┌────────┬────────┬────────┬────────┬────────┬────────┬────────┐");
            else
                System.out.println("         ├────────┼────────┼────────┼────────┼────────┼────────┼────────┤");
            for(int r=0;r<=2;r++){
                System.out.print("         ");
                for (int y = 0; y <= 6; y++) {
                    if (lightShipBoard.getTilesTable()[x][y].isEmpty()) {
                        System.out.print("│        ");
                    } else {
                        if (r == 0)
                            System.out.print("│" + lightShipBoard.getTile(x, y).toString1());
                        if (r == 1)
                            System.out.print("│" + lightShipBoard.getTile(x, y).toString2());
                        if (r == 2)
                            System.out.print("│" + lightShipBoard.getTile(x, y).toString3());
                    }
                }
                System.out.println("│");
            }
        }
        System.out.println("         └────────┴────────┴────────┴────────┴────────┴────────┴────────┘");
    }
    /**
     * Prints the booked tiles on the shipboard.
     *
     * @param lightShipBoard the light version of the shipboard.
     */
    public void printBooked(LightShipBoard lightShipBoard) {
        System.out.println("         ┌────────┬────────┐");
            for(int r=0;r<=2;r++){
                System.out.print("         ");
                if (lightShipBoard.getBookedTiles().isEmpty())
                    System.out.print("│        │        ");
                for (Tile tile: lightShipBoard.getBookedTiles()) {
                        if (r == 0)
                            System.out.print("│" + tile.toString1());
                        if (r == 1)
                            System.out.print("│" + tile.toString2());
                        if (r == 2)
                            System.out.print("│" + tile.toString3());
                }
                if (lightShipBoard.getBookedTiles().size()==1)
                    System.out.print("│        ");
                System.out.println("│");
            }
        System.out.println("         └────────┴────────┘");
    }

    /**
     * Prompts that the crew member was successfully positioned
     */
    @Override
    public void crewPositioned() {
        System.out.println("crew positioned");
    }
    /**
     * Prints the tiles drawn by the player.
     *
     * @param drawnTiles a map of drawn tiles indexed by their identifier.
     */
    public void printDrawnTiles(Map<Integer, Tile> drawnTiles) {
        ArrayList<Tile> tilesList = new ArrayList<>(drawnTiles.values());
        boolean limit=false;
        int index1=0;
        int index2=0;
        int index3=0;
        System.out.println("┌────────┬────────┬────────┬────────┬────────┬────────┬────────┬────────┬────────┐");
        while (true) {
            for (int i = 0; i < 9; i++) {
                if (index1 == drawnTiles.size()) {
                    System.out.print("│        ");
                }else{
                    System.out.print("│" + tilesList.get(index1).toString1());
                    index1++;
                }
            }
            System.out.println("│");
            for (int i = 0; i < 9; i++) {
                if (index2 == drawnTiles.size()) {
                    System.out.print("│        ");
                }else{
                    System.out.print("│" + tilesList.get(index2).toString2());
                    index2++;
                }
            }
            System.out.println("│");
            for (int i = 0; i < 9; i++) {
                if (index3 == drawnTiles.size()) {
                    System.out.print("│        ");
                    limit=true;
                }else{
                    System.out.print("│" + tilesList.get(index3).toString3());
                    index3++;
                }
            }
            System.out.println("│");
            if (limit) {
                System.out.println("└────────┴────────┴────────┴────────┴────────┴────────┴────────┴────────┴────────┘");
                break;
            }
            System.out.println("├────────┼────────┼────────┼────────┼────────┼────────┼────────┼────────┼────────┤");
        }
    }
    /**
     * Sets the client controller for the view.
     *
     * @param clientController the client controller to set.
     */
    @Override
    public void setClientController(ClientController clientController) {

    }
    /**
     * Displays information about the projectile attacking the player.
     *
     * @param projectile the projectile attacking the player.
     */
    @Override
    public void printProjectile(Projectile projectile) {
        System.out.println("you're under attack from:\n" + projectile.toString());
    }

    /**
     * Notifies the player that they are connected to the game.
     */
    @Override
    public void connected() {
        System.out.println("you are connected");
    }
    /**
     * Sets the game mode for the current session.
     *
     * @param gameMode the game mode to set.
     */
    @Override
    public void setGameMode(GameMode gameMode) {
        System.out.println("your gameMode is: " + gameMode.toString());
    }

    /**
     * Displays the goods available to the player, along with their values in cosmic credits.
     *
     * @param goodsArray a list of goods to display.
     */
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
    /**
     * Notifies the player that the hourglass has turned for the specified time.
     *
     * @param i the number of turns or time periods.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyTurnedHourglass(int i) throws RemoteException {
        System.out.println("hourglass has been turned for the "+i+" time");
    }
    /**
     * Notifies the player that time has ended for the current phase.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyEndOfTime() throws RemoteException {
        System.out.println("end of time");
    }
    /**
     * Notifies the player that they have landed early.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyEarlyLanding() throws RemoteException {
        System.out.println("we're sorry, you landed");
    }
    /**
     * Notifies the player of the combat zone strength for a given player.
     *
     * @param playerName the name of the player.
     * @param strength the strength value in the combat zone.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyCombatZoneStrength(String playerName, float strength) throws RemoteException {
        System.out.println(playerName+" cannon strength is "+strength);
    }
    /**
     * Notifies the player of the combat zone engine power for a given player.
     *
     * @param playerName the name of the player.
     * @param strength the engine power value in the combat zone.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyCombatZoneEngine(String playerName, float strength) throws RemoteException {
        System.out.println(playerName+" engine power is "+strength);
    }
    /**
     * Notifies the player of the combat zone crew count for a given player.
     *
     * @param playerName the name of the player.
     * @param crew the number of crew members in the combat zone.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyCombatZoneCrew(String playerName, int crew) throws RemoteException {
        System.out.println(playerName+" num of crewmates is "+crew);
    }
    /**
     * Notifies the player about the podium and the rankings of the players.
     *
     * @param players a list of players ranked by their scores.
     * @throws RemoteException if there is an issue with remote communication.
     */
    @Override
    public void notifyPodium(ArrayList<Player> players) throws RemoteException {
        System.out.println("flightboard:");
        for (Player player : players) {
            System.out.println(player.getPlayerName()+" position:"+player.getPlayerPosition()+" rank:"+player.getPlayerRanking());
        }
    }

    /**
     * Prompts to choose the equipment type of the cabin
     * @param cabins the cabin
     */
    @Override
    public void printCabins(Tile cabins) {
        System.out.println("choose equip type of:" + cabins.toString());
    }

    /**
     * creates and return a new TUI instance
     * @return an object that extends DisplayableVIew (TUI or GUI)
     * @throws RemoteException RMI
     */
    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return new TUI();
    }


    /**
     * Prompts that a new card has been drawn
     * @param card the card that was drawn.
     * @throws RemoteException RMI
     */
    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        System.out.println("a new card has been drawn\n " + card.toString());
    }

    /**
     * Notifies that a player landed on a planet
     * @param playerName the name of the player who landed.
     * @param planet the planet on which the player landed.
     * @throws RemoteException RMI
     */
    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        System.out.println(playerName + " landed on planet " + planet);
    }

    /**
     * Tells the player that they entered a wrong input
     */
    @Override
    public void wrongLocalInput() {
        System.out.println("You entered a wrong input");
    }

    /**
     * notifies the player that the coordinates where successfully selected
     */
    @Override
    public void coordinateSelected() {
        System.out.println("coordinate selected correctly");
    }
    /**
     * Displays the available card choices to the player.
     *
     * @param cards a list of available cards.
     */
    @Override
    public void showDeck(ArrayList<Card> cards) {

        if(cards!=null)
            for(Card card : cards) {
                if( card !=null)
                    System.out.println(card.toString());
        }
    }

    /**
     * Notifies the player that a new player has joined the game or reconnected.
     *
     * @param expectedPlayer the expected number of players.
     * @param currentPlayer the current number of players.
     * @param reconnected true if the player reconnected, false if they are a new player.
     */
    @Override
    public void notifyPlayerJoined(int expectedPlayer, int currentPlayer, boolean reconnected) {
        if (!reconnected) {
            System.out.println("new player joined the game: " + currentPlayer + "/" + expectedPlayer);
        } else  {
            System.out.println("player reconnected: " + currentPlayer + "/" + expectedPlayer);
        }
    }
    /**
     * Displays the available decks for the game.
     */
    @Override
    public void showAvailableDecks() {

    }

    /**
     * Displays the name of the player that left the game as a print on the cli
     * @param playerName The name of the player
     */
    @Override
    public void notifyPlayerLeftTheGame(String playerName) {
        System.out.println(playerName + " has left the game");
    }

    /**
     * Displays the name of the player that disconnected from the game as a print on the cli
     * @param playerName The name of the player
     */
    @Override
    public void notifyPlayerDisconnected(String playerName) {
        System.out.println(playerName + " has disconnected from the game");
    }
}
