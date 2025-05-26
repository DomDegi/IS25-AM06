package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementation of the {@link ControllerFactory} interface using RMI.
 * This factory is responsible for creating and exporting {@link VirtualController}
 * instances that clients can use to interact with the game remotely.
 *
 * <p>
 * Internally, it delegates controller creation to a shared {@link MultiGameController}
 * instance which manages multiple concurrent game sessions.
 * </p>
 */
public class ControllerFactoryImpl extends UnicastRemoteObject implements ControllerFactory, Remote {

    /**
     * Shared controller responsible for managing multiple game instances.
     */
    private MultiGameController multiGameController;

    /**
     * Constructs a new {@code ControllerFactoryImpl} with a default {@link MultiGameController}.
     *
     * @throws RemoteException if the export fails
     */
    public ControllerFactoryImpl() throws RemoteException {
        super();
        multiGameController = new MultiGameController();
    }

    /**
     * Constructs a new {@code ControllerFactoryImpl} using the provided {@link MultiGameController}.
     *
     * @param multiGameController the shared controller used for game management
     * @throws RemoteException if the export fails
     */
    public ControllerFactoryImpl(MultiGameController multiGameController) throws RemoteException {
        super();
        this.multiGameController = multiGameController;
    }

    /**
     * Creates a new {@link VirtualController} instance by instantiating a new {@link Controller}
     * and wrapping it inside a {@link VirtualControllerRMI} for remote use.
     *
     * @return a remotely accessible {@link VirtualController}
     * @throws RemoteException if the remote export or controller creation fails
     */
    @Override
    public VirtualController createController() throws RemoteException {
        VirtualController virtualController = new VirtualControllerRMI(new Controller(multiGameController));
        return virtualController;
    }
}

