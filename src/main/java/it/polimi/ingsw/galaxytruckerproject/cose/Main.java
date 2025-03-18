package it.polimi.ingsw.galaxytruckerproject.cose;

import it.polimi.ingsw.galaxytruckerproject.observers.GameController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Game game = new Game();
        final GameController gameController = new GameController(game);
        while (game.getGameState() != GameState.CONCLUDE_GAME) {
            Scanner playerName = new Scanner(System.in);
            Scanner input = new Scanner(System.in);
            gameController.processPlayerInput(playerName.toString(), input.toString());
        }
    }
}
