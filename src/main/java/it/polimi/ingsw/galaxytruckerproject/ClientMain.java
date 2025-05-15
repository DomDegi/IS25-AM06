package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.network.Client;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class ClientMain implements Client {

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        ClientController clientController;
        clientController = new ClientController();
        System.out.println("You want to use TUI or GUI? [tui] [gui]");
        while(true) {
            String in = scanner.nextLine();
            if(clientController.getState()== ClientState.CHOOSE_UI) {
                clientController.chooseUI(in);
            }else
                clientController.input(in);
        }
    }

}

