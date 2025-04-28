package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;
import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.HolaService;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;

public class RMIClient {
    public static void main(String[] args) {
        try {
            // Prendo l'oggetto remoto
            VirtualController controller = (VirtualController) Naming.lookup("rmi://localhost/VirtualController");

            ClientController clientController = new ClientController(controller);
            DisplayableView displayableView = new TUI();
            VirtualView virtualView = new VirtualViewRMI(clientController,displayableView);
            controller.setView(virtualView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}