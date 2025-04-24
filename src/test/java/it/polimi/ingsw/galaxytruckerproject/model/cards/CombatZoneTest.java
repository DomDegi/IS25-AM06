package it.polimi.ingsw.galaxytruckerproject.model.cards;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.*;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.FlightDaysPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneTest {
    Game game;
    Player player1;
    Player player2;
    Player player3;
    ShipBoard shipBoard1;
    ShipBoard shipBoard2;
    ShipBoard shipBoard3;
    FlightBoard flightBoard;
    CombatZone combatZone;
    ArrayList<Projectile> listOfMeteor;


    @BeforeEach
    void setUp() {
        int level = 2;


        game = new Game(GameMode.LEVEL2,3);
        player1 = new Player("player 1", PlayersColor.BLUE);
        player2 = new Player("player 2", PlayersColor.RED);
        player3 = new Player("player 3", PlayersColor.YELLOW);

        //shipboard 5 to player1
        shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        tile2.setCrewType(CrewType.HUMAN);
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
        shipBoard1.verifyCorrectness();

        //shipboard4 to player2
        shipBoard2 = new ShipBoard(player2);
        player2.setPlayerShip(shipBoard2);
        shipBoard2.initializeLevel2();
        Tile tile23=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        shipBoard2.positionTile(Optional.of(tile23), new Coordinates(1,3));
        Tile tile24=new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0);
        shipBoard2.positionTile(Optional.of(tile24), new Coordinates(2,2));
        Tile tile25=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),0);
        shipBoard2.positionTile(Optional.of(tile25), new Coordinates(2,4));
        tile25.setCrewType(CrewType.HUMAN);
        Tile tile27=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0,3);
        shipBoard2.positionTile(Optional.of(tile27), new Coordinates(3,2));
        Tile tile28=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0,2);
        shipBoard2.positionTile(Optional.of(tile28), new Coordinates(3,3));
        Tile tile29=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard2.positionTile(Optional.of(tile29), new Coordinates(3,4));
        shipBoard2.verifyCorrectness();

        //shipboard1 to player3
        shipBoard3 = new ShipBoard(player3);
        player3.setPlayerShip(shipBoard3);
        shipBoard3.initializeLevel2();
        Tile tile30=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard3.positionTile(Optional.of(tile30), new Coordinates(1,3));
        Tile tile31=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard3.positionTile(Optional.of(tile31), new Coordinates(1,4));
        Tile tile32=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),0);
        tile32.rotate();
        shipBoard3.positionTile(Optional.of(tile32), new Coordinates(2,5));
        Tile tile33=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard3.positionTile(Optional.of(tile33), new Coordinates(2,4));
        Tile tile34=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        tile34.rotate();
        tile34.rotate();
        tile34.rotate();
        shipBoard3.positionTile(Optional.of(tile34), new Coordinates(2,2));
        Tile tile35=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0);
        shipBoard3.positionTile(Optional.of(tile35), new Coordinates(3,3));
        shipBoard3.verifyCorrectness();

        flightBoard = game.getFlightBoard();
        flightBoard.addPlayerToGame(player1);
        flightBoard.addPlayerToGame(player2);
        flightBoard.addPlayerToGame(player3);
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);

        listOfMeteor = new ArrayList<>(Arrays.asList(
                new SmallMeteor(Direction.SOUTH),
                new SmallMeteor(Direction.SOUTH)
        ));

        LinkedHashMap<ChallengeType, Penalty> listOfChallenges = new LinkedHashMap<>();
        listOfChallenges.put(ChallengeType.MINIMUM_CREW_NUMBER, new FlightDaysPenalty(3));
        listOfChallenges.put(ChallengeType.MINIMUM_ENGINE_POWER, new FlightDaysPenalty(3));
        listOfChallenges.put(ChallengeType.MINIMUM_CANNON_STRENGTH, new CrewPenalty(1));

        combatZone = new CombatZone(2,listOfChallenges);
    }

    @Test
    void testCombatZone(){
        int player1_initialDays = player1.getPlayerPosition();
        game.setDrawnCard(combatZone);
        game.getDrawnCard().initializeCard(game, );
        game.setGameState(GameState.CARD_EVENT);
        assertEquals(combatZone, game.getDrawnCard());
        assertEquals(player1,game.getListOfInFlightPlayers().get(combatZone.getPlayerIndex()));
        combatZone.executeCard(game, , player1.getPlayerName());
        combatZone.executeCard(game, , player2.getPlayerName());
        combatZone.executeCard(game, , player3.getPlayerName());
        combatZone.executeCard(game, , player1.getPlayerName());
        combatZone.executeCard(game, , player2.getPlayerName());
        combatZone.executeCard(game, , player3.getPlayerName());

        //problema con la gestione dell' indice

    }
}