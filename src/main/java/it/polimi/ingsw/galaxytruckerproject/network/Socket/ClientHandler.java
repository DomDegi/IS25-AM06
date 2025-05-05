package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class ClientHandler implements Runnable {


    //socket of the client
    private final Socket clientSocket;

    //VirtualView (non se va messa proprio la virtualViewSocket nel caso)
    private VirtualView virtualView;

    //output stream to the client
    private ObjectOutputStream output;

    //input stream from the client
    private ObjectInputStream input;

    //controller interface
    private ControllerInterface controller;

    //set to true only when run ends
    private boolean readyToGo = false;

    //indicates if the ClientHandler is listening
    private volatile boolean listening = true;

    //per quella che è la nostra logica di gioco non dovrebbe servire
    private final Queue<ClientMessage> receivedMessages = new PriorityQueue<>(Comparator.comparingInt(ClientMessage::getIndex));


    //Initializes a new handler using a specific socket (connected to the client)
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        //Non mi è ancora tutto chiarissimo ma mettere la new input stream qui
        //implicherebbe che in caso di problemi non riuscimo manco ad inizializzare
        // il client handler. Ha più senso metterlo in RUN
        /*try {
            this.input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
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
        Thread threadInListening = new Thread(this,"Thread in Listening");
        threadInListening.start();
    }

    public void sendMessageToServer(ClientMessage message){};

    public void listenForMessages() {
        System.out.println("Listening for messages received by " + clientSocket.getInetAddress());
        int expectedIndex = 0;
        while(listening) {
            ClientMessage message;
            try {
                message = (ClientMessage) input.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                //Il messaggio arriva troppo presto e quindi deve essere messo in coda
                if ( message.getIndex() > expectedIndex) {
                    receivedMessages.add(message);
                }else if(message.getIndex() < expectedIndex){
                    //wrong input
                }
            }



        }

    }

    public void listenAndProcess(){
        System.out.println("Listening for messages from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

        while (listening) {
            try {
                ClientMessage message = (ClientMessage) input.readObject();
                message.processMessage(this); // o passa la VirtualView se serve
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            } catch (Exception e) {
                System.err.println("Error while processing message:");
                e.printStackTrace();
            }
        }
    }



    public void processMessage(ClientMessage message){
        message.processMessage(this);
    }





















    public void setVirtualView(){
        this.virtualView = virtualView;
    }

    public VirtualView getVirtualView(){
        return this.virtualView;
    }

    public void setController(ControllerInterface controller){
        this.controller = controller;
    }

    public ControllerInterface getController(){
        return this.controller;
    }


    public boolean isReadyToGo() {
        return readyToGo;
    }


}
