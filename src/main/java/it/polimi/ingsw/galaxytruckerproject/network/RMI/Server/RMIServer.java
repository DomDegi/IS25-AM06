package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactory;
import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactoryImpl;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.Server;

import java.io.ObjectInputFilter;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * This class sets up and runs an RMI server for the Galaxy Trucker game.
 * It binds the controller factory to the RMI registry so clients can connect to the server.
 */
public class RMIServer implements Server {

    /** The name of the server, used for binding the server object in the RMI registry */
    public static final String SERVER_NAME = "GalaxyTruckerServer";

    /**
     * Default constructor for the RMIServer.
     */
    public RMIServer() {
    }

    /**
     * Establishes the RMI connection and binds the controller factory to the RMI registry.
     *
     * This method sets up an RMI registry on a specific port (default 1099), creates a
     * `ControllerFactory` object, and binds it to the RMI registry so that clients can
     * access the server functionality.
     *
     * It also starts the ping-pong mechanism to keep the connection alive.
     *
     * @param multiGameController The controller that manages multiple game sessions.
     */
    public void connect(MultiGameController multiGameController) {
        // Set up the filter for object deserialization
        ObjectInputFilter.Config.setSerialFilter(info -> ObjectInputFilter.Status.ALLOWED);

        try {
            // Create the controller factory for RMI
            ControllerFactory controllerFactory = new ControllerFactoryImpl(multiGameController);

            // Start the RMI registry on a specified port (1099 by default)
            int port = 1099;
            String ip = InetAddress.getLocalHost().getHostAddress();
            LocateRegistry.createRegistry(port); // Creates the RMI registry on the default port

            // Bind the controller factory to the RMI registry
            String url = String.format("rmi://%s:%d/ControllerFactory", ip, port);
            Naming.rebind(url, controllerFactory);
            System.out.println("RMI server is on at " + ip + ":" + port);

            // Start the ping-pong mechanism
            multiGameController.playPingPong();
        } catch (Exception e) {
            // Handle any exceptions that occur while setting up the RMI server
            e.printStackTrace();
        }
    }
}
