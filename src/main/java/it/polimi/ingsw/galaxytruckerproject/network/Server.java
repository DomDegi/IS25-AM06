package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public interface Server {
    String SERVER_NAME = "GalaxyTruckerServer";

    public static String SERVER_NAME(){
        return "GalaxyTruckerServer";
    }

}
