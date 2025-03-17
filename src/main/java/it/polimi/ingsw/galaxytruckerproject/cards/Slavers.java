package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.Scanner;

public class Slavers extends Enemies{
    private final int rewardCredits;
    private final int lostCrew;

    public Slavers(int level, int requiredDays, int cannonStrength, int rewardCredits, int lostCrew) {
        super(level, requiredDays, cannonStrength);
        this.rewardCredits = rewardCredits;
        this.lostCrew = lostCrew;
    }

    @Override
    public void executeCard(FlightBoard flightBoard){

        //iterates on the player array until the condition isn't false
        for(Player player: flightBoard.getInGamePlayers()){

            //if player is weaker, loses crew members
            if (player.getCannonStrength() < cannonStrength) {
                System.out.printf("The slavers defeated you thanks to their superior cannon strength of %d,\n hang tight\n", cannonStrength);
                player.loseCrew(lostCrew);
            }

            //if player is stronger, they can choose if they want to spend days to get the rewards
            else if (player.getCannonStrength() > cannonStrength) {
                System.out.printf("You defeated the slavers,\n press 1 to accept %d and lose %d days of flight or 2 to refuse\n", rewardCredits, requiredDays);

                //reads player input
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                if (choice == 1) {
                    player.gainCredit(rewardCredits);
                    flightBoard.moveBackward(player.getPlayerRanking(), requiredDays);
                }

                //stops the outer for loop
                break;
            }
        }
    }
}
