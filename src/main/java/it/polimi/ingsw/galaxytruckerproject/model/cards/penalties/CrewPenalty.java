package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.SendCoordinatesResponse;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;

public class CrewPenalty extends Penalty {

    private final int numberOfLostCrew;

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
        this.initializePenalty(playersView, player);
        return 0;
    }


    public boolean initializePenalty(ViewInterface view, Player player) {
        if (player.getTotalCrew() == 0) {
            return false;
        }
        view.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
        return true;
    }

    public void automaticPenalty(GameInterface game, Player disconnectedPlayer, ViewInterface view) {
        for (int i = 0; i < numberOfLostCrew && disconnectedPlayer.getTotalCrew() > 0; i++) {
            Coordinates firstCabin = disconnectedPlayer.getShipBoard().getCabinsCoordinates().getFirst();
            disconnectedPlayer.getShipBoard().chooseCrewToRemove(firstCabin);
        }
    }

    @Override
    public String toString() {
        return "CrewPenalty: " + numberOfLostCrew;
    }
}
