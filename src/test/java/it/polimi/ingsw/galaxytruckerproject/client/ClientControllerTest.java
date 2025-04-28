package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {
    MultiGameController multiGameController = new MultiGameController();
    VirtualController virtualController=new VirtualControllerRMI(multiGameController);
    ClientController controller=new ClientController(virtualController);

    ClientControllerTest() throws RemoteException {
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void CHOOSE_UI_test() throws RemoteException {
        String input="tui";
        controller.input(input);
        String outputCheck ="VirtualViewRMI[UnicastServerRef [liveRef: [endpoint:[10.169.235.115:63623](local),objID:[3c30e5fc:1967c764b10:-7ff6, 2391577648214030755]]]]";
        //assertTrue(controller.getView().toString().regionMatches(0, outputCheck,0, outputCheck.length()));
        assertEquals(TUI.class, controller.getView().getDisplayedView().getClass());
    }

    @Test
    void CHOOSE_CONNECTION_test() throws RemoteException {
        CHOOSE_UI_test();
        String input="rmi";
        controller.input(input);
        String outputCheck ="it.polimi.ingsw.galaxytruckerproject.view.TUI";
    }

    @Test
    void LOGIN_test() throws RemoteException {
        CHOOSE_CONNECTION_test();
        String input="pippi";
        controller.input(input);
        input = "redo";
        controller.input(input);
        input = "pippo";
        controller.input(input);
        input = "done";
        controller.input(input);
        String outputCheck ="pippo";
        assertEquals(outputCheck,controller.getName());
    }
}