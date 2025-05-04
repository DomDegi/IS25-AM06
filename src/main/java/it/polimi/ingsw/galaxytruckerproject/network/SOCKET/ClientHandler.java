package it.polimi.ingsw.galaxytruckerproject.network.SOCKET;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Client.VirtualViewRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    //socket of the client
    private final Socket clientSocket;

    //VirtualView (non se va messa proprio la virtualViewSocket nel caso)
    private VirtualView owner;

    //output stream to the client
    private ObjectOutputStream output;

    //input stream from the client
    private ObjectInputStream input;

    //controller interface
    private ControllerInterface controller;

    //set to true only when run ends
    private boolean readyToGo = false;

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
        while(this.owner == null && this.controller==null){
            try{
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
