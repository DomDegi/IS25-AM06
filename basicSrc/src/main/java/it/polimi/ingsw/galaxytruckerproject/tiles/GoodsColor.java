package it.polimi.ingsw.galaxytruckerproject.tiles;

public enum GoodsColor {
    Red(4), Yellow(3), Green(2), Blue(1);

    private int value;

    GoodsColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


