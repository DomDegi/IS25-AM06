
<<<<<<<< Updated upstream:src/main/java/it/polimi/ingsw/galaxytruckerproject/Goods.java
<<<<<<<< Updated upstream:src/main/java/it/polimi/ingsw/galaxytruckerproject/Goods.java
package it.polimi.ingsw.galaxytruckerproject;
========
package it.polimi.ingsw.galaxytruckerproject.model.goods;
>>>>>>>> Stashed changes:src/main/java/it/polimi/ingsw/galaxytruckerproject/model/goods/Goods.java
========
package it.polimi.ingsw.galaxytruckerproject.model.goods;
>>>>>>>> Stashed changes:src/main/java/it/polimi/ingsw/galaxytruckerproject/model/goods/Goods.java

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Goods {
    private final GoodsColor color;

    @JsonCreator
    public Goods(@JsonProperty("color") GoodsColor color) {
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
