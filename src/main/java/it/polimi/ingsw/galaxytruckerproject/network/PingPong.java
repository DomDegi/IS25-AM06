package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PingPong implements Runnable {

    private ConcurrentHashMap<String, GameController> games;
    public PingPong(ConcurrentHashMap<String, GameController> games) {
            this.games = games;
        }
        @Override
        public void run() {
            while (true) {
                if (games != null) {
                    for (GameController game : games.values()) {
                        if (game.getActivePlayers() != null) {
                            for (String playerName : game.getActivePlayers().keySet()) {
                                game.pingPong(playerName, game.getViewFromNickname(playerName));
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
}
