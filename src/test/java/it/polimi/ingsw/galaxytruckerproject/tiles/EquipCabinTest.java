package it.polimi.ingsw.galaxytruckerproject.tiles;

import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EquipCabinTest {


    @Test
    void EquipCabinTest() {
        Player player1 = new Player("fedeBulfariGalattico", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH), CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,4));
        Tile tile3=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile3.rotate();
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,5));
        Tile tile4=new AlienLifeSupportsSystem( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL), CrewType.PURPLE);
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,4));
        Tile tile5=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        tile5.rotate();
        tile5.rotate();
        tile5.rotate();
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(2,2));
        Tile tile6=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,3));

        tile1.getStat();
        tile3.getStat();
        tile4.getStat();
        tile5.getStat();
        tile6.getStat();

        tile2.checkAlienability();
        tile2.setCrewType(CrewType.BROWN);
        tile2.getStat();



        tile1.destroy();
        boolean result = tile2.removeCrew();
        assertFalse(result);





    }
}