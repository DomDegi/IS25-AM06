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
        assertEquals("RED", good1.getColor().getColorName());
        assertEquals(blue.addGood(good1), -1);
        assertEquals(red.addGood(good1), 1);
        assertEquals(red.addGood(good2), 1);
        assertEquals(red.addGood(good3), 0);
        red.removeGood(good1);
        assertEquals(red.addGood(good1), 1);

    }

    @Test
    void blueToString1Test1(){
        CargoHold blue= new CargoBlue(0, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        String expected = blue.toString1();
        assertEquals(expected,"    0   ");
    }

    @Test
    void blueToString1Test2(){
        CargoHold blue= new CargoBlue(1, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        blue.addGood(new Goods(GoodsColor.BLUE));
        String expected = blue.toString1();
        assertEquals(expected," B  0   ");

    }

    @Test
    void blueToString1Test3(){
        CargoHold blue= new CargoBlue(1, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        String expected = blue.toString1();
        assertEquals(expected," ░  0   ");
    }

    @Test
    void blueToString1Test4(){
        CargoHold blue= new CargoBlue(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        blue.addGood(new Goods(GoodsColor.BLUE));
        String expected = blue.toString1();
        assertEquals(expected," B  0 ░ ");
    }

    @Test
    void blueToString3Test(){
        CargoHold blue= new CargoBlue(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,100);
        String expected = blue.toString3();
        assertEquals(expected," ░ 2 100");
    }

    @Test
    void blueToString3Test2(){
        CargoHold blue= new CargoBlue(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,10);
        String expected = blue.toString3();
        assertEquals(expected," ░ 2 10 ");
    }

    @Test
    void blueToString3Test3(){
        CargoHold blue= new CargoBlue(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,10);
        String expected = blue.toString3();
        assertEquals(expected, "   2 10 ");
    }

    @Test
    void blueToString3Test4(){
        CargoHold blue= new CargoBlue(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,100);
        String expected = blue.toString3();
        assertEquals(expected, "   2 100");

    }

    @Test
    void blueToString3Test5(){
        CargoHold blue= new CargoBlue(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,100);
        blue.addGood(new Goods(GoodsColor.BLUE));
        blue.addGood(new Goods(GoodsColor.YELLOW));
        blue.addGood(new Goods(GoodsColor.GREEN));
        String expected = blue.toString3();
        assertEquals(expected, " G 2 100");
    }

    @Test
    void blueToString3Test6(){
        CargoHold blue= new CargoBlue(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,10);
        blue.addGood(new Goods(GoodsColor.BLUE));
        blue.addGood(new Goods(GoodsColor.YELLOW));
        blue.addGood(new Goods(GoodsColor.GREEN));
        String expected = blue.toString3();
        assertEquals(expected, " G 2 10 ");
    }


    @Test
    void redToString1Test1(){
        CargoHold red= new CargoRed(0, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        String expected = red.toString1();
        assertEquals(expected,"    0   ");
    }

    @Test
    void redToString1Test2(){
        CargoHold red= new CargoRed(1, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        red.addGood(new Goods(GoodsColor.BLUE));
        String expected = red.toString1();
        assertEquals(expected," B  0   ");

    }

    @Test
    void redToString1Test3(){
        CargoHold red= new CargoRed(1, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        String expected = red.toString1();
        assertEquals(expected," ░  0   ");
    }

    @Test
    void redToString1Test4(){
        CargoHold red= new CargoRed(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        red.addGood(new Goods(GoodsColor.BLUE));
        String expected = red.toString1();
        assertEquals(expected," B  0 ░ ");
    }

    @Test
    void redToString3Test(){
        CargoHold red= new CargoRed(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,100);
        String expected = red.toString3();
        assertEquals(expected," ░ 2 100");
    }

    @Test
    void redToString3Test2(){
        CargoHold red= new CargoRed(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,10);
        String expected = red.toString3();
        assertEquals(expected," ░ 2 10 ");
    }

    @Test
    void redToString3Test3(){
        CargoHold red= new CargoRed(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,10);
        String expected = red.toString3();
        assertEquals(expected, "   2 10 ");
    }

    @Test
    void redToString3Test4(){
        CargoHold red= new CargoRed(2, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,100);
        String expected = red.toString3();
        assertEquals(expected, "   2 100");

    }

    @Test
    void redToString3Test5(){
        CargoHold red = new CargoRed(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,100);
        red.addGood(new Goods(GoodsColor.BLUE));
        red.addGood(new Goods(GoodsColor.YELLOW));
        red.addGood(new Goods(GoodsColor.GREEN));
        String expected = red.toString3();
        assertEquals(expected, " G 2 100");
    }

    @Test
    void redToString3Test6(){
        CargoHold red = new CargoRed(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),null,0,10);
        red.addGood(new Goods(GoodsColor.BLUE));
        red.addGood(new Goods(GoodsColor.YELLOW));
        red.addGood(new Goods(GoodsColor.GREEN));
        String expected = red.toString3();
        assertEquals(expected, " G 2 10 ");
    }









}