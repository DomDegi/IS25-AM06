package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote factory interface used to create instances of {@link VirtualController}.
 * Implemented on the server side and exposed to clients via RMI.
 * Allows clients to obtain their personal controller stubs for remote interaction.
 */
public interface ControllerFactory extends Remote, Serializable {

    /**
     * Creates and returns a new {@link VirtualController} instance for the requesting client.
     *
     * @return a remote reference to a {@link VirtualController}
     * @throws RemoteException if the remote call fails
     */
    VirtualController createController() throws RemoteException;
}
