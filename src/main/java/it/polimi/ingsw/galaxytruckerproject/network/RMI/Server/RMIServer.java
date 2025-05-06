package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactory;
import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactoryImpl;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.Server;

import java.io.ObjectInputFilter;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer implements Server {

    public static final String SERVER_NAME = "GalaxyTruckerServer";

    public RMIServer() {
    }

    public void connect(MultiGameController multiGameController) {
        ObjectInputFilter.Config.setSerialFilter(info -> ObjectInputFilter.Status.ALLOWED);

        try {
            ControllerFactory controllerFactory = new ControllerFactoryImpl(multiGameController);
            // Avvio il registry (opzionale se già avviato esternamente)
            LocateRegistry.createRegistry(1099); // porta standard RMI
            // Registro l'oggetto con un nome
            Naming.rebind("rmi://localhost/ControllerFactory", controllerFactory);
            System.out.println("RMIServer pronto!");
            multiGameController.playPingPong();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

