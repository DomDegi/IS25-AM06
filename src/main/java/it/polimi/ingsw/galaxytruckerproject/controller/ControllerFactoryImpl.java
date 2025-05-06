package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ControllerFactoryImpl extends UnicastRemoteObject implements ControllerFactory, Remote {
    private MultiGameController multiGameController;
    public ControllerFactoryImpl() throws RemoteException {
        super();
        multiGameController = new MultiGameController();
    }

    public ControllerFactoryImpl(MultiGameController multiGameController) throws RemoteException {
        super();
        this.multiGameController = multiGameController;
    }

    @Override
    public VirtualController createController() throws RemoteException {
        VirtualController virtualController= new VirtualControllerRMI(new Controller(multiGameController));
        return virtualController;
    }

}
