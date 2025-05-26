package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactory;
import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactoryImpl;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.Server;

import java.io.ObjectInputFilter;
import java.net.InetAddress;
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
            int port = 1099;
            String ip = InetAddress.getLocalHost().getHostAddress();
            LocateRegistry.createRegistry(port); // porta standard RMI
            // Registro l'oggetto con un nome
            String url = String.format("rmi://%s:%d/ControllerFactory", ip, port);
            Naming.rebind(url, controllerFactory);
            System.out.println("RMIserver is on at " + ip + ":" + port);
            multiGameController.playPingPong();
        } catch (Exception e) {
        }
    }
}

