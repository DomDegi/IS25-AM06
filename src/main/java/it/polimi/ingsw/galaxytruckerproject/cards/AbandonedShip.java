package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.Optional;

public class AbandonedShip extends Card{
    private final int crewNumberRequired;
    private final int possibleCreditGains;
    private int playerIndex;
    private final CrewPenalty penaltyIfAccept;
    private Player playerToPlay =  null;
    private boolean playerAccepted;

    @JsonCreator
    public AbandonedShip(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays")int requiredDays,
            @JsonProperty("crewNumberRequired")int crewNumberRequired,
            @JsonProperty("possibleCreditGains")int possibleCreditGains
    ) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleCreditGains = possibleCreditGains;
        this.playerIndex = 0;
        this.penaltyIfAccept = new CrewPenalty(crewNumberRequired);
        this.playerAccepted = false;
    }

    @Override
    public void initializeCard(Game game) {
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            System.out.println("Everyone refused to fix the abandoned ship\n");
            game.endCardEvent();
            return;
        }
        playerToPlay = game.getListOfPlayers().get(this.playerIndex);
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (playerToPlay != null && playerToPlay.getPlayerName().equalsIgnoreCase(playerName)) {
            if (!playerAccepted) {
                if (playerToPlay.getTotalCrew() >= crewNumberRequired){
                    System.out.println(playerToPlay.getPlayerName() + " do you wish to trade" +
                            crewNumberRequired + " for " + possibleCreditGains + " cosmic credits and lose " +
                            requiredDays + " flight days?\n");
                    System.out.println("Input yes or no");
                    if (input[0].equalsIgnoreCase("yes")) {
                        playerAccepted = true;
                        System.out.println("input a pair of number x y for every crew to remove\n");
                        penaltyIfAccept.printInfo(playerToPlay);
                    }
                    else if (input[0].equalsIgnoreCase("no")){
                        playerIndex++;
                        initializeCard(game);
                    }
                }
                else {
                    playerIndex++;
                    initializeCard(game);
                }
            }
            else {
                if (penaltyIfAccept.applyPenalty(game, playerToPlay, input) == 1) {
                    playerToPlay.gainCredit(possibleCreditGains);
                    game.getFlightBoard().moveBackward(playerToPlay, requiredDays);
                    game.endCardEvent();
                }
                else
                    System.out.println("need more inputs");
            }
        }
    }

    @Override
    public String toString() {
        return
                "AbandonedShip: " + super.toString() + " crewNumberRequired "
                        + crewNumberRequired + " possibleCreditsGain " + possibleCreditGains;
    }

    public Player getPlayerToPlay() {
        return playerToPlay;
    }
}
