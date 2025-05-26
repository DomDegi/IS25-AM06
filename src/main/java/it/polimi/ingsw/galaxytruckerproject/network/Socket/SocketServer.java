package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The SocketServer class is responsible for managing a server that listens for incoming socket connections
 * from clients. It creates a new `ClientHandler` for each connected client, sets up the necessary controllers
 * and views, and initiates the communication between the server and clients.
 * <p>
 * The server listens for client connections on a specific port (12345) and handles multiple client connections
 * in separate threads.
 */
public class SocketServer implements Server {

    /**
     * Establishes the server and listens for incoming client connections. For each client that connects,
     * a new `ClientHandler` is created to handle communication with the client. The server also sets up
     * the necessary controllers and virtual views for the game.
     *
     * @param multiGameController The controller that manages the overall game logic and state.
     */
    @Override
    public void connect(MultiGameController multiGameController) {
        ServerSocket server;
        try {
            // The server listens on port 12345
            int port = 12345;
            server = new ServerSocket(port);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("SocketServer is on at " + ip + ":" + port);
        } catch (IOException e) {
            System.out.println("Cannot open server");
            System.exit(1);
            return;
        }

        // Creating a new thread that listens for client connections
        Thread socketThread = new Thread(() -> {
            while (true) {
                // Accept a new client connection
                new Thread(() -> {
                    try {
                        Socket client = server.accept();
                        System.out.println("New client connected from " + client.getInetAddress());

                        // Create a new ClientHandler to manage communication with the client
                        ClientHandler clientHandler = new ClientHandler(client);
                        Thread clientHandlerThread = new Thread(clientHandler);
                        clientHandlerThread.start();

                        // Create a VirtualViewSocket for the client
                        VirtualViewSocket virtualView = new VirtualViewSocket(clientHandler);

                        // Create a new controller for managing the game
                        Controller controller = new Controller(multiGameController);
                        virtualView.setController(controller);
                        controller.setView(virtualView);

                        // Wait until the client handler is ready before proceeding
                        while (!clientHandler.isReadyToGo()) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println("Waiting for handler setup");
                            }
                        }

                        // Set the controller and virtual view for the client handler
                        clientHandler.setController(controller);
                        clientHandler.setVirtualView(virtualView);

                    } catch (IOException e) {
                        System.out.println("Connection crashed");
                        throw new RuntimeException(e);
                    }

                    // Start the PingPong mechanism for managing connections
                    multiGameController.playPingPong();
                }, "ClientHandlerSetup").start();
            }
        }, "SocketServer thread");

        // Start the server listening for connections
        socketThread.start();
    }
}
