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


    public static void main(String[] args) {
        /*
        ObjectInputFilter filter = ObjectInputFilter.Config.createFilter(
                "java.rmi.server.*;java.rmi.registry.*;java.lang.*;" +
                        "java.util.*;java.base/*;" +
                "it.polimi.ingsw.galaxytruckerproject.*;java.base/*;!*"
        );
        ObjectInputFilter.Config.setSerialFilter(filter);*/
        ObjectInputFilter.Config.setSerialFilter(info -> ObjectInputFilter.Status.ALLOWED);

        try {
            MultiGameController multiGameController = new MultiGameController();
            ControllerFactory controllerFactory = new ControllerFactoryImpl();
            // Avvio il registry (opzionale se già avviato esternamente)
            LocateRegistry.createRegistry(1099); // porta standard RMI
            // Registro l'oggetto con un nome
            Naming.rebind("rmi://localhost/ControllerFactory", controllerFactory);
            System.out.println("Server pronto!");
            multiGameController.playPingPong();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

