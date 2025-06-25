package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.ClientMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.ServerMessage;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

/**
 * The ServerHandler class is responsible for handling communication between the client and the server.
 * It manages input and output streams, processes incoming messages from the server, and forwards them to
 * the appropriate components of the client. It also allows for sending messages to the server.
 * <p>
 * The class uses an executor service to process incoming messages concurrently and asynchronously.
 * <p>
 * This class handles the setup of the connection and allows interaction with the server's messages.
 */
public class ServerHandler implements Runnable {

    /**
     * The socket that connects this handler to the server.
     */
    private final Socket server;

    /**
     * Input stream to read messages from the server.
     */
    private ObjectInputStream input;

    /**
     * Flag indicating whether the server handler is active and processing messages.
     */
    public volatile boolean isOn = true;

    /**
     * Output stream to send messages to the server.
     */
    private ObjectOutputStream output;

    /**
     * The view associated with this handler for displaying information.
     */
    private DisplayableView view;

    /**
     * The client controller that manages the logic of the client.
     */
    private ClientController clientController;

    /**
     * The virtual controller for managing the game logic.
     */
    private VirtualController virtualController;

    /**
     * Executor service used for processing incoming messages asynchronously.
     */
    private final ExecutorService messageProcesser = Executors.newSingleThreadExecutor();

    /**
     * Flag indicating whether the setup is complete and the handler is ready.
     */
    public boolean isReady = false;

    /**
     * Constructs a new ServerHandler for handling communication with the given socket.
     *
     * @param server The socket representing the connection to the server.
     * @throws IOException If an error occurs while initializing input and output streams.
     */
    public ServerHandler(Socket server) throws IOException {
        this.server = server;
    }

    /**
     * Sets the view for this handler. The view is used to interact with the user interface.
     *
     * @param view The view to be associated with this handler.
     */
    public void setView(DisplayableView view) {
        this.view = view;
    }

    /**
     * Sets the virtual controller for this handler. The virtual controller manages game logic.
     *
     * @param virtualController The virtual controller to be associated with this handler.
     */
    public void setVirtualController(VirtualController virtualController) {
        this.virtualController = virtualController;
    }

    /**
     * Sets the client controller for this handler. The client controller manages the state of the client.
     *
     * @param clientController The client controller to be associated with this handler.
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Starts the communication by initializing input and output streams, and begins listening
     * for messages from the server.
     */
    @Override
    public void run() {
        ExecutorService connectionExecutor = Executors.newSingleThreadExecutor();
        try {
            CompletableFuture<?> connectionFuture = CompletableFuture.runAsync(() -> {
                try {
                    this.output = new ObjectOutputStream(server.getOutputStream());
                    this.output.flush();
                    this.input = new ObjectInputStream(server.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Input and output streams could not be created");
                    throw new CompletionException(e);
                }
            });
            connectionFuture.get(3, TimeUnit.SECONDS);

            this.waitSetup();
            Thread messageReceiver = new Thread(this::receiveMessages, "message receiver");
            messageReceiver.start();

        } catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println("Connection timed out");
            try {
                server.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println(e1.getMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Server handler could not run");
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param clientMessage The message to be sent to the server.
     */
    public synchronized void sendClientMessage(ClientMessage clientMessage) {
        if (!isOn) {
            System.out.println("Connection is closed. Cannot send message to server.");
            return;
        }
        try {
            output.writeObject(clientMessage);
            output.flush();
        } catch (IOException e) {
            System.out.println("Connection lost while sending message.");
            e.printStackTrace();
            if (isOn) {
                clientController.setOffline();
                stopReceivingMessages();
            }
        }
    }

    /**
     * Receives and processes messages from the server.
     */
    public void receiveMessages() {
        System.out.println("Receiving messages from server");
        while (isOn) {
            try {
                ServerMessage serverMessage = (ServerMessage) input.readObject();
                synchronized (this) {
                    messageProcesser.submit(() -> processMessage(serverMessage));
                }
            } catch (EOFException | SocketException e) {
                System.out.println("Client disconnected: " + server.getInetAddress());
                if(isOn) {
                    clientController.setOffline();
                    stopReceivingMessages();
                }
                break;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling inputStream from server");
                if (isOn) {
                    clientController.setOffline();
                    stopReceivingMessages();
                }
                e.printStackTrace();
                break;
            } catch (Exception e) {
                System.err.println("Error while processing message:");
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes the received server message.
     *
     * @param serverMessage The message to be processed.
     */
    public void processMessage(ServerMessage serverMessage) {
        try {
            serverMessage.processMessage(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not process message from server");
        }
    }

    /**
     * Stops receiving messages from the server and closes the connection.
     */
    public void stopReceivingMessages() {
        if (this.isOn) {
            this.isOn = false;
            try {
                if (!server.isClosed()) {
                    server.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not close server");
            }
        }
    }

    /**
     * Waits for the setup to be complete before continuing with message processing.
     */
    public void waitSetup() {
        this.isReady = true;
        while (this.view == null || this.virtualController == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Waiting for setup");
            }
        }
    }

    /**
     * Returns the view associated with this handler.
     *
     * @return The view associated with this handler.
     */
    public DisplayableView getView() {
        return this.view;
    }

    /**
     * Returns the client controller associated with this handler.
     *
     * @return The client controller associated with this handler.
     */
    public ClientController getClientController() {
        return this.clientController;
    }

    /**
     * Checks if the handler is ready to receive and process messages.
     *
     * @return True if the handler is ready, false otherwise.
     */
    public boolean isReady() {
        return this.isReady;
    }
}
