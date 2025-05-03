package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;
import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.network.Client;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class RMIClient implements Client {

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientController clientController = new ClientController();
        while(true) {
            String in = scanner.nextLine();
            try {
                clientController.input(in);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

