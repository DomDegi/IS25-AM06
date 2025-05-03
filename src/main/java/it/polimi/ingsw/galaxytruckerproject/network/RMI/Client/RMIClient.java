package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.network.Client;

import java.util.Scanner;

public class RMIClient implements Client {

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientController clientController = null;
        clientController = new ClientController();
        while(true) {
            String in = scanner.nextLine();
            clientController.input(in);
        }

    }
    
}

