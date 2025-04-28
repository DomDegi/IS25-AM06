package it.polimi.ingsw.galaxytruckerproject.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HolaService extends Remote {
    String sayComoEstas() throws RemoteException;
}