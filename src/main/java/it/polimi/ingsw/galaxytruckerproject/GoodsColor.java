package it.polimi.ingsw.galaxytruckerproject;

public enum GoodsColor {
    RED(4), YELLOW(3), GREEN(2), BLUE(1);

    private int value;

    GoodsColor(int value) {
        this.value = value;
    }
    public String toString() {
        if(this == RED)
            return "Red ";
        if(this == YELLOW)
            return "Yellow ";
        if (this == GREEN)
            return "Green ";
        return "Blue ";
    }

    public int getValue() {
        return value;
    }
}


