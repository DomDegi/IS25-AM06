package it.polimi.ingsw.galaxytruckerproject.observers;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameState;
import it.polimi.ingsw.galaxytruckerproject.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GameController implements GameObserver {
    private final Game game;
    private final Map<String, String> playerInputs;

    public GameController(Game game) {
        this.game = game;
        playerInputs = new HashMap<>();
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
                ShipsCreation(playerName, input);
            }
            case VERIFY_SHIP_CORRECTNESS: {
                VerifyShipCorrectness(playerName, input);
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
                LookInGameCards(playerName, words);
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
        //Could add that if we are looking at cards we can't call draw, but we can also makes so that the input of look is hidden when calling other methods
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
                //If the third word can't be parsed to a int, do nothing
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

    public void RefuseTile(String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            game.RefuseTile(playerName);
        }
    }


    public void LookInGameCards(String playerName, String[] input) {
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
        System.out.println("All the players have completed the ship creation\n");
    }

    //Turns hourglass if it isn't on and adds 1 to the turn count,
    // if it's already been turned twice, player that turns it needs to have completed his ship
    public void TurnHourglass (String playerName) {
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
    public void DrawCard (String playerName, String input){
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
                CheckShipBoard(playerName, words);
                break;
            case "land":
                game.getFlightBoard().earlyLanding(game.IdentifyPlayerByName(playerName).getPlayerRanking());
                System.out.println(playerName + " made an early landing\n");
                break;
            default: break;
        }
    }

    //Gets the drawnCard from main and sends the input to each Card, depending on return value gives errors
    public void CardEvent(String playerName, String input) {
        String[] words =  input.split(" ");
        Card drawnCard = game.getDrawnCard();
        drawnCard.executeCard(game, playerName,words);
    }


}
