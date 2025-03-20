package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameMode;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.LargeMeteor;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectilePenaltyTest {

    private Player player;
    private ProjectilePenalty penalty;
    private ArrayList<Projectile> listOfMeteors1;
    private ArrayList<Projectile> listOfMeteorsFull;
    private ShipBoard shipBoard;
    private Game gameLvl2 = new Game(GameMode.LEVEL2);
    private Game gameTrial = new Game(GameMode.TRIAL);

    @BeforeEach
    void setUp() {

        //setupShipBoard made of only single cannons
        player = new Player("dummy", PlayersColor.RED);
        shipBoard = new ShipBoard(player);
        shipBoard.initializeLevel2();
        SingleCannon singleCannonN = new SingleCannon(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.UNIVERSAL), new Link(Connectors.SMOOTH));
        SingleCannon singleCannonE = new SingleCannon(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.UNIVERSAL));
        SingleCannon singleCannonS = new SingleCannon(new Link(Connectors.UNIVERSAL), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));
        SingleCannon singleCannonW = new SingleCannon(new Link(Connectors.SMOOTH), new Link(Connectors.UNIVERSAL), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));

        shipBoard.positionTile(Optional.of(singleCannonN), new Coordinates(3, 1));
        shipBoard.positionTile(Optional.of(singleCannonE), new Coordinates(4, 2));
        shipBoard.positionTile(Optional.of(singleCannonS), new Coordinates(3, 3));
        shipBoard.positionTile(Optional.of(singleCannonW), new Coordinates(2, 2));



        listOfMeteors1 = new ArrayList<> (List.of(new LargeMeteor(Direction.NORTH)));

        listOfMeteorsFull = new ArrayList<> (Arrays.asList(
                new LargeMeteor(Direction.NORTH),
                new SmallMeteor(Direction.NORTH),
                new LargeMeteor(Direction.EAST),
                new SmallMeteor(Direction.EAST),
                new LargeMeteor(Direction.SOUTH),
                new SmallMeteor(Direction.SOUTH),
                new LargeMeteor(Direction.WEST),
                new SmallMeteor(Direction.WEST)
        ));
        penalty = new ProjectilePenalty(listOfMeteors1);
    }

    @Test
    void large_meteor_from_north_on_small_cannon() {
        String[] input = {};
        int returnValue = penalty.applyPenalty(gameLvl2, player, input);
        assertEquals(1, returnValue);
    }
}