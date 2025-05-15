package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.StringWriter;

public class ClientUpdater {

    private ClientUpdater(){}

    public static String currentGameStatus(GameController gameController) {
        StringWriter sw = new StringWriter();

        ControllerSerializerDeserializer.saveForUpdates(gameController,sw);
        GameSerializerDeserializer.saveForUpdates(gameController.getGame(),sw);

        for (Player player: gameController.getAllPlayers()) {
            PlayerSerializerDeserializer.save(player,sw);
        }
        return sw.toString();
    }
}
