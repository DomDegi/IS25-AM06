package it.polimi.ingsw.galaxytruckerproject.network.RMI;

import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualController extends Remote, Serializable {
    void connect(ViewInterface client) throws RemoteException;
}
