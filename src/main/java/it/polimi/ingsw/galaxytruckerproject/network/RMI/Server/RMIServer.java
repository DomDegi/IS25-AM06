package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;
import java.rmi.registry.LocateRegistry;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import java.rmi.Naming;
import java.util.Map;

public class RMIServer {

    public static void main(String[] args) {
        try {
            MultiGameController multiGameController= new MultiGameController();
            VirtualControllerRMI virtualController = new VirtualControllerRMI(multiGameController);
            // Avvio il registry (opzionale se già avviato esternamente)
            LocateRegistry.createRegistry(1099); // porta standard RMI
            // Registro l'oggetto con un nome
            Naming.rebind("rmi://localhost/VirtualController", virtualController);
            System.out.println("Server pronto!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

