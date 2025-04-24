package it.polimi.ingsw.galaxytruckerproject.model.cards;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AbandonedShipTest {
    private AbandonedShip abandonedShip;
    private Game game;
    private final VirtualView mockView1 = mock(VirtualView.class);
    private final VirtualView mockView2 = mock(VirtualView.class);
    private final VirtualView mockView3 = mock(VirtualView.class);
    private final VirtualView mockView4 = mock(VirtualView.class);
    Map<String,VirtualView> viewMap = new HashMap<>();
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private FlightBoard flightBoard;

    @BeforeEach
    void setUp() {
        game = new Game(GameMode.LEVEL2, 4);
        flightBoard = game.getFlightBoard();
        player2 = new Player("riccardo", PlayersColor.BLUE);
        player3 = new Player("francesco", PlayersColor.YELLOW);
        player4 = new Player("pietro", PlayersColor.GREEN);
        abandonedShip = new AbandonedShip(1, 2, 3, 10);
        player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        viewMap.put(player1.getPlayerName(), mockView1);
        viewMap.put(player2.getPlayerName(), mockView2);
        viewMap.put(player3.getPlayerName(), mockView3);
        viewMap.put(player4.getPlayerName(), mockView4);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));

        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        tile2.setCrewType(CrewType.HUMAN);
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0,CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoRed(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(1,3));
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(1,4));
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0);
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(1,5));

        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(2,0));
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        tile8.setCrewType(CrewType.HUMAN);
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        tile9.setCrewType(CrewType.HUMAN);
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        tile12.setCrewType(CrewType.HUMAN);
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),0, 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        tile14.setCrewType(CrewType.HUMAN);
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

        flightBoard.addPlayerToGame(player1);
        flightBoard.addPlayerToGame(player2);
        flightBoard.addPlayerToGame(player3);
        flightBoard.addPlayerToGame(player4);
        flightBoard.addToFlightBoard(player1, 2);
        flightBoard.addToFlightBoard(player2, 1);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);
    }

    @Test
    void the_only_player_that_can_pass_is_player1_in_position_2_and_accepts_the_trade() {
        player1.setAllCrewToHuman();
        int player1_initialDays = player1.getPlayerPosition();
        game.setDrawnCard(abandonedShip);
        game.getDrawnCard().initializeCard(game, viewMap);
        game.setGameState(GameState.CARD_EVENT);
        assertEquals(abandonedShip, game.getDrawnCard());
        //first player (2) to play doesn't have enough crew so should be skipped automatically whatever his input is
        assertEquals(player2, abandonedShip.getPlayerToPlay());
        assertEquals(player1, abandonedShip.getPlayerToPlay());
        //player 1 is next and says yes
        abandonedShip.choice(player1.getPlayerName(), true);
        //now player 1 has to choose crew to remove
        ArrayList<Coordinates> toRemove = new ArrayList<>();
        toRemove.add(new Coordinates(1,1));
        toRemove.add(new Coordinates(1,1));
        toRemove.add(new Coordinates(2,0));
        abandonedShip.removeCrew(player1.getPlayerName(), toRemove);
        //it's still his turn because he typed one of the coordinates wrong, now has to re input the last one
        toRemove.removeLast();
        toRemove.add(new Coordinates(2,1));
        assertEquals(player1, abandonedShip.getPlayerToPlay());
        abandonedShip.removeCrew(player1.getPlayerName(), toRemove);
        assertEquals(GameState.DRAW_CARD, game.getGameState());
        assertEquals(10, player1.getCredit());
        assertEquals(player1.getPlayerPosition(), player1_initialDays - 2);
    }

}
