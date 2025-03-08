package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Goods;

import java.util.ArrayList;

public class AbandonedStation extends Card {
    private final int crewNumberRequired;
    private final ArrayList<Goods> possibleGoodsGain;

    public AbandonedStation(int level, int requiredDays, int crewNumberRequired, ArrayList<Goods> possibleGoodsGain) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
    }

    //asks every player in order of ranking that meets the requirements if they want to spend days to gain the goods
    @Override
    public void executeCard(FlightBoard flightBoard) {
        Player[] listOfPlayers = flightBoard.getRanking();

        for (Player player: listOfPlayers){
            if (player.getCrewNumber >= crewNumberRequired){

                System.out.printf("Do you want to lose %d flight days to gain the following goods?\n", requiredDays);

                possibleGoodsGain.forEach(goods -> {
                    System.out.printf("%s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                });

                System.out.println("Input 1 to accept or 0 to refuse\n");
                //reads player input
                int choice = scanner.nextInt();
                if (choice == 1) {
                    flightBoard.moveBackward(player, requiredDays);
                    player.gainGoods(possibleGoodsGain);
                    //exit for loop: the station has been claimed
                    break;
                }
            }
        }

    }
}
