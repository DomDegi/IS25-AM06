package it.polimi.ingsw.galaxytruckerproject.network.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HolaServiceImpl extends UnicastRemoteObject implements HolaService {

    public HolaServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayComoEstas() throws RemoteException {
        return "Ciao dal server RMI!";
    }
}
