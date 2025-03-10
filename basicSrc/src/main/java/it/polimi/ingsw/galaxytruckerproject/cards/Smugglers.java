package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;
import java.util.ArrayList;

public class Smugglers extends Enemies{
    private final int lostGoods;
    private final ArrayList<Goods> rewardGoods;

    public Smugglers(int level, int requiredDays, int cannonStrength, int lostGoods, ArrayList<Goods> rewardGoods){
        super(level, requiredDays, cannonStrength);
        this.lostGoods = lostGoods;
        this.rewardGoods = rewardGoods;
    }

    @Override
    public void executeCard(FlightBoard flightBoard) {

        //iterates on the player array until the condition isn't false
        for(Player player: FlightBoard.getRanking()){

            //if player is weaker, loses goods
            if (player.getCannonStrength() < cannonStrength) {
                System.out.printf("The smuglers defeated you thanks to their superior cannon strenght of %d,\n hang tight\n", cannonStrength);
                player.loseGoods(lostGoods);
            }

            //if player is stronger, they can choose if they want to spend days to get the rewards
            else if (player.getCannonStrength() > cannonStrength) {
                System.out.printf("You defeated the smuglers,\n press 1 to accept the following goods and lose %d days of flight or 2 to refuse\n", requiredDays);
                rewardGoods.forEach(goods -> {
                    System.out.printf("%s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                });

                //reads player input
                int choice = scanner.nextInt();
                if (choice == 1) {
                    player.gainGoods(rewardGoods);
                    flightBoard.moveBackward(player, requiredDays);
                }
                //stops the outer for loop
                break;
            }
        }
    }
}
