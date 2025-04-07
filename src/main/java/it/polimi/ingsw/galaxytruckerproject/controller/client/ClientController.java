package it.polimi.ingsw.galaxytruckerproject.controller.client;

import it.polimi.ingsw.galaxytruckerproject.view.View;

public class ClientController {
    private ClientState state;
    private final View view;
    public ClientController(View view) {

        this.view = view;
    }
}
