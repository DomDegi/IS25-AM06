package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

public class ControllerGUI implements ControllerUI {
    private final ClientController clientController;
    private DisplayableView view;

    public ControllerGUI(ClientController clientController) {
        this.clientController = clientController;
        view=this.clientController.getView();
    }

    @Override
    public boolean input(String input) {
        return true;
    }
}
