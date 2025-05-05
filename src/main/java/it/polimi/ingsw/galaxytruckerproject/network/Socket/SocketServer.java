package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.Server;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketServer implements Server {

    public void connect(MultiGameController multiGameController){
        ServerSocket server;
        try{
            server = new ServerSocket(12345);
        } catch (IOException e) {
            System.out.println("Cannot open server");
            System.exit(1);
            return;
        }

        Thread socketThread = new Thread(() -> {
            while(true){
                try {
                    Socket client = server.accept();
                    ClientHandler clientHandler = new ClientHandler(client);
                    Thread clientHandlerThread = new Thread(clientHandler);
                    clientHandlerThread.start();
                    VirtualViewSocket virtualView = new VirtualViewSocket(clientHandler);
                    Controller controller = new Controller(multiGameController);
                    virtualView.setController(controller);
                    controller.setView(virtualView);
                    while(!clientHandler.isReadyToGo()) {
                        try{
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            System.out.println("waiting for handler setup");
                        }
                    }
                    clientHandler.setController(controller);
                    clientHandler.setVirtualView(virtualView);
                    virtualView.setClientState(ClientState.LOGIN);
                } catch (IOException e) {
                    System.out.println("Connection crashed");
                    throw new RuntimeException(e);
                }
            }
        }, "SocketServer thread");
        socketThread.start();
    }
}
