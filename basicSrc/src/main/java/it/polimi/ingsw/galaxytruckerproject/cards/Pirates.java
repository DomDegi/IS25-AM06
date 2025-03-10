package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.CannonShot;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;
import java.util.ArrayList;
import java.util.Scanner;

public class Pirates extends Enemies {
    private final int rewardCredits;
    private final ArrayList<CannonShot> listOfShots;

    public Pirates(int level, int requiredDays, int cannonStrength, int rewardCredits, ArrayList<CannonShot> listOfShots) {
        super(level, requiredDays, cannonStrength);
        this.rewardCredits = rewardCredits;
        this.listOfShots = listOfShots;
    }

    @Override
    public void executeCard(FlightBoard flightBoard){

        //iterates on the player array until the condition isn't false
        for(Player player: flightBoard.getRanking()){

            //if player is weaker, gets hit by cannonShots
            if (player.getCannonStrength() < cannonStrength) {
                System.out.printf("The pirates defeated you thanks to their superior cannon strength of %d,\n hang tight!\n", cannonStrength);
                listOfShots.forEach((cannonShot) -> {
                    cannonShot.throwCannonShot(player);
                });
            }
            //if player is stronger, they can choose if they want to spend days to get the rewards
            else if (player.getCannonStrength() > cannonStrength) {
                System.out.printf("You defeated the pirates,\n press 1 to accept %d and lose %d days of flight or 0 to refuse\n", rewardCredits, requiredDays);

                //reads player input
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                if (choice == 1) {
                    player.gainCredit(rewardCredits);
                    flightBoard.moveBackward(player.getPlayerRanking()-1, requiredDays);
                }
                //stops the outer for loop
                break;
            }
        }
    }
}
