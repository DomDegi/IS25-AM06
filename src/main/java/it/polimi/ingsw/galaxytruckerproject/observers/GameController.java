package it.polimi.ingsw.galaxytruckerproject.observers;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameMode;
import it.polimi.ingsw.galaxytruckerproject.GameState;
import it.polimi.ingsw.galaxytruckerproject.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GameController implements GameObserver {
    private final Game game;
    private final ArrayList<String> playersWithErrors;
    private final Map<String, String> playerInputs;

    public GameController(Game game) {
        this.game = game;
        playerInputs = new HashMap<>();
        this.playersWithErrors = new ArrayList<>();
    }

    @Override
    public void notifyChanges(GameState newState) {
    }

    public void processPlayerInput(String playerName, String input) {
        switch (game.getGameState()) {
            case START_GAME: {
                PlayerSelection(playerName, input);
            }
            case SHIPS_CREATION: {
                shipsCreation(playerName, input);
            }
            case VERIFY_SHIP_CORRECTNESS: {
                if (input.equalsIgnoreCase("shipboard")) {
                    checkShipBoard(playerName, input.toLowerCase().split(" "));
                } else {
                    shipErrorManagement(playerName, input);
                }
                if(playersWithErrors.isEmpty()){
                    verifyShipCorrectness();
                }

            }
            case DRAW_CARD: {
                drawCard(playerName, input);
            }
            case CARD_EVENT: {
                cardEvent(playerName, input);
            }
            case CONCLUDE_GAME: {
                concludeGame();
            }
        }
    }

    //first player that enters the game inputs number of players to start the game with (2 to 4)
    //every player inputs a color as they enter (input ex: playersColor int (only if first)
    public synchronized void PlayerSelection(String playerName, String input) {
        String[] words = input.split(" ");
        if (game.getPlayerCount() == 0 && words.length >= 2) {
            try {
                int playerCount =  Integer.parseInt(words[1]);
                game.setPlayerCount(playerCount);
            }
            catch (NumberFormatException e) {
                System.out.println("First player to input didn't pass an int to specify player count for the game");
            }
        }
        try {
            PlayersColor color = PlayersColor.valueOf(words[0]);
            game.AddPlayer(playerName, color);

            //first one is the number of player in the list, the other is the number of player to start the game with
            if (game.getNumberOfPlayers() == game.getPlayerCount()) {
                game.StartGame();
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println("Not a valid color for the game");
        }
    }

    public synchronized void ShipsCreation(String playerName, String input) {

        String[] words = input.split(" ");

        if (game.getHourglassTurns() == 0 && !words[0].equals("hourglass")) {
            System.out.println("To begin ship creation flip the hourglass\n");
            return;
        }
        else if (words[0].equals("hourglass")) {
            TurnHourglass(playerName);
        }
    }

    public synchronized void shipsCreation(String playerName, String input) {
        String[] words = input.split(" ");
        switch (words[0].toLowerCase()) {

            //draws either from stack or from turned depending on words[1], if turned words[3] is the index of turnedTiles
            case "draw":
                drawTile(playerName, words);
                break;

            //prints the turned tiles ArrayList
            case "turned":
                game.printTurnedTiles();
                break;

            //prints the player's currently booked tiles
            case "booked":
                game.printBookedTiles(playerName);
                break;

            //player refuses currently drawnTile
            case "refuse":
                refuseTile(playerName);
                break;

            //player sets currently drawnTile at coordinates passed with input
            case "set":
                setTile(playerName, words);
                break;

            //player books currently drawnTile
            case "book":
                bookTile(playerName);
                break;

            //shows deck of 3 cards 1, 2, 3. Can only be called when no card is drawn.
            case "look":
                lookGameCards(playerName, words);
                break;

            //turns the hourglass, if hourglass at last possible turn, the player last input has to be completed
            case "hourglass":
                turnHourglass(playerName);
                break;

            //all other player inputs get refused afterward except for turnHourglass
            case "completed":
                completed(playerName);
                break;

            //looks at other player ship, if no second word, looks at yours
            case "shipboard":
                checkShipBoard(playerName, words);
                break;

            //if no case is met, ignore
            default:
                if (Objects.equals(playerInputs.get(playerName), "completed")){
                    addToFlightBoardProcess(playerName,input);
                }
                break;
        }
    }

    public synchronized void drawTile(String playerName, String[] input) {
        //Could add that if we are looking at cards we can't call draw, but we can also make so that the input of look is hidden when calling other methods
        if (Objects.equals(playerInputs.get(playerName), "draw") || Objects.equals(playerInputs.get(playerName), "completed")) {
            return;
        }
        switch (input[1].toLowerCase()) {
            case "stack": {
                Tile drawnTile = game.DrawTile(playerName);
                if (drawnTile == null) {
                    System.out.println("tile stack empty or playerNotFound");
                    return;
                }
                System.out.println("Your drawn tile is:" + drawnTile.toString());
                playerInputs.put(playerName, "draw stack");
                break;
            }
            case "turned": {
                //If there is no third word for the input, do nothing
                if (input.length < 3) {
                    System.out.println("too few inputs: index turned tiles to pick is needed");
                    return;
                }
                //If the third word can't be parsed to an int, do nothing
                try {
                    int index = Integer.parseInt(input[2]);
                    Tile drawnTile = game.DrawTurnedTile(playerName, index);
                    if (drawnTile == null) {
                        System.out.println("no turned tiles or the index is  out of bounds");
                        return;
                    }
                    System.out.println("Your drawn tile is:" + drawnTile.toString());
                    playerInputs.put(playerName, "draw turned " + input[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Didn't input a numerical index for the turned tiles to pick");
                }
                break;
            }
            case "booked" : {
                if (input.length < 3) {
                    System.out.println("too few inputs: index booked tiles to pick is needed");
                    return;
                }
                try {
                    int index = Integer.parseInt(input[2]);
                    Tile drawnTile = game.DrawBookedTile (playerName, index);

                    //the index has to be either 0 or 1
                    if (drawnTile == null) {
                        System.out.println("no booked tiles or the index is  out of bounds");
                        return;
                    }
                    System.out.println("Your drawn tile is:" + drawnTile.toString());
                    playerInputs.put(playerName, "draw booked " + input[2]);
                } catch (RuntimeException e) {
                    System.out.println("Didn't input a numerical index for the booked tile to pick");
                }
            }
        }
    }

    //errors check and management
    public synchronized void verifyShipCorrectness() {
        for (Player player : game.getListOfPlayers()) {
            boolean correctness = player.getShipBoard().verifyCorrectness();
            if (correctness){
                System.out.println("Ship construction is correct for player: " + player.getPlayerName());
            } else {
                System.out.println("Ship construction has errors for player: " + player.getPlayerName()+"Input tiles coordinates to destroy");
                playersWithErrors.add(player.getPlayerName());
            }
        }
    }
    public void shipErrorManagement(String playerName, String input){
        Player player = game.IdentifyPlayerByName(playerName);
        if(player==null){
            return;
        }
        if(!playersWithErrors.contains(playerName)){
            System.out.println("Input refused for player: " + player.getPlayerName());
            return;
        }
        String[] words = input.split(" ");
        try {
            int x = Integer.parseInt(words[0]);
            int y = Integer.parseInt(words[1]);
            Coordinates coordinatesToDestroy = new Coordinates(x, y);
            player.getShipBoard().destroyTile(coordinatesToDestroy);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please input valid numerical coordinates.");
            return;
        }
        boolean correctness = player.getShipBoard().verifyCorrectness();
        if (correctness) {
            if(game.getMode()== GameMode.TRIAL) {
                game.getFlightBoard().setPlayerToLast(player);
            }
            playersWithErrors.remove(playerName);
            return;
        }
        System.out.println("Ship construction has errors for player: " + player.getPlayerName() +"Input tiles coordinates to destroy");
    }

    public void refuseTile(String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            game.RefuseTile(playerName);
        }
    }

    public void lookGameCards(String playerName, String[] input) {
        if (Objects.equals(playerInputs.get(playerName), "draw") && Objects.equals(playerInputs.get(playerName), "concluded")) {
            System.out.println("Can't look at cards while you have drawn a tile or your ship is concluded");
            return;
        }
        if (input.length < 2) {
            System.out.println("no input to choose cards' group to look at");
            return;
        }
        switch (input[1]) {
            case "1":
                game.lookInGameCards1(playerName);
                playerInputs.put(playerName, "look 1");
                break;
            case "2":
                game.lookInGameCards2(playerName);
                playerInputs.put(playerName, "look 2");
                break;
            case "3":
                game.lookInGameCards3(playerName);
                playerInputs.put(playerName, "look 3");
                break;
            default:
                System.out.println("invalid input\n");
                break;
        }
    }

    //set drawn tile on the player's shipboard
    public void setTile (String playerName, String[] input) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            try {
                int x = Integer.parseInt(input[1]);
                int y = Integer.parseInt(input[2]);
                Coordinates coordinates = new Coordinates(x, y);
                if (!game.playerSetTile(playerName, coordinates)) {
                    System.out.println("space at those coordinates is either out of bounds or already fixed\n");
                    return;
                }
                playerInputs.put(playerName, "set " + input[1] + " " + input[2]);
            } catch  (NumberFormatException e) {
                System.out.println("invalid input\n");
            }
        }
    }

    //set currently drawn tile as booked for the player
    public void bookTile(String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            if (!game.playerBookTile (playerName)) {
                System.out.println("booked tile spaces are full\n");
            }
            playerInputs.put(playerName, "book");
        }
    }

    public void checkShipBoard(String playerName, String[] input) {
        ShipBoard shipBoardToCheck = game.getPlayerShipBoard(input[1]);
        if (shipBoardToCheck == null) {
            System.out.println("no player with that name to check");
            return;
        }
        System.out.println(shipBoardToCheck.toString());
    }

    //now no input except hourglass and checkShipboard work and checks if the other player have completed
    //if yes ends shipboard creation phase
    public void completed (String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            refuseTile(playerName);
        }
        playerInputs.put(playerName, "completed");
        for (Map.Entry<String, String> entry : playerInputs.entrySet()) {
            if (!entry.getKey().equals("completed")) {;
                return;
            }
        }
        if(game.getMode()== GameMode.TRIAL) {
            for (Player player : game.getFlightBoard().getInGamePlayers()) {
                if (player.getPlayerName().equals(playerName)) {
                    game.getFlightBoard().addToTrialFlightBoard(player);
                    break;
                }
            }
        }else{
            for (Player player : game.getFlightBoard().getInGamePlayers()) {
                if (player.getPlayerName().equals(playerName)) {
                    addToFlightBoardProcess(player.getPlayerName(),"completed");
                    break;
                }
            }
        }
        game.endShipCreation();
        System.out.println("All the players have completed the ship creation\n");
    }

    public void addToFlightBoardProcess(String playerName,String input){
        if(Objects.equals(playerInputs.get(playerName), "completed"))
            return;
        try {
            int parsedInput = Integer.parseInt(input);
            game.getFlightBoard().addToFlightBoard(game.IdentifyPlayerByName(playerName), parsedInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Unable to parse to an integer.");
        }
    }
    //Turns hourglass isn't on and adds 1 to the turn count,
    // if it's already been turned twice, player that turns it needs to have completed his ship
    public void turnHourglass(String playerName) {
        if (!game.getHourglassState()) {
            System.out.println("hourglass is already going\n");
            return;
        }
        switch (game.getHourglassTurns()) {
            case 0:
                System.out.println(playerName + " starts the game: GO!\n");
                game.StartTimer();
                break;
            case 1:
                System.out.println(playerName + " has flipped the hourglass\n");
                game.StartTimer();
                break;
            case 2:
                if (playerInputs.get(playerName).equals("completed")) {
                    game.StartTimer();
                }
                else {
                    System.out.println("can't make the last hourglass turn when your shipboard isn't complete\n");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + game.getHourglassTurns() + "\n");
        }
    }

    //Number 1 player can draw
    //Every player can check others shipboard
    //Every player can do an early landing
    //When cards are over go to CONCLUDE_GAME state
    public void drawCard (String playerName, String input){
        if (game.getCardsLeft() == 0) {
            game.endCardPhase();
        }

        String[] words =  input.split(" ");

        switch(words[0].toLowerCase()) {
            case "draw":
                if (game.IdentifyPlayerByName(playerName).equals(game.getListOfPlayers().getFirst())){
                game.DrawCard();
                }
                else {
                    System.out.println("The first ranked player has to draw\n");
                }
                break;
            case "shipboard":
                checkShipBoard(playerName, words);
                break;
            case "land":
                game.getFlightBoard().earlyLanding(game.IdentifyPlayerByName(playerName).getPlayerRanking());
                System.out.println(playerName + " made an early landing\n");
                break;
            default: break;
        }
    }

    //Gets the drawnCard from main and sends the input to each Card, depending on return value gives errors
    public void cardEvent(String playerName, String input) {
        String[] words =  input.split(" ");
        Card drawnCard = game.getDrawnCard();
        drawnCard.executeCard(game, playerName,words);
    }


    //PlayerPoint calculation
    public synchronized void concludeGame() {
        for (Player player : game.getFlightBoard().getAllPlayers()) {
            if (player != null) {
                game.getFlightBoard().earlyLanding(player);
                switch (player.getPlayerRanking()){
                    case 1:
                        player.addCredit(8);
                        break;
                    case 2:
                        player.addCredit(6);
                        break;
                    case 3:
                        player.addCredit(4);
                        break;
                    case 4:
                        player.addCredit(2);
                        break;
                }
            }
        }
        ArrayList<Player> coolestPlayers = new ArrayList<>();
        coolestPlayers.add(game.getFlightBoard().getAllPlayers().getFirst());
        for(Player player : game.getFlightBoard().getAllPlayers()){
            if (player.getShipBoard().countExposedConnectors()>coolestPlayers.getFirst().getShipBoard().countExposedConnectors()){
                coolestPlayers.clear();
                coolestPlayers.add(player);
            }
            if (player.getShipBoard().countExposedConnectors()==coolestPlayers.getFirst().getShipBoard().countExposedConnectors()){
                coolestPlayers.add(player);
            }
        }
        for (Player player : coolestPlayers) {
            player.addCredit(4);
        }
        for (Player player : game.getFlightBoard().getAllPlayers()) {
                player.addCredit(player.getShipBoard().convertGoodsToCredit);
        }
        for (Player player : game.getFlightBoard().getAllPlayers()) {
            player.removeCredit(player.getShipBoard().getPenalty());
        }
        // Sort players based on their credits in descending order
        game.setPodium();
        System.out.println("Podium:");
        for (int i = 0; i < game.getListOfAllPlayer().size(); i++) {
            Player player = game.getListOfAllPlayer().get(i);
            System.out.println((i + 1) + ". " + player.getPlayerName() + " - Credits: " + player.getCredit());
        }
    }
}
