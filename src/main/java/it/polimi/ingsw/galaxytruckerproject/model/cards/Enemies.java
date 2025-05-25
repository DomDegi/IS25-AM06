package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.Map;

/**
 * Abstract subclass of {@link Card} representing enemy encounters (e.g., Slavers, Smugglers, Pirates).
 * These cards have a defined cannon strength used during combat challenges.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Slavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = Smugglers.class, name = "Smugglers"),
        @JsonSubTypes.Type(value = Pirates.class, name = "Pirates"),
})
public abstract class Enemies extends Card {

    /** The cannon strength of the enemy card, used to evaluate firepower challenges */
    protected final int cannonStrength;

    /**
     * Constructor used by Jackson for deserialization.
     * @param level the difficulty level of the card
     * @param requiredDays the days required to handle the event
     * @param cannonStrength the firepower value of the enemies
     * @param filePath optional file path (e.g., for image or description)
     */
    @JsonCreator
    public Enemies(int level, int requiredDays, int cannonStrength, String filePath) {
        super(level, requiredDays, filePath);
        this.cannonStrength = cannonStrength;
    }

    /**
     * Standard constructor without file path.
     * @param level the difficulty level of the card
     * @param requiredDays the days required to handle the event
     * @param cannonStrength the firepower value of the enemies
     */
    public Enemies(int level, int requiredDays, int cannonStrength) {
        super(level, requiredDays);
        this.cannonStrength = cannonStrength;
    }

    /**
     * Returns a string representation of the enemy card, including cannon strength.
     * @return string with card details
     */
    @Override
    public String toString() {
        return super.toString() + "  cannonStrength: " + cannonStrength + " ";
    }

    /**
     * Abstract method to initialize the enemy card with game context and player views.
     * @param game the game interface
     * @param viewsMap map of player names to their virtual views
     */
    @Override
    public abstract void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap);

    /**
     * Returns the firepower of the enemy, used in combat evaluations.
     * @return cannon strength as float
     */
    @Override
    public float getEnemiesFirePower() {
        return cannonStrength;
    }
}
