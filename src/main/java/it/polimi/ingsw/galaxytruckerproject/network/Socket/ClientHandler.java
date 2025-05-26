package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.ServerMessage;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class represents a handler for managing the communication with a connected client.
 * It listens for messages from the client, processes them, and sends server messages back to the client.
 */
public class ClientHandler implements Runnable {

    /**
     * Represents the socket connection to the client.
     * This socket is used to send and receive data between the server and the client.
     */
    private final Socket clientSocket;

    /**
     * The VirtualView associated with this handler.
     * It is used to interact with the client and update the client on the game's state.
     */
    private VirtualView virtualView;

    /**
     * The output stream to send data to the client.
     * This stream allows serializing objects to be sent to the client.
     */
    private ObjectOutputStream output;

    /**
     * The input stream to receive data from the client.
     * This stream is used to read serialized objects sent by the client.
     */
    private ObjectInputStream input;

    /**
     * The controller that manages the game logic and interacts with the server.
     * This controller is used by the handler to execute game-related operations.
     */
    private ControllerInterface controller;

    /**
     * A flag indicating whether the handler is ready to begin processing client messages.
     * This flag is set to true once the socket connection and other initialization are complete.
     */
    private boolean readyToGo = false;

    /**
     * A flag indicating whether the handler is actively listening for incoming messages from the client.
     * This flag is set to false when the client disconnects or an error occurs.
     */
    private volatile boolean listening = true;


    /**
     * Initializes a new ClientHandler with the given client socket.
     *
     * @param clientSocket The socket connected to the client.
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    /**
     * Starts the client handler by initializing the input and output streams and
     * waiting for the VirtualView and Controller to be set before starting the
     * listening thread.
     */
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            this.output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Could not open connection to " + clientSocket.getInetAddress());
            return;
        }
        readyToGo = true;
        //once the socket-connection is ready, we wait for the VirtualView and the Controller
        while(this.virtualView == null && this.controller==null){
            try{
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread threadInListening = new Thread(this::listenAndProcess, "Thread in Listening");
        threadInListening.start();
    }

    /**
     * Sends a server message to the client.
     *
     * @param message The server message to send to the client.
     */
    public synchronized void sendServerMessageToClient(ServerMessage message){
            try {
                //output.reset();
                output.writeObject(message);
            } catch (IOException e) {
                System.out.println("Could not send message to " + clientSocket.getInetAddress());
            }
    };

    /**
     * Listens for messages from the client and processes them.
     * The method will continue running as long as the client is connected and listening is true.
     */
    public void listenAndProcess(){
        System.out.println("Listening for messages from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

        while (listening) {
            try {
                ClientMessage message = (ClientMessage) input.readObject();
                message.processMessage(this);
            } catch (EOFException | SocketException e) {
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                listening = false;
                break;
            } catch (IOException e) {
                System.out.println("I/O error with client: " + clientSocket.getInetAddress());
                e.printStackTrace();
                listening = false;
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            } catch (Exception e) {
                System.err.println("Error while processing message:");
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the VirtualView associated with this ClientHandler.
     *
     * @param view The VirtualView to associate with this handler.
     */
    public void setVirtualView(VirtualView view) {
        this.virtualView = view;
    }

    /**
     * Returns the VirtualView associated with this ClientHandler.
     *
     * @return The VirtualView associated with this handler.
     */
    public VirtualView getVirtualView() {
        return this.virtualView;
    }

    /**
     * Sets the controller associated with this ClientHandler.
     *
     * @param controller The controller to associate with this handler.
     */
    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    /**
     * Returns the controller associated with this ClientHandler.
     *
     * @return The controller associated with this handler.
     */
    public ControllerInterface getController() {
        return this.controller;
    }

    /**
     * Returns whether the handler is ready to process messages.
     *
     * @return True if the handler is ready to go, false otherwise.
     */
    public boolean isReadyToGo() {
        return readyToGo;
    }
}
