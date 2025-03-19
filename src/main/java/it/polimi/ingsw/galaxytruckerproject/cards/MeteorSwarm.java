package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.CannonPenalty;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class MeteorSwarm extends Card {
    private final ArrayList<Projectile> listOfMeteors;
    private Player currentPlayer;
    private int playerIndex;
    private CannonPenalty currentMeteor = null;

    public MeteorSwarm(int level, ArrayList<Projectile> listOfMeteors) {
        super(level, 0);
        this.listOfMeteors = listOfMeteors;
    }

    @Override
    public void initializeCard(Game game){
        if (listOfMeteors.isEmpty()){
            game.endCardEvent();
            return;
        }
        if (playerIndex > game.getNumberOfPlayers() - 1){
            listOfMeteors.removeFirst();
            playerIndex = 0;
        }

        currentPlayer = game.getListOfAllPlayer().get(playerIndex);
    }

    @Override
    public void executeCard(Game game, String player, String[] input){

    }

    @Override
    public String toString() {
        return "MeteorSwarm";
    }
}
