package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class AbandonedStation extends Card {
    private final int crewNumberRequired;
    private final ArrayList<Goods> possibleGoodsGain;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;

    public AbandonedStation(int level, int requiredDays, int crewNumberRequired, ArrayList<Goods> possibleGoodsGain) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.initialized=false;
        this.currentPlayer =null;
        this.playerToInteract = new ArrayList<>();
    }
    //asks every player in order of ranking that meets the requirements if they want to spend days to gain the goods
    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
        }
        System.out.printf("Abandoned station: you will loose %d flight days to gain the following goods:\n", requiredDays);
        possibleGoodsGain.forEach(goods -> System.out.printf("%s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue()));
        currentPlayer= playerToInteract.getFirst();
        if (currentPlayer.getTotalCrew() >= crewNumberRequired){
            System.out.printf("Sorry"+currentPlayer+"you can't land on the station, you need at least %d crew members\n", crewNumberRequired);
            playerToInteract.removeFirst();
            initializeCard(game);
            return;
        }
        System.out.printf(currentPlayer+"input 'yes' to land on the station, input 'no' to ignore, you will loose %d flight and get days\n", requiredDays);
        if(playerToInteract.isEmpty()){
            game.endCardPhase();
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (playerToInteract.isEmpty()) {
            game.endCardPhase();
            return;
        }
        if (currentPlayer == null) {
            currentPlayer = game.getListOfPlayers().getFirst();
        }
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        int choice;
        if (input[0].equalsIgnoreCase("no")) {
            input[0] = "0";
        }
        if (input[0].equalsIgnoreCase("yes")) {
            input[0] = "1";
        }
        try {
            choice = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            return;
        }
        if (currentPlayer.getTotalCrew() >= crewNumberRequired) {
            if (choice == 1) {
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                currentPlayer.gainGoods();
                //exit for loop: the station has been claimed
            } else if (choice == 0) {
                System.out.println("No action performed");
                playerToInteract.removeFirst();
                initializeCard(game);
            } else {
                System.out.println("Invalid choice: " + choice);
                return;
            }
        } else {
            System.out.printf("Sorry" + currentPlayer + "you can't land on the station, you need at least %d crew members\n", crewNumberRequired);
        }
    }

    @Override
    public String toString() {
        return "AbandonedStation";
    }
}
