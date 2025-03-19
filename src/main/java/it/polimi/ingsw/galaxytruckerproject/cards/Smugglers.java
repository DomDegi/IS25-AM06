package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;

public class Smugglers extends Enemies{
    private final int lostGoods;
    private final ArrayList<Goods> rewardGoods;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private int won;

    public Smugglers(int level, int requiredDays, int cannonStrength, int lostGoods, ArrayList<Goods> rewardGoods){
        super(level, requiredDays, cannonStrength);
        this.lostGoods = lostGoods;
        this.rewardGoods = rewardGoods;
        this.initialized=false;
        this.currentPlayer =null;
        this.playerToInteract = new ArrayList<>();
        this.won =0;
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
        System.out.printf(currentPlayer+"they are coming for you! \n Do you want to use your double cannon?(yes/no)\n");
        if(playerToInteract.isEmpty()){
            game.endCardEvent();
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if(playerToInteract.isEmpty())
            game.endCardEvent();
        if(currentPlayer==null)
            currentPlayer=game.getListOfPlayers().getFirst();
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        if(won == 0){
            if (input[0].equalsIgnoreCase("yes")){
                System.out.println("input first the double cannons coordinates and after the batteries coordinates\n");
                won++;
                return;
            }else if (input[0].equalsIgnoreCase("no")){
                if(currentPlayer.useDoubleCannons(new ArrayList<Coordinates>()) < cannonStrength){
                    System.out.printf("Sorry"+currentPlayer+"you are too weak(looser)\n");
                    if(currentPlayer.getShipBoard().getGoods().size()>=lostGoods){
                        currentPlayer.removeGoods();
                    }else {
                        currentPlayer.removeBatteries();
                    }
                    playerToInteract.removeFirst();
                    initializeCard(game);
                }
                if(currentPlayer.useDoubleCannons(new ArrayList<Coordinates>()) > cannonStrength){
                    System.out.printf("HURRAY!! You are more powerful than the Smugglers, you've defeated them!!\n"+currentPlayer+"want to pillage dose filthy Smugglers(loose %d days)(yes/no)?",requiredDays);
                    won=2;
                    return;
                }
            }else {
                System.out.println("Invalid input: " + input[0]+ " retry");
            }
        } else if(won == 1) {
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
                System.out.printf("HURRAY!! You are more powerful than the Smugglers, you've defeated them!!\n"+currentPlayer+"want to pillage dose filthy Smugglers(loose %d days)(yes/no)?",requiredDays);
                won=2;
                return;
            }else if (playerStrength < cannonStrength){
                System.out.printf("Sorry"+currentPlayer+"you are too weak(looser)");
                if(currentPlayer.getShipBoard().getGoods().size()>=lostGoods){
                    currentPlayer.removeGoods();
                }else {
                    currentPlayer.removeBatteries();
                }
                playerToInteract.removeFirst();
                initializeCard(game);
            }
        } else if(won >= 2){
            if (input[0].equalsIgnoreCase("yes")){
                System.out.println("Good job galaxy truck driver");
                currentPlayer.gainGoods(rewardGoods);
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            }else if (input[0].equalsIgnoreCase("no")){
                System.out.println("You are too pure of heart for a galaxy truck driver");
            }else {
                System.out.println("Invalid input: " + input[0]);
            }
            playerToInteract.removeFirst();
        }
    }

    @Override
    public String toString() {
        return "Smugglers";
    }
}
