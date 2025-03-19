package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;

public class Slavers extends Enemies{
    private final int rewardCredits;
    private final int lostCrew;
    private int playerIndex;
    private Player currentPlayer = null;
    private CrewPenalty penaltyIfLose;
    private int won = 0;

    public Slavers(int level, int requiredDays, int cannonStrength, int rewardCredits, int lostCrew) {
        super(level, requiredDays, cannonStrength);
        this.rewardCredits = rewardCredits;
        this.lostCrew = lostCrew;
        this.playerIndex = 0;
    }

    public String toString() {
        return "Slavers";
    }

    @Override
    public void initializeCard(Game game) {
        if (playerIndex > game.getNumberOfPlayers() - 1){
            System.out.println("No player beat the slavers\n");
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfAllPlayer().get(playerIndex);
        System.out.println(currentPlayer.getPlayerName() + ", you are face to face with a ship of Slavers\n");
        System.out.println("their cannon strength is " + cannonStrength + "\n");
        System.out.println("if yours is lower than theirs, you will lose "+ lostCrew + "crew members\n");
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
                                rewardCredits + " cosmic credits for defeating the slavers\n");
                    }
                    else if (currentPlayer.useDoubleCannons(new ArrayList<Coordinates>()) < cannonStrength){
                        won = - 1;
                        System.out.println("input the coordinates of the crew members to lose to the slavers\n");
                        currentPlayer.printCurrentInfoCabins();
                    }
                    else {
                        System.out.println(currentPlayer.getPlayerName() + " tied with the slavers\n");
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
                                rewardCredits + " cosmic credits for defeating the slavers\n");
                    }
                    else if (playerStrength < cannonStrength){
                        won = - 1;
                        System.out.println("input the coordinates of the crew members to lose to the slavers\n");
                        currentPlayer.printCurrentInfoCargoHolds();
                    }
                    else  {
                        System.out.println(currentPlayer.getPlayerName() + " tied with the slavers\n");
                        System.out.println("next player\n");
                        playerIndex++;
                        initializeCard(game);
                    }
                }
            }
            else if (won == 1){
                if (input[0].equalsIgnoreCase("no")){
                    game.endCardEvent();
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
