package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.RMIServer;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.SocketServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The main entry point for starting the server for the Galaxy Trucker game.
 * This class sets up both an RMI (Remote Method Invocation) server and a Socket server
 * for clients to connect to and interact with the game.
 */
public class ServerMain {

    /**
     * The main method that initializes and starts the server.
     * It sets the local machine's IP address for RMI, then starts both
     * the RMI and Socket servers, connecting them to the MultiGameController.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        String ip = null;
        try (Socket socket = new Socket()) {
            // Connect to an external address (Google) to retrieve the local machine's IP address
            socket.connect(new InetSocketAddress("8.8.8.8", 53),2000); // Google DNS, lightweight
            ip = socket.getLocalAddress().getHostAddress();
            // Set the local IP as the system property for the RMI server
            System.setProperty("java.rmi.server.hostname", ip);
        } catch (IOException e) {
            System.out.println("Could not connect to external server, falling back to localhost.");
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Local IP detected: " + ip);
            } catch (UnknownHostException ex) {
                System.out.println("Could not determine local IP, defaulting to 127.0.0.1");
                ip = "127.0.0.1";
            }
        }

        // Create an instance of the MultiGameController to manage multiple games
        MultiGameController multiGameController = new MultiGameController();

        // Start the Socket server and connect it to the MultiGameController
        SocketServer socketServer = new SocketServer();
        socketServer.connect(ip,multiGameController);

        // Start the RMI server and connect it to the MultiGameController
        RMIServer rmi = new RMIServer();
        rmi.connect(ip,multiGameController);
    }
}
