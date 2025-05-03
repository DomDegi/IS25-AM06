package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class ControllerFactoryImpl implements ControllerFactory, Serializable, Remote {
    private MultiGameController multiGameController;
    public ControllerFactoryImpl() {
        multiGameController = new MultiGameController();
    }
    @Override
    public VirtualController createController() throws RemoteException {
        VirtualController virtualController= new VirtualControllerRMI(new Controller(multiGameController));
        return virtualController;
    }

}
