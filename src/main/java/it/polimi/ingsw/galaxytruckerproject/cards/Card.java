package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MeteorSwarm.class, name = "meteorSwarm"),
        @JsonSubTypes.Type(value = AbandonedStation.class, name = "AbandonedStation"),
        @JsonSubTypes.Type(value = AbandonedShip.class, name = "AbandonedShip"),
        @JsonSubTypes.Type(value = CombatZone.class, name = "CombatZone"),
        @JsonSubTypes.Type(value = Enemies.class, name = "Enemies"),
        @JsonSubTypes.Type(value = OpenSpace.class, name = "OpenSpace"),
        @JsonSubTypes.Type(value = StarDust.class, name = "StarDust"),
        @JsonSubTypes.Type(value = Planets.class, name = "Planets"),
        @JsonSubTypes.Type(value = Epidemic.class, name ="Epidemic")
})

public abstract class Card {
    protected final int level;
    protected final int requiredDays;

    public Card(int level, int requiredDays) {
        this.level = level;
        this.requiredDays = requiredDays;
    }

    public abstract void initializeCard(Game game);

    public abstract void executeCard(Game game, String playerName, String[] input);

    public int getLevel() {
        return level;
    }

    public int getRequiredDays() {
        return requiredDays;
    }

    public ArrayList<Coordinates> parseCoordinates(String[] input) {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        if (input.length % 2 == 0) {
            try {
                for (int i = 0; i < input.length; i++) {
                    coordinates.add(new Coordinates(Integer.parseInt(input[i]), Integer.parseInt(input[i + 1])));
                }
                return coordinates;
            } catch(NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}