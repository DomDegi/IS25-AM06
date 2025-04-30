package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.*;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class LightShipBoardTest {



    @Test
    void Test1(){
    }

        LightPlayer light1 = new LightPlayer("Pippo C.", PlayersColor.RED);
        LightShipBoard shipBoard1 = new LightShipBoard(light1);
        shipBoard1.initializeLevel2();


}