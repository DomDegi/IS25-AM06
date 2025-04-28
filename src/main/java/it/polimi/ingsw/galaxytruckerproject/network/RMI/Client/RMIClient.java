package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.HolaService;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;

public class RMIClient {
    public static void main(String[] args) {
        try {
            // Prendo l'oggetto remoto
            VirtualController controller = (VirtualController) Naming.lookup("rmi://localhost/VirtualController");

            System.out.println("connesso");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}