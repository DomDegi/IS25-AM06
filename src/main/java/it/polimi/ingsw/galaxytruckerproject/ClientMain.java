package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.network.Client;


import java.util.Scanner;

/**
 * The {@code ClientMain} class is the entry point for the client-side application
 * in the Galaxy Trucker game. It initiates the client controller and manages user
 * input to select between different user interfaces (TUI or GUI).
 * <p>
 * This class is responsible for setting up the initial state of the client application,
 * allowing the user to choose between a Text User Interface (TUI) or a Graphical User
 * Interface (GUI) for interacting with the game. Based on the user's choice, it initializes
 * the appropriate UI and begins the game.
 * </p>
 */
public class ClientMain implements Client {

    /**
     * The main method that starts the client application. It prompts the user to choose
     * between a Text User Interface (TUI) or a Graphical User Interface (GUI) to interact
        * with the game. Based on the user's choice, it initializes the game client accordingly.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientController clientController;
        clientController = new ClientController();

        // Prompt the user to choose the UI type (TUI or GUI)
        System.out.println("You want to use TUI or GUI? [tui] [gui]");

        while (true) {
            String in = scanner.nextLine();

            // Choose the UI based on the user's input
            if (clientController.getState() == ClientState.CHOOSE_UI) {
                clientController.chooseUI(in);
            } else {
                clientController.input(in);
            }
        }
    }
}
