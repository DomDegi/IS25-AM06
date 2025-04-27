package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.network.MockVirtualView;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CrewPenaltyTest {
    CrewPenalty penalty=new CrewPenalty(3);
    Player player1 = new Player("Andrea", PlayersColor.RED);
    Game game = new Game(GameMode.LEVEL2,4);
    VirtualView mockView1 = new MockVirtualView();

    @BeforeEach
    void setUp() {
        ShipBoard shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0,CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(1,3));
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(1,4));
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0);
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(1,5));
        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(2,0));
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),0, 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile15), new Coordinates(3,3));
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),0);
        shipBoard1.positionTile(Optional.of(tile16), new Coordinates(3,4));
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0);
        shipBoard1.positionTile(Optional.of(tile17), new Coordinates(3,5));
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0, 2);
        shipBoard1.positionTile(Optional.of(tile18), new Coordinates(3,6));
        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0, 2);
        shipBoard1.positionTile(Optional.of(tile19), new Coordinates(4,0));
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile20), new Coordinates(4,1));
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),0);
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));
        shipBoard1.verifyCorrectness();
        game.getFlightBoard().addPlayerToGame(player1);
        game.getFlightBoard().addToFlightBoard(player1,1);
    }
    @Test
    void applyPenalty() {
        player1.setAllCrewToHuman();
        penalty.initializePenalty(game,mockView1,player1);
        ArrayList<Coordinates> toRemove = new ArrayList<>();
        Coordinates coord0 =  new Coordinates(1,1);
        Coordinates coord1 =  new Coordinates(1,2);
        Coordinates coord2 =  new Coordinates(1,1);
        Coordinates coord3 =  new Coordinates(2,2);
        Coordinates coord4 =  new Coordinates(2,3);
        toRemove.add(coord0);
        toRemove.add(coord1);
        toRemove.add(coord2);
        toRemove.add(coord3);
        toRemove.add(coord4);
        penalty.removeCrew(player1, mockView1, toRemove);
        assertEquals(9,player1.getTotalCrew());
        assertTrue (player1.getShipBoard().getTile(coord0).getCrew() == 0 && player1.getShipBoard().getTile(coord1).getCrew() == 1 &&
        player1.getShipBoard().getTile(coord3).getCrew() == 2 && player1.getShipBoard().getTile(coord4).getCrew() == 2);
    }

    @Test
    void applyPenalty_disconnected() {
        player1.setAllCrewToHuman();
        player1.playerDisconnects();
        assertFalse(penalty.initializePenalty(game,mockView1,player1));
        assertEquals(9,player1.getTotalCrew());
    }

}