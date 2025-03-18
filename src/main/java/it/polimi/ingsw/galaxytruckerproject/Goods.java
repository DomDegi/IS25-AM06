
package it.polimi.ingsw.galaxytruckerproject;
public class Goods {
    private final GoodsColor color;

    public Goods(GoodsColor color){
        this.color = color;
    }

    public String toString() {
        return color.toString();
    }

    public GoodsColor getColor() {
        return color;
    }
    public int getValue() {
        return color.getValue();
    }
}
