package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;

public class Pirates extends Enemies {
    private final int rewardCredits;
    private int playerIndex;
    private Player currentPlayer = null;
    private final ProjectilePenalty penaltyIfLose;
    private int won = 0;

    @JsonCreator
    public Pirates(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("listOfShots") ArrayList<Projectile> listOfShots) {
        super(level, requiredDays, cannonStrength);  // Chiamata al costruttore della classe base
        this.rewardCredits = rewardCredits;
        this.playerIndex = 0;
        this.penaltyIfLose = new ProjectilePenalty(listOfShots);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Pirates: ").append(super.toString()).append("rewardCredits: ").append(rewardCredits).append(" ");
        for (Projectile projectile: penaltyIfLose.getListOfProjectiles()) {
            string.append(projectile.toString());
        }
        return string.toString();
    }

    @Override
    public void initializeCard(Game game) {
        if (playerIndex > game.getNumberOfPlayers() - 1){
            System.out.println("No player beat the pirates\n");
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfAllPlayer().get(playerIndex);
        System.out.println(currentPlayer.getPlayerName() + ", you are face to face with a ship of Pirates\n");
        System.out.println("their cannon strength is " + cannonStrength + "\n");
        System.out.println("if yours is lower than theirs, you will get hit by a series of cannon shots\n");
        System.out.println("input if needed first the double cannons coordinates and after the batteries coordinates\n");
        currentPlayer.printCurrentInfoCannons();
        currentPlayer.printCurrentInfoBatteries();
    }


    //pay crew penalty
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (currentPlayer != null && playerName.equalsIgnoreCase(currentPlayer.getPlayerName())) {
            if (won == 0) {
                if (input[0].equalsIgnoreCase("no")){
                    if (currentPlayer.useDoubleCannons(new ArrayList<Coordinates>()) > cannonStrength) {
                        won = 1;
                        System.out.println("input yes or no if you want to spend " + requiredDays + " flight days to gain" +
                                rewardCredits + " cosmic credits for defeating the pirates\n");
                    }
                    else if (currentPlayer.useDoubleCannons(new ArrayList<Coordinates>()) < cannonStrength){
                        won = - 1;
                        penaltyIfLose.printInfoOnAllProjectiles();
                    }
                    else {
                        System.out.println(currentPlayer.getPlayerName() + " tied with the pirates\n");
                        System.out.println("next player\n");
                        playerIndex++;
                        initializeCard(game);
                    }
                }
                else {
                    ArrayList<Coordinates> coordinates = new ArrayList<>(currentPlayer.parseCoordinates(input));
                    if (coordinates.isEmpty()){
                        System.out.println("invalid input\n");
                        return;
                    }
                    float playerStrength = currentPlayer.useDoubleCannons(coordinates);
                    if (playerStrength == -2){
                        System.out.println("need the batteries coordinates\n");
                        return;
                    }

                    if (playerStrength == -1){
                        System.out.println("invalid input of batteries or cannons: input again cannons coordinates\n");
                        return;
                    }
                    if (playerStrength > cannonStrength){
                        won = 1;
                        System.out.println("input yes or no if you want to spend " + requiredDays + " flight days to gain" +
                                rewardCredits + " cosmic credits for defeating the pirates\n");
                    }
                    else if (playerStrength < cannonStrength){
                        won = - 1;
                        System.out.println("input the coordinates of the crew members to lose to the pirates\n");
                        currentPlayer.printCurrentInfoCargoHolds();
                    }
                    else  {
                        System.out.println(currentPlayer.getPlayerName() + " tied with the pirates\n");
                        System.out.println("next player\n");
                        playerIndex++;
                        initializeCard(game);
                    }
                }
            }
            else if (won == 1){
                if (input[0].equalsIgnoreCase("no")){
                    game.drawCard();
                }
                else if (input[0].equalsIgnoreCase("yes")){
                    game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                    currentPlayer.gainCredit(rewardCredits);
                    game.endCardEvent();
                }

            }
            else if (won == -1){
                if (penaltyIfLose.applyPenalty(game, currentPlayer, input) == 1) {
                    playerIndex++;
                    initializeCard(game);
                }
                else {
                    System.out.println("input more correct coordinates\n");
                    currentPlayer.printCurrentInfoCabins();
                }
            }
        }
    }
}