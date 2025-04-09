package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

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
        diceRoll();
    }

    @Override
    public void initializeCard(Game game, Map<String, ViewInterface> viewsMap){
        if (listOfMeteors.isEmpty()){
            game.endCardEvent();
            return;
        }
        if (playerIndex > game.getNumberOfPlayers()-1 ){
            listOfMeteors.removeFirst();
            playerIndex = 0;
            diceRoll();
        }

        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);

        if (currentMeteor.getListOfProjectiles().isEmpty()){
            currentMeteor.addProjectile(listOfMeteors.getFirst());
            currentMeteor.setDiceRoll(currentDiceRoll);
        }
    }

    @Override
    public void executeCard(Game game, Message message){
        if (playerName.equalsIgnoreCase(currentPlayer.getPlayerName()) && currentPlayer != null) {
            //when penalty is over on the current player
            if (currentMeteor.applyPenalty(game, currentPlayer, , input, ) == 1){
                playerIndex++;
                initializeCard(game, );
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
        /*Random random = new Random();
        this.currentDiceRoll = 2 + random.nextInt(11);

         */
        currentDiceRoll=8;
    }

    //for testing
    public int getPlayerIndex(){
        return playerIndex;
    }
}
