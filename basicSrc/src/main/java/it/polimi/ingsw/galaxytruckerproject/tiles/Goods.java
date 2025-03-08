package it.polimi.ingsw.galaxytruckerproject.tiles;

public class Goods {
    private final GoodsColor color;

    public Goods(GoodsColor color){
        this.color = color;
    }

    public GoodsColor getColor() {
        return color;
    }
    public int getValue() {
        return color.getValue();
    }
}
