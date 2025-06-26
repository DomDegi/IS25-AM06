package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class ControllerFactoryImplTest {

    private MultiGameController mockMultiGameController;
    private ControllerFactoryImpl factory;

    @BeforeEach
    void setUp() throws RemoteException {
        mockMultiGameController = new MultiGameController();
        factory = new ControllerFactoryImpl(mockMultiGameController);
    }

    @Test
    void controllerFactoryImplTest_constructor2() throws RemoteException {
        ControllerFactoryImpl controllerFactory = new ControllerFactoryImpl();
    }

    @Test
    void testCreateControllerReturnsNonNull() throws RemoteException {
        VirtualController controller = factory.createController();
        assertNotNull(controller, "The VirtualController should not be null");
    }

    @Test
    void testCreateControllerReturnsCorrectType() throws RemoteException {
        VirtualController controller = factory.createController();
        assertTrue(controller instanceof it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI,
                "The controller should be an instance of VirtualControllerRMI");
    }
}
