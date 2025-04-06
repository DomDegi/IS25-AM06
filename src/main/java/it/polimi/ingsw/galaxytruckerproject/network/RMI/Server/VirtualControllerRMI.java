package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class VirtualControllerRMI extends UnicastRemoteObject implements VirtualController{

    final MultiGameController multiController;
    final List<ViewInterface> clients = new ArrayList<>();


    protected VirtualControllerRMI(MultiGameController multiController) throws RemoteException {
        super();
        this.multiController = multiController;
    }
    public void connect(ViewInterface client) throws RemoteException {
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }
    public void reset () throws RemoteException {
        System.err.println("RMI server reset ");
        synchronized (this.clients) {

        }
    }
}
