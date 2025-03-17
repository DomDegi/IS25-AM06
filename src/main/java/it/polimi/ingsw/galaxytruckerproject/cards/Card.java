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
    //needed by useDoubleCannon and useDoubleEngine to decide if the coordinates in input are for batteries or not
    protected ArrayList<Coordinates> firstCoordinatesChoice;

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
        if (input.length % 2 == 0 && input.length > 0) {
            try {
                for (int i = 0; i < input.length; i++) {
                    coordinates.add(new Coordinates(Integer.parseInt(input[i]), Integer.parseInt(input[i + 1])));
                }
                return coordinates;
            } catch(NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Invalid input\n");
        //returns empty list
        return coordinates;
    }

    public int useDoubleEngines(ArrayList<Coordinates> chosenCoordinates) {

        //this conditions returns the value of single engine power + brown aliens
        if (chosenCoordinates.isEmpty()) {
            return calculateEngineStrength(chosenCoordinates, chosenCoordinates);
        }

        //this condition inputs the chosen engines to use and awaits for another input to choose the batteries
        if (firstCoordinatesChoice.isEmpty()) {
            firstCoordinatesChoice = new ArrayList<>(chosenCoordinates);
            return -2;
        }
        //this condition calculate if the inputs were correct, uses the batteries and returns the total value
        else {
            int engineStrength = calculateEngineStregth(firstCoordinatesChoice, chosenCoordinates);
            if  (engineStrength == -1) {
                System.out.println("Invalid input\n");
                firstCoordinatesChoice = new ArrayList<>();
                return -1;
            }
            return engineStrength;
        }
    }

    @Override
    public abstract String toString();
}