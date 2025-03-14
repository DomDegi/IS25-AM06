package it.polimi.ingsw.galaxytruckerproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static it.polimi.ingsw.galaxytruckerproject.GameState.*


public class GameController {
    private final Game game;
    private Map<String, String> playerInputs;

    public GameController(Game game) {
        this.game = game;
        playerInputs = new HashMap<>();
    }

    public void processPlayerInput (String playerName,  String input) {
        switch (game.getGameState()) {
            case START_GAME: {
                StartGame(playerName, input);
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

    //Adds player to the game and starts if the first added player types START or the player are 4
    public synchronized void StartGame(String playerName, String input) {

        if (game.getNumberOfPlayers() >= 2 && playerName.equals(game.getPlayerName(0))) {
            game.StartGame();
        }

        //Adds a player whatever they type, input is not important
        else if (game.getNumberOfPlayers() < 4){
            game.AddPlayer(playerName);
        }
        else if (game.getNumberOfPlayers() == 4){
            game.StartGame();
        }
    }

    public synchronized void ShipsCreation(String playerName, String input) {
        String[] words = input.split(" ");
        switch (words[0].toLowerCase()) {

            //draws either from stack or from turned depending on words[1], if turned words[3] is the index of turnedTiles
            case "draw": DrawTile(playerName, words); break;

            //prints the turned tiles ArrayList
            case "turned": game.printTurnedTiles(); break;

            //player refuses currently drawnTile
            case "refuse": RefuseTile(playerName); break;

            //player books currently drawnTile
            case "book": BookTile(); break;

            //shows deck of 3 cards 1, 2, 3. Can only be called when no card is drawn.
            case "look": LookGameCards(playerName); break;

            //turns the hourglass, if hourglass at last possible turn, the player last input has to be completed
            case "hourglass": TurnHourglass(); break;

            //all other player inputs get refused afterward except for TurnHourglass
            case "completed": Completed(); break;

            //looks at other player ship, if no second word, looks at yours
            case "shipboard": CheckShipBoard(playerName, words); break;

            //if no case is met, ignore
            default: break;
        }
    }

    public synchronized void DrawTile(String playerName, String[] input) {
        if (Objects.equals(playerInputs.get(playerName), "draw") || Objects.equals(playerInputs.get(playerName), "completed")) {
            return;
        }
        switch(input[1].toLowerCase()) {
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
                    int index =  Integer.parseInt(input[2]);
                    Tile drawnTile = game.DrawTurnedTile(playerName, index);
                    if (drawnTile == null) {
                        System.out.println("no turned tiles or the index is  out of bounds");
                        return;
                    }
                    System.out.println("Your drawn tile is:" + drawnTile.toString());
                    playerInputs.put(playerName, "draw turned" + input[2]);
                }
                catch (NumberFormatException e) {
                    System.out.println("Didn't input a numerical index for the turned tiles to pick");
                }
            }
        }
    }

    public synchronized void RefuseTile(String playerName) {
        if (Objects.equals(playerInputs.get(playerName), "draw")) {
            game.RefuseTile(playerName);
        }
    }


    public synchronized void LookGameCards(String playerName) {

    }
}
