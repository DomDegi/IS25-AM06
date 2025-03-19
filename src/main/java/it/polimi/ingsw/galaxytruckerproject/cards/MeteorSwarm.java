package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Meteor;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;

public class MeteorSwarm extends Card {
    private final ArrayList<Projectile> listOfMeteors;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private Coordinates coordinates;

    public MeteorSwarm(int level, ArrayList<Projectile> listOfMeteors) {
        super(level, 0);
        this.listOfMeteors = listOfMeteors;
        this.initialized=false;
        this.currentPlayer =null;
        this.coordinates=null;
        this.playerToInteract = new ArrayList<>();
    }

    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized = true;
            System.out.println("Meteor swarm(though for the law of physic this i impossible): embrace your self and hope for the best");
        }
        if (listOfMeteors.isEmpty()) {
            return;
        }
        int diceRoll = listOfMeteors.getFirst().rollTheDices();
        System.out.println(listOfMeteors.getTipe() + "from" + listOfMeteors.getFirst().getDirection() + "at" + diceRoll);
        currentPlayer = playerToInteract.removeFirst();
        Coordinates coordinates= listOfMeteors.getFirst().Throw(currentPlayer, diceRoll);
        if (currentPlayer.getShipBoard().searchCannon(coordinates) && listOfMeteors.getTipe() == "LargeMeteor") {
            if(currentPlayer.getShipBoard().searchCannon(coordinates)=="Double"){
                System.out.println(currentPlayer + "fire that chunky Meteor or it will hit you(1 for Yes, 0 for No I hate my ship)");
            }else {
                System.out.println(currentPlayer + ", you have destroyed that chunky Meteor");
            }
        } else if(listOfMeteors.getTipe()=="SmallMeteor"&&currentPlayer.getShipBoard().isFlat(coordinates)){
            System.out.println("YOU HAVE BEEN HIT BY A METEOR, but it was a small one, you lucky");
            playerToInteract.removeFirst();
            initializeCard(game);
        } else if(listOfMeteors.getTipe()=="SmallMeteor"&&!currentPlayer.getShipBoard().isFlat(coordinates)){
            System.out.println("YOU HAVE BEEN HIT BY A METEOR, but it was a small one, BUT IT HIT YOU ON A EXPOSED CONNECTOR");
            currentPlayer.getShipBoard().destroyTile(coordinates);
            playerToInteract.removeFirst();
            initializeCard(game);
        }else{
            System.out.println("YOU HAVE BEEN HIT BY A METEOR");
            currentPlayer.getShipBoard().destroyTile(coordinates);
            playerToInteract.removeFirst();
            initializeCard(game);
        }
        if(playerToInteract.isEmpty()&&listOfMeteors.isEmpty()){
            return;
        }
        if(playerToInteract.isEmpty()){
            listOfMeteors.removeFirst();
            initialized=false;
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if(playerToInteract.isEmpty()){
            game.endCardEvent();
            return;
        }
        if(currentPlayer==null){
            currentPlayer=game.getListOfPlayers().getFirst();
        }
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        int choice;
        try {
            choice = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            executeCard(game, playerName, input);
            return;
        }
        if (choice == 1) {
            System.out.println("Successfully fired the meteor");
        }else if (choice == 0) {
            System.out.println("No action performed");
            currentPlayer.getShipBoard().destroyTile(coordinates);
            playerToInteract.removeFirst();
            initializeCard(game);
        }else {
            System.out.println("Invalid choice: " + choice);
            executeCard(game, playerName, input);
        }
        playerToInteract.removeFirst();
        initializeCard(game);
        return;
    }


    @Override
    public String toString() {
        return "";
    }
}
