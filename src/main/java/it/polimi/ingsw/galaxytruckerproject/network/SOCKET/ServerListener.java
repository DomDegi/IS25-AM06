package it.polimi.ingsw.galaxytruckerproject.network.SOCKET;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener implements Runnable {

    private ObjectInputStream input;
    public ServerListener(Socket socket) throws IOException {
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object update = input.readObject();
                //update alla view
            }
        } catch (Exception e) {
            System.out.println("Connessione persa con il server.");
        }
    }
}
