package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;
import java.util.Random;

public class MeteorSwarm extends Card {
    private ArrayList<Projectile> listOfMeteors;
    private Player currentPlayer;
    private int playerIndex;
    private ProjectilePenalty currentMeteor;
    private int currentDiceRoll;

    @JsonCreator
    public MeteorSwarm(@JsonProperty("level") int level,
                       @JsonProperty("listOfMeteors") ArrayList<Projectile> listOfMeteors) {
        super(level, 0);
        this.listOfMeteors = listOfMeteors;
        this.currentMeteor = new ProjectilePenalty(new ArrayList<>());
        this.playerIndex = 0;
        this.currentDiceRoll = 0;
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
            diceRoll();
        }

        currentPlayer = game.getListOfAllPlayer().get(playerIndex);

        if (currentMeteor.getListOfProjectiles().isEmpty()){
            currentMeteor.addProjectile(listOfMeteors.getFirst());
            currentMeteor.setDiceRoll(currentDiceRoll);
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input){
        if (playerName.equalsIgnoreCase(currentPlayer.getPlayerName()) && currentPlayer != null) {
            //when penalty is over on the current player
            if (currentMeteor.applyPenalty(game, currentPlayer, input) == 1){
                playerIndex++;
                initializeCard(game);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("MeteorSwarm: ");
        for (Projectile projectile : listOfMeteors) {
            string.append(projectile.toString());
        }
        return string.toString();
    }

    public void diceRoll() {
        Random random = new Random();
        this.currentDiceRoll = 2 + random.nextInt(11);
    }
}
