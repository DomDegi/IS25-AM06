package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.HolaService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HolaClient {

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int port = 1099;

            // Ottieni il registry dall'host e dalla porta
            Registry registry = LocateRegistry.getRegistry(host, port);

            // Cerca l'oggetto remoto con il nome registrato
            HolaService stub = (HolaService) registry.lookup("HelloService");

            // Invoca il metodo remoto
            String response = stub.sayComoEstas();
            System.out.println("Risposta dal server: " + response);
        } catch (Exception e) {
            System.err.println("Errore client: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
