package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.LargeMeteor;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckerproject.tiles.Direction;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmTest {

    @BeforeEach
    void setUp() {
        ShipBoard shipBoard = new ShipBoard();
        int level = 2;
        ArrayList listOfMeteors = new ArrayList<> (Arrays.asList(
                new LargeMeteor(Direction.NORTH),
                new SmallMeteor(Direction.NORTH),
                new LargeMeteor(Direction.EAST),
                new SmallMeteor(Direction.EAST),
                new LargeMeteor(Direction.SOUTH),
                new SmallMeteor(Direction.SOUTH),
                new LargeMeteor(Direction.WEST),
                new SmallMeteor(Direction.WEST)
        ));
        MeteorSwarm meteorSwarm = new MeteorSwarm(level, listOfMeteors);
    }

    @Test
    void test_nord
}