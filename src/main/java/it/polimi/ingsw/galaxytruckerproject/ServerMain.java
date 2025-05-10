package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.RMIServer;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.SocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com",80));
            String ip = socket.getLocalAddress().getHostAddress();
            System.setProperty("java.rmi.server.hostname",ip);
        } catch(IOException e) {
            System.out.println("Error connecting to server via socket");
        }
        MultiGameController multiGameController = new MultiGameController();
        SocketServer socketServer = new SocketServer();
        socketServer.connect(multiGameController);
        RMIServer rmi = new RMIServer();
        rmi.connect(multiGameController);
    }
}
