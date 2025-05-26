package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Defense;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.LargeMeteor;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.SmallCannonShot;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
        import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    private Player player1;
    private ShipBoard shipBoard1;
    private Player player2;
    private ShipBoard shipBoard2;
    private Game gameLvl2 = new Game(GameMode.LEVEL2, 4);
    private Game gameTrial = new Game(GameMode.TRIAL, 4);

    @BeforeEach
    void setUp() {

        //setupShipBoard made of only single cannons
        player1 = new Player("dummy", PlayersColor.RED);
        shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,4));
        Tile tile3=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile3.rotate();
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,5));
        Tile tile4=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,4));
        Tile tile5=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        tile5.rotate();
        tile5.rotate();
        tile5.rotate();
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(2,2));
        Tile tile6=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,3));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting


        player2 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard2 = new ShipBoard(player2);
        shipBoard2.initializeLevel2();
        Tile tile7=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile7), new Coordinates(0,4));
        Tile tile8=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile8), new Coordinates(1,1));
        tile8.setCrewType(CrewType.HUMAN);
        Tile tile9=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),CrewType.BROWN);
        shipBoard2.positionTile(Optional.of(tile9), new Coordinates(1,2));
        Tile tile10=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile10), new Coordinates(1,3));
        Tile tile11=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile11), new Coordinates(1,4));
        Tile tile12=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard2.positionTile(Optional.of(tile12), new Coordinates(1,5));

        Tile tile13=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile13), new Coordinates(2,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile14), new Coordinates(2,1));
        tile14.setCrewType(CrewType.HUMAN);
        Tile tile15=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile15), new Coordinates(2,2));
        tile15.setCrewType(CrewType.HUMAN);
        Tile tile16=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard2.positionTile(Optional.of(tile16), new Coordinates(2,4));
        Tile tile17=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard2.positionTile(Optional.of(tile17), new Coordinates(2,5));
        tile17.setCrewType(CrewType.HUMAN);
        Tile tile18=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard2.positionTile(Optional.of(tile18), new Coordinates(2,6));

        Tile tile19=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),2);
        shipBoard2.positionTile(Optional.of(tile19), new Coordinates(3,0));
        Tile tile20=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile20), new Coordinates(3,2));
        tile20.setCrewType(CrewType.HUMAN);
        Tile tile21=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile21), new Coordinates(3,3));
        Tile tile22=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE));
        shipBoard2.positionTile(Optional.of(tile22), new Coordinates(3,4));
        Tile tile23=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard2.positionTile(Optional.of(tile23), new Coordinates(3,5));
        Tile tile24=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),2);
        shipBoard2.positionTile(Optional.of(tile24), new Coordinates(3,6));

        Tile tile25=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),2);
        shipBoard2.positionTile(Optional.of(tile25), new Coordinates(4,0));
        Tile tile26=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile26), new Coordinates(4,1));
        Tile tile27=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile27), new Coordinates(4,2));
        Tile tile28=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile28.rotate();
        tile28.rotate();
        shipBoard2.positionTile(Optional.of(tile28), new Coordinates(4,4));
        shipBoard2.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting
        assertTrue(result);
    }

    @Test
    void testShipoboard1SmallMeteorN(){
        SmallMeteor met= new SmallMeteor(Direction.NORTH);
        assertTrue(met.throwProjectile(player1,5,gameLvl2)== Defense.PROTECTED);
        System.out.println(shipBoard1.toString());
    }

    @Test
    void testShipoboard1SmallMeteorS(){
        SmallMeteor met= new SmallMeteor(Direction.SOUTH);
        assertTrue(met.throwProjectile(player1,8,gameLvl2)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard1SmallMeteorE(){
        SmallMeteor met= new SmallMeteor(Direction.EAST);
        assertTrue(met.throwProjectile(player1,8,gameLvl2)==Defense.PROTECTED);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard1SmallMeteorW(){
        SmallMeteor met= new SmallMeteor(Direction.WEST);
        assertTrue(met.throwProjectile(player1,8,gameLvl2)==Defense.PROTECTED);
        System.out.println(met.getCoordinatesToDestroy());
    }

    @Test
    void testShipoboard2SmallMeteorN(){
        SmallMeteor met= new SmallMeteor(Direction.NORTH);
        assertTrue(met.throwProjectile(player2,7,gameLvl2)== Defense.CHOOSETOUSEBATTERY);
        System.out.println(shipBoard1.toString());
    }

    @Test
    void testShipoboard2SmallMeteorS(){
        SmallMeteor met= new SmallMeteor(Direction.SOUTH);
        assertTrue(met.throwProjectile(player2,8,gameLvl2)==Defense.PROTECTED);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard2SmallMeteorE(){
        SmallMeteor met= new SmallMeteor(Direction.EAST);
        assertTrue(met.throwProjectile(player2,9,gameLvl2)==Defense.CHOOSETOUSEBATTERY);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard2SmallMeteorW(){
        SmallMeteor met= new SmallMeteor(Direction.WEST);
        assertTrue(met.throwProjectile(player2,8,gameLvl2)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }

    @Test
    void testShipoboard1LargeMeteorN(){
        LargeMeteor met= new LargeMeteor(Direction.NORTH);
        assertTrue(met.throwProjectile(player1,7,gameTrial)== Defense.PROTECTED);
        System.out.println(shipBoard1.toString());
    }

    @Test
    void testShipoboard1LargeMeteorS(){
        LargeMeteor met= new LargeMeteor(Direction.SOUTH);
        assertTrue(met.throwProjectile(player1,7,gameLvl2)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard1LargeMeteorE(){
        LargeMeteor met= new LargeMeteor(Direction.EAST);
        assertTrue(met.throwProjectile(player1,8,gameTrial)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard1LargeMeteorW(){
        LargeMeteor met= new LargeMeteor(Direction.WEST);
        assertTrue(met.throwProjectile(player1,8,gameTrial)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }

    @Test
    void testShipoboard2LargeMeteorN(){
        LargeMeteor met= new LargeMeteor(Direction.NORTH);
        assertTrue(met.throwProjectile(player2,5,gameLvl2)== Defense.PROTECTED);
        System.out.println(shipBoard1.toString());
    }

    @Test
    void testShipoboard2LargeMeteorS(){
        LargeMeteor met= new LargeMeteor(Direction.SOUTH);
        assertTrue(met.throwProjectile(player2,8,gameLvl2)==Defense.PROTECTED);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard2LargeMeteorE(){
        LargeMeteor met= new LargeMeteor(Direction.EAST);
        assertTrue(met.throwProjectile(player2,8,gameLvl2)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard2LargeMeteorW(){
        LargeMeteor met= new LargeMeteor(Direction.WEST);
        assertTrue(met.throwProjectile(player2,8,gameLvl2)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }

    @Test
    void testShipoboard1LargeCannonShotN(){
        LargeCannonShot met= new LargeCannonShot(Direction.NORTH);
        assertTrue(met.throwProjectile(player1,7,gameLvl2)== Defense.HIT);
        System.out.println(shipBoard1.toString());
    }

    @Test
    void testShipoboard1LargeCannonShotS(){
        LargeCannonShot met= new LargeCannonShot(Direction.SOUTH);
        assertTrue(met.throwProjectile(player1,8,gameLvl2)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard1LargeCannonShotE(){
        LargeCannonShot met= new LargeCannonShot(Direction.EAST);
        assertTrue(met.throwProjectile(player1,9,gameLvl2)==Defense.PROTECTED);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard1LargeCannonShotW(){
        LargeCannonShot met= new LargeCannonShot(Direction.WEST);
        assertTrue(met.throwProjectile(player1,8,gameLvl2)==Defense.HIT);
        System.out.println(met.getCoordinatesToDestroy());
    }
    @Test
    void testShipoboard1SmallCannonShot(){
        SmallCannonShot met= new SmallCannonShot(Direction.NORTH);
        assertTrue(met.throwProjectile(player1,7,gameLvl2)== Defense.HIT);
        System.out.println(shipBoard1.toString());
        SmallCannonShot met1= new SmallCannonShot(Direction.SOUTH);
        assertTrue(met1.throwProjectile(player1,8,gameLvl2)==Defense.HIT);
        System.out.println(met1.getCoordinatesToDestroy());
        SmallCannonShot met2= new SmallCannonShot(Direction.EAST);
        assertTrue(met2.throwProjectile(player1,9,gameLvl2)==Defense.PROTECTED);
        System.out.println(met2.getCoordinatesToDestroy());
        SmallCannonShot met3= new SmallCannonShot(Direction.WEST);
        assertTrue(met3.throwProjectile(player1,8,gameLvl2)==Defense.HIT);
        System.out.println(met3.getCoordinatesToDestroy());
    }

    @Test
    void testShipoboard2allCannonShot(){
        SmallCannonShot met= new SmallCannonShot(Direction.NORTH);
        assertTrue(met.throwProjectile(player2,7,gameLvl2)== Defense.CHOOSETOUSEBATTERY);
        System.out.println(met.getCoordinatesToDestroy());
        SmallCannonShot met1= new SmallCannonShot(Direction.SOUTH);
        assertTrue(met1.throwProjectile(player2,8,gameLvl2)==Defense.HIT);
        System.out.println(met1.getCoordinatesToDestroy());
        SmallCannonShot met2= new SmallCannonShot(Direction.EAST);
        assertTrue(met2.throwProjectile(player2,9,gameLvl2)==Defense.CHOOSETOUSEBATTERY);
        System.out.println(met2.getCoordinatesToDestroy());
        SmallCannonShot met3= new SmallCannonShot(Direction.WEST);
        assertTrue(met3.throwProjectile(player2,8,gameLvl2)==Defense.HIT);
        System.out.println(met3.getCoordinatesToDestroy());
    }


}