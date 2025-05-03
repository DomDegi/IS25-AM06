package it.polimi.ingsw.galaxytruckerproject.network.SOCKET;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ObjectInputStream input;
    //private VirtualView virtualView;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object command = input.readObject();
                //analizza command per inviarlo al controller
            }
        } catch (Exception e) {
            System.out.println("Connessione persa con il client.");
        }
    }
}
