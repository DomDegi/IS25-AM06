package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;

import java.util.Scanner;

public class AbandonedShip extends Card{
    private final int crewNumberRequired;
    private final int possibleCreditGains;

    public AbandonedShip(int level, int requiredDays, int crewNumberRequired, int possibleCreditGains) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleCreditGains = possibleCreditGains;
    }

    //asks players in order of ranking if they want to exchange crew for credits
    @Override
    public void executeCard(FlightBoard flightBoard) {
        Player[] listOfPlayers = flightBoard.getRanking();

        for (Player player: listOfPlayers){
            if (player.getCrewNumber() >= crewNumberRequired) {

                System.out.printf("Do you want to lose %d flight days and %d crew members to gain %d cosmic credits?\n",
                        requiredDays, crewNumberRequired, possibleCreditGains);
                System.out.println("Input 1 to accept or 0 to refuse\n");
                //reads player input
            }

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                if (choice == 1) {
                    flightBoard.moveBackward(player.getPlayerRanking(), requiredDays);
                    player.loseCrew(crewNumberRequired);
                    player.gainCredit(possibleCreditGains);
                    //exit for loop: the station has been claimed
                    break;
                }
            }
        }

    }
}
