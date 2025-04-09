package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsManager;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class AbandonedStation extends Card {
    private final int crewNumberRequired;
    private final ArrayList<Goods> possibleGoodsGain;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private boolean won;
    private GoodsManager goodsManager;

    @JsonCreator
    public AbandonedStation(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("crewNumberRequired") int crewNumberRequired, @JsonProperty("possibleGoodsGain") ArrayList<Goods> possibleGoodsGain) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = false;
    }
    //asks every player in order of ranking that meets the requirements if they want to spend days to gain the goods
    @Override
    public void initializeCard(Game game, Map<String, ViewInterface> viewsMap) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfInFlightPlayers());
            initialized=true;
        }
        if(playerToInteract.isEmpty()){
            game.endCardEvent();
            return;
        }
        System.out.printf("\nAbandoned station: you will loose %d flight days to gain the following goods:\n", requiredDays);
        goodsManager=new GoodsManager(currentPlayer,possibleGoodsGain);
        goodsManager.goodsPrinter(possibleGoodsGain);
        currentPlayer= playerToInteract.getFirst();
        if (currentPlayer.getTotalCrew() < crewNumberRequired){
            System.out.printf("\nSorry "+currentPlayer.getPlayerName()+" you can't land on the station, you need at least %d crew members and you have %d\n", crewNumberRequired,currentPlayer.getTotalCrew() );
            playerToInteract.removeFirst();
            initializeCard(game, );
            return;
        }
        System.out.printf("\n"+currentPlayer.getPlayerName()+" congratulation, you have "+currentPlayer.getTotalCrew() +" witch is more than %d input 'yes' to land on the station, input 'no' to ignore, you will loose %d flight days\n\n", crewNumberRequired,requiredDays);
    }

    @Override
    public void executeCard(Game game, Message message) {
        if (currentPlayer == null)
            currentPlayer = game.getListOfInFlightPlayers().getFirst();
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        if (playerToInteract.isEmpty()) {
            game.endCardEvent();
            return;
        }
        if (input.length == 0) {
            System.out.println("Invalid input format. Please provide integer values.\n");
            return;
        }
        if (!won) {
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
                System.out.println("\nInvalid input format. Please provide integer values.");
                return;
            }
            if (currentPlayer.getTotalCrew() >= crewNumberRequired) {
                if (choice == 1) {
                    game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                    goodsManager=new GoodsManager(currentPlayer,possibleGoodsGain);
                    won=true;
                    goodsManager.goodsPrinter(possibleGoodsGain);
                    System.out.print("Chose for each good where to put it, input 'done' to stop,'pick x y' to pick one good from your cargo\n");
                    //exit for loop: the station has been claimed
                } else if (choice == 0) {
                    System.out.println("\nNo action performed");
                    playerToInteract.removeFirst();
                    initializeCard(game, );
                } else {
                    System.out.println("\nInvalid choice: " + choice);
                }
            } else {
                System.out.printf("\nSorry" + currentPlayer + "you can't land on the station, you need at least %d crew members\n", crewNumberRequired);
            }
        }else{
            if(goodsManager.getReward(input)){
                game.endCardEvent();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AbandonedStation: ").append(super.toString()).append(" ").
                append("crewNumberRequired: ").append(crewNumberRequired).
                append(" possibleGoodsGain: ");
        for (Goods good: possibleGoodsGain)
            sb.append(good.toString()).append(" ");
        return sb.toString();
    }
}