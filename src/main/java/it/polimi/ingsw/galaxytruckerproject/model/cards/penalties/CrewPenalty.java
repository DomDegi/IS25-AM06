package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.SendCoordinatesResponse;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;

public class CrewPenalty extends Penalty {

    private int numberOfLostCrew;

    @JsonCreator
    public CrewPenalty(@JsonProperty("numberOfLostCrew") int numberOfLostCrew) {
        this.numberOfLostCrew = numberOfLostCrew;
    }

    public int getNumberOfLostCrew() {
        return numberOfLostCrew;
    }

    @Override
    public int applyPenalty(GameInterface game, Player player, ViewInterface playersView, Message message) {
        if (message.getMessageType().equals(MessageType.SEND_COORDINATES_RESPONSE)) {
            SendCoordinatesResponse sendCoordinatesResponse = (SendCoordinatesResponse) message;
            ArrayList<Coordinates>  coordinates = sendCoordinatesResponse.getCoordinates();
            if (coordinates.isEmpty()) {
                return 0;
            }
            while (coordinates.size() > numberOfLostCrew) {
                coordinates.removeLast();
            }
            if (coordinates.size() < numberOfLostCrew) {
                playersView.showErrorMessage("error sending coordinates");
                return 0;
            }
            if (player.removeCrew(coordinates)) {
                return 1;
            }
            else{
                playersView.showErrorMessage("error removing crew: coordinate wasn't a cabin with crew inside");
            }
        }
        if (numberOfLostCrew == 0){
            return 1;
        }
        printInfo(playersView, player);
        return 0;
    }


    public void printInfo(ViewInterface view, Player player) {
        if (player.getTotalCrew() == 0) {
            view.showGenericMessage("No crew found to remove!");
            numberOfLostCrew = 0;
        }
        view.showGenericMessage("You have to remove " + numberOfLostCrew + " crew members!");
        view.asksToInputCoordinates();
    }

    @Override
    public String toString() {
        return "CrewPenalty: " + numberOfLostCrew;
    }
}
