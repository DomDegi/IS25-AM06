package it.polimi.ingsw.galaxytruckerproject.observers;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameState;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GameController implements GameObserver {
    private final Game game;
    private Map<String, String> playerInputs;

    public GameController(Game game) {
        this.game = game;
        playerInputs = new HashMap<>();
    }

    @Override
    public void notifyChanges(GameState newState) {
        if (newState == GameState.SHIPS_CREATION){
            game.StartTimer();
        }
    }

    public void processPlayerInput(String playerName, String input) {
        switch (game.getGameState()) {
            case START_GAME: {
                StartGame(playerName, input);
            }
            case SHIPS_CREATION: {
                ShipsCreation(playerName, input);
            }
            case VERIFY_SHIP_CORRECTNESS: {
                VerifyShipCorrectness(playerName);
            }
            case DRAW_CARD: {
                DrawCard(playerName, input);
            }
            case CARD_EVENT: {
                CardEvent(playerName, input);
            }
            case CONCLUDE_GAME: {
                ConcludeGame(playerName, input);
            }
        }
    }

    //Adds player to the game and starts if the first added player types START or the player are 4
    public synchronized void StartGame(String playerName, String input) {

        String[] words = input.split(" ");
        //player that types start has to already be in the game
        if (game.getNumberOfPlayers() >= 2 && words[0].equalsIgnoreCase("start") && game.IdentifyPlayerByName(playerName) != null) {
            System.out.println(playerName + " starts the timer: GO!");
            game.StartGame();
            return;
        }
        String color = words[0];
        try {
            PlayersColor.valueOf(color);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid color");
            return;
        }
        //Adds a player whatever they type, input is not important
        if (game.getNumberOfPlayers() < 4) {
            game.AddPlayer(playerName, PlayersColor.valueOf(color));
        } else {
            System.out.println("can't have more than 4 players, waiting on Start input");
        }
    }

    public synchronized void ShipsCreation(String playerName, String input) {
        String[] words = input.split(" ");
        switch (words[0].toLowerCase()) {

            //draws either from stack or from turned depending on words[1], if turned words[3] is the index of turnedTiles
            case "draw":
                DrawTile(playerName, words);
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
                RefuseTile(playerName);
                break;

            //player sets currently drawnTile at coordinates passed with input
            case "set":
                setTile(playerName, words);
                break;

            //player books currently drawnTile
            case "book":
                BookTile(playerName);
                break;

            //shows deck of 3 cards 1, 2, 3. Can only be called when no card is drawn.
            case "look":
                LookGameCards(playerName, words);
                break;

            //turns the hourglass, if hourglass at last possible turn, the player last input has to be completed
            case "hourglass":
                TurnHourglass(playerName);
                break;

            //all other player inputs get refused afterward except for TurnHourglass
            case "completed":
                Completed(playerName);
                break;

            //looks at other player ship, if no second word, looks at yours
            case "shipboard":
                CheckShipBoard(playerName, words);
                break;

            //if no case is met, ignore
            default:
                break;
        }
    }

    public synchronized void DrawTile(String playerName, String[] input) {
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

    public synchronized Map<Player,Boolean> VerifyShipCorrectness(String playerName) {
        Player player = game.getListOfPlayers().stream()
                .filter(p -> p.getPlayerName().equals(playerName))
                .findFirst()
                .orElse(null);
        Map<Player,Boolean> check=new HashMap<>();
        if (player == null) {
            System.out.println("Player not found.");
            check.put(player,true);
            return check;
        }
        boolean correctness = player.getShipBoard().verifyCorrectness();
        if (correctness){
            System.out.println("Ship construction is correct for player: " + playerName);
            check.put(player,true);
            return check;
        } else {
            System.out.println("Ship construction has errors for player: " + playerName+"Input tiles coordinates to destroy");
            check.put(player,false);
        }
        return check;
    }
    
    public synchronized Map<Player,Boolean> shipErrorManagement(Player player, String input){
        String[] words = input.split(" ");
        Map<Player,Boolean> check=new HashMap<>();
        Coordinates coordinatesToDestroy = new Coordinates(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
        player.getShipBoard().destroyTile(coordinatesToDestroy);
        boolean correctness = player.getShipBoard().verifyCorrectness();
        if (correctness) {
            check.put(player, true);
            return check;
        }
        System.out.println("Ship construction has errors for player: " + player.getPlayerName() +"Input tiles coordinates to destroy");
        check.put(player, false);
        return check;
    }

    public void RefuseTile(String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            game.RefuseTile(playerName);
        }
    }


    public void LookGameCards(String playerName, String[] input) {
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
    public void BookTile(String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            if (!game.playerBookTile (playerName)) {
                System.out.println("booked tile spaces are full\n");
            }
            playerInputs.put(playerName, "book");
        }
    }

    public void CheckShipBoard(String playerName, String[] input) {
        ShipBoard shipBoardToCheck = game.getPlayerShipBoard(input[1]);
        if (shipBoardToCheck == null) {
            System.out.println("no player with that name to check");
            return;
        }
        System.out.println(shipBoardToCheck.toString());
    }

    //now no input except hourglass and checkShipboard work and checks if the other player have completed
    //if yes ends shipboard creation phase
    public void Completed (String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            RefuseTile(playerName);
        }
        playerInputs.put(playerName, "completed");
        for (Map.Entry<String, String> entry : playerInputs.entrySet()) {
            if (!entry.getKey().equals("completed")) {;
                return;
            }
        }
        game.endShipCreation();
        System.out.println("All the players have completed the ship creation");
    }

    //Turns hourglass isn't on and adds 1 to the turn count,
    // if it's already been turned twice, player that turns it needs to have completed his ship
    public void TurnHourglass (String playerName) {
        if (!game.getHourglassState()) {
            System.out.println("hourglass is already going");
            return;
        }
        if (game.getHourglassTurns() == 2) {
            if (playerInputs.get(playerName).equals("completed")) {
                game.StartTimer();
            }
            else {
                System.out.println("can't make the last hourglass turn when your shipboard isn't complete");
            }
        }
    }
}
