package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.model.cards.MeteorSwarm;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.LargeMeteor;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Direction;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;

class MeteorSwarmTest {

    @BeforeEach
    void setUp() {
        Player player = new Player("dummy", PlayersColor.RED);
        ShipBoard shipBoard = new ShipBoard(player);
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
}