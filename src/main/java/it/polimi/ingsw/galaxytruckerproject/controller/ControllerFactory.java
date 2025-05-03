package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerFactory extends Remote, Serializable {
    VirtualController createController() throws RemoteException;
}
