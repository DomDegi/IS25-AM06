package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.Optional;

public class AbandonedShip extends Card{
    private final int crewNumberRequired;
    private final int possibleCreditGains;
    private int playerIndex;
    private CrewPenalty penaltyIfAccept;
    private Optional<Player> playerToPlay =  Optional.empty();
    boolean playerAccepted;

    public AbandonedShip(int level, int requiredDays, int crewNumberRequired, int possibleCreditGains) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleCreditGains = possibleCreditGains;
        this.playerIndex = 0;
        this.penaltyIfAccept = new CrewPenalty(crewNumberRequired);
        this.playerAccepted = false;
    }

    @Override
    public String toString() {
        return "AbandonedShip";
    }

    @Override
    public void initializeCard(Game game) {
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            System.out.println("Everyone refused to fix the abandoned ship\n");
            game.endCardEvent();
            return;
        }
        playerToPlay = Optional.of(game.getListOfPlayers().get(this.playerIndex));
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (playerToPlay.isPresent() && playerToPlay.get().getPlayerName().equalsIgnoreCase(playerName)) {
            if (!playerAccepted) {
                System.out.println(playerToPlay.get().getPlayerName() + " do you wish to trade" +
                        crewNumberRequired + " for " + possibleCreditGains + " cosmic credits and lose " +
                        requiredDays + " flight days?\n");
                System.out.println("Input yes or no");
                if (input[0].equalsIgnoreCase("yes")) {
                    playerAccepted = true;
                    System.out.println("input a pair of number x y for every crew to remove\n");
                    penaltyIfAccept.printInfo(playerToPlay.get());
                }
                else if (input[0].equalsIgnoreCase("no")){
                    playerIndex++;
                    initializeCard(game);
                }
            }
            else {
                if (penaltyIfAccept.applyPenalty(game, playerToPlay.get(), input) == 1) {
                    playerToPlay.get().gainCredit(possibleCreditGains);
                    game.getFlightBoard().moveBackward(playerToPlay.get(), requiredDays);
                    game.DrawCard();
                }
            }
        }
    }
}
