package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class Smugglers extends Enemies{
    private final int lostGoods;
    private final ArrayList<Goods> rewardGoods;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;

    public Smugglers(int level, int requiredDays, int cannonStrength, int lostGoods, ArrayList<Goods> rewardGoods){
        super(level, requiredDays, cannonStrength);
        this.lostGoods = lostGoods;
        this.rewardGoods = rewardGoods;
        this.initialized=false;
        this.currentPlayer =null;
        this.playerToInteract = new ArrayList<>();
    }
    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
            System.out.printf("WATCH OUT, SMUGGLERS!! \n If you don't have at least a Cannon Strength of "+cannonStrength+" you will loose "+lostGoods+"\n DESTROY THEM and you will loose"+requiredDays+"to fill your cargo with the following goods:\n");
            rewardGoods.forEach(goods -> System.out.printf("%s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue()));
        }
        currentPlayer= playerToInteract.getFirst();
        if (currentPlayer.getCannonStrength() < cannonStrength){
            System.out.printf("Sorry"+currentPlayer+"you are too weak(looser)\n");
            if(currentPlayer.getShipBoard().getGoods().size()>=lostGoods){
                currentPlayer.removeGoods();
            }else {
                currentPlayer.removeBatteries();
            }
            playerToInteract.removeFirst();
            initializeCard(game);
        }else if (currentPlayer.getCannonStrength() > cannonStrength){
            System.out.printf("HURRAY!! You are more powerful than the Smugglers, you've defeated them!!\n"+currentPlayer+"input 1 to pillage dose filthy Smugglers(loose %d days), input 0 to ignore",requiredDays);
        }
        if(playerToInteract.isEmpty()){
            return;
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if(playerToInteract.isEmpty()){
            game.endCardEvent();
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
        if (choice==1){
            System.out.println("Good job galaxy truck driver");
            currentPlayer.gainGoods(rewardGoods);
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
        }else if (choice==0){
            System.out.println("You are too pure of heart for a galaxy truck driver");
        }else {
            System.out.println("Invalid choice: " + choice);
            executeCard(game, playerName, input);
        }
        playerToInteract.removeFirst();
    }

    @Override
    public String toString() {
        return "Smugglers";
    }
}
