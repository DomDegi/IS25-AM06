package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.GoodsPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsManager;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;

public class Smugglers extends Enemies{
    private final GoodsPenalty lostGoods;
    private final ArrayList<Goods> rewardGoods;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private int won;
    private GoodsManager goodsManager;

    @JsonCreator
    public Smugglers(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("cannonStrength") int cannonStrength, @JsonProperty("lostGoods") int lostGoods, @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods) {
        super(level, requiredDays, cannonStrength);
        this.lostGoods =new GoodsPenalty(lostGoods);
        this.rewardGoods = rewardGoods;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = 0;
    }

    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
            System.out.printf("WATCH OUT, SMUGGLERS!! \nIf you don't have at least a Cannon Strength of "+cannonStrength+" you will loose "+lostGoods+"\nDESTROY THEM and you will loose"+requiredDays+"to fill your cargo with the following goods:\n");
            goodsManager=new GoodsManager(currentPlayer,rewardGoods);
            goodsManager.goodsPrinter(rewardGoods);
        }
        if (playerToInteract.isEmpty()) {

            game.endCardEvent();
            return;
        }
        currentPlayer= playerToInteract.getFirst();
        System.out.printf("\n"+currentPlayer.getPlayerName()+", they are coming for you! \nDo you want to use your double cannon?(yes/no)\n");
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if(currentPlayer==null)
            currentPlayer=game.getListOfPlayers().getFirst();
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        if(playerToInteract.isEmpty()) {
            game.endCardEvent();
            return;
        }
        if (input.length == 0) {
            System.out.println("Invalid input format. Please provide integer values.\n");
            return;
        }
        //executeCard
        if(won == 0){
            if (input[0].equalsIgnoreCase("yes")){
                System.out.println("\nInput first the double cannons coordinates and after the batteries coordinates\n");
                won=1;
            }else if (input[0].equalsIgnoreCase("no")){
                if(currentPlayer.useDoubleCannons(new ArrayList<>()) < cannonStrength){
                    System.out.printf("\nSorry"+currentPlayer.getPlayerName()+"you are too weak(looser)\n");
                    if(currentPlayer.getShipBoard().getAllGoods().size()>=lostGoods.getNumber()){
                        System.out.print("\nChoose the cargo hold that will be pillaged by the Smugglers?\n");
                    }else if(currentPlayer.getShipBoard().getNumBatteries()!=0){
                        System.out.print("\nChoose the battery container that will be pillaged by the Smugglers?\n");
                    }else{
                        System.out.print("\nYou have nothing to pillage\n");
                        won=0;
                        playerToInteract.removeFirst();
                        initializeCard(game);
                        return;
                    }
                    won=-1;
                }else if(currentPlayer.useDoubleCannons(new ArrayList<>()) > cannonStrength){
                    System.out.printf("\nHURRAY!! You are more powerful than the Smugglers, you've defeated them!!\n\n"+currentPlayer.getPlayerName()+"want to pillage dose filthy Smugglers(loose %d days)(yes/no)?",requiredDays);
                    won=2;
                }else if(currentPlayer.useDoubleCannons(new ArrayList<>()) == cannonStrength){
                    System.out.println("\nYAY!! You are as powerful as the Smugglers, you've defeated them, but they managed to escape jus in time!\n");
                    won=0;
                    playerToInteract.removeFirst();
                    initializeCard(game);
                }
            }else {
                System.out.println("Invalid input: " + input[0]+ " retry");
            }
        //D.C.Manager
        } else if(won == 1) {
            ArrayList<Coordinates> coordinates = new ArrayList<>(currentPlayer.parseCoordinates(input));
            if (coordinates.isEmpty()){
                System.out.println("Invalid input\n");
                return;
            }
            float playerStrength = currentPlayer.useDoubleCannons(coordinates);
            if (playerStrength == -2){
                System.out.println("Need the batteries coordinates\n");
            }else if (playerStrength == -1){
                System.out.println("\nInvalid input of batteries or cannons: input again cannons coordinates\n");
            }else if (playerStrength > cannonStrength){
                System.out.printf("\nHURRAY!! You are more powerful than the Smugglers, you've defeated them!!\n\n"+currentPlayer.getPlayerName()+"want to pillage dose filthy Smugglers(loose %d days)(yes/no)?",requiredDays);
                won=2;
            }else if (playerStrength < cannonStrength){
                System.out.printf("Sorry"+currentPlayer.getPlayerName()+"you are too weak(looser)\n");
                if(currentPlayer.getShipBoard().getAllGoods().size()>=lostGoods.getNumber()){
                    System.out.print("\nChoose the cargo hold that will be pillaged by the Smugglers?\n");
                }else {
                    System.out.print("\nChoose the battery container that will be pillaged by the Smugglers?\n");
                }
                won=-1;
            }else if(playerStrength == cannonStrength){
                System.out.println("\nYAY!! You are as powerful as the Smugglers, you've defeated them, but they managed to escape jus in time!\n");
                won=0;
                playerToInteract.removeFirst();
                initializeCard(game);
            }
        //won
        } else if(won == 2){
            if (input[0].equalsIgnoreCase("yes")){
                System.out.println("Good job galaxy truck driver /n here is hour reward: \n");
                goodsManager.goodsPrinter(rewardGoods);
                System.out.println("Chose for each good where to put it, input 'done' to stop,'pick x y' to pick one good from your cargo\n");
                goodsManager=new GoodsManager(currentPlayer,rewardGoods);
                won=3;
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            }else if (input[0].equalsIgnoreCase("no")){
                System.out.println("You are too pure of heart for a galaxy truck driver\nCard Finished\n");
                game.endCardEvent();
            }else {
                System.out.println("Invalid input: " + input[0]);
            }
        //getReward
        } else if(won == 3){
            if(goodsManager.getReward(input)){
                game.endCardEvent();
            }
        //loseManager
        } else if(won == -1) {
            if(lostGoods.applyPenalty(game,currentPlayer,input)==1){
                won=0;
                playerToInteract.removeFirst();
                initializeCard(game);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Smugglers: ").append(super.toString()).append(" lostGoods: ").append(this.lostGoods).append(" ");
        string.append("rewardGoods: ");
        for (Goods goods : rewardGoods) {
            string.append(goods.toString()).append(" ");
        }
        return string.toString();
    }
}