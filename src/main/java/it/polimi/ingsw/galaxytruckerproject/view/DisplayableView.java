package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;

public interface DisplayableView extends ViewInterface{
    void printFlightboard(LightFlightboard lightFlightboard);
    void printShipboard(LightShipBoard lightShipBoard);
}
