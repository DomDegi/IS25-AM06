package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Slavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = Smugglers.class, name = "Smugglers"),
        @JsonSubTypes.Type(value = Pirates.class, name = "Pirates"),
})

public abstract class Enemies extends Card {
    protected final int cannonStrength;

    @JsonCreator
    public Enemies(int level, int requiredDays, int cannonStrength,String filePath) {
        super(level, requiredDays, filePath);
        this.cannonStrength = cannonStrength;
    }

    public Enemies(int level, int requiredDays, int cannonStrength) {
        super(level, requiredDays);
        this.cannonStrength = cannonStrength;
    }

    @Override
    public String toString() {
        return super.toString() + "  cannonStrength: " + cannonStrength + " ";
    }

    @Override
    public abstract void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap);

    @Override
    public float getEnemiesFirePower() {
        return cannonStrength;
    }
}
