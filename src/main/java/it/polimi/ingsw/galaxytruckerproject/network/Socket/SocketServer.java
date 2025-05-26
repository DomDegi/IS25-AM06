package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Server {

    public void connect(MultiGameController multiGameController){
        ServerSocket server;
        try{
            int port = 12345;
            server = new ServerSocket(port);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("SocketServer is on at " + ip + ":" + port);
        } catch (IOException e) {
            System.out.println("Cannot open server");
            System.exit(1);
            return;
        }

        Thread socketThread = new Thread(() -> {
            while(true){

                new Thread(() -> {
                    try {
                        Socket client = server.accept();
                        System.out.println("New client connected from " + client.getInetAddress());
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
                    } catch (IOException e) {
                        System.out.println("Connection crashed");
                        throw new RuntimeException(e);
                    }
                    multiGameController.playPingPong();

                },"ClientHandlerSetup").start();

            }
        }, "SocketServer thread");
        socketThread.start();
    }
}
