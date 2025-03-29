package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.observers.GameController;

import java.util.Scanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Game game = new Game(GameMode.LEVEL2);
        final GameController gameController = new GameController(game);
        Scanner scanner = new Scanner(System.in);

        while (game.getGameState() != GameState.CONCLUDE_GAME) {
            System.out.println(game.getGameState());
            String playerName = scanner.nextLine();

            String input = scanner.nextLine();

            gameController.processPlayerInput(playerName, input);
        }

        scanner.close();
    }
}
