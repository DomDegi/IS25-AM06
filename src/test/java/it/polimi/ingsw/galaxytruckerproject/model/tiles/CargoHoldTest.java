package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoHoldTest {

    @Test
    void testAddingGoodsToCargoRedAndBlue(){
        CargoHold red= new CargoRed(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        CargoHold blue= new CargoBlue(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        Goods good1 = new Goods(GoodsColor.RED);
        Goods good2 = new Goods(GoodsColor.BLUE);
        Goods good3 = new Goods(GoodsColor.GREEN);
        Goods good4 = new Goods(GoodsColor.RED);
        Goods good5 = new Goods(GoodsColor.YELLOW);
        Goods good6 = new Goods(GoodsColor.GREEN);
        assertEquals(blue.addGood(good1), -1);
        assertEquals(red.addGood(good1), 1);
        assertEquals(red.addGood(good2), 1);
        assertEquals(red.addGood(good3), 0);
        red.removeGood(good1);
        assertEquals(red.addGood(good1), 1);

    }

}