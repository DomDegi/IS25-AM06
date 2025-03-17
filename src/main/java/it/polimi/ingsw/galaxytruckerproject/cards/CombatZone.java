package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;
import it.polimi.ingsw.galaxytruckerproject.cards.penalties.Penalty;

import java.util.LinkedHashMap;

public class CombatZone extends Card{
    private final LinkedHashMap<ChallengeType, Penalty> listOfChallenges;

    public CombatZone(int level, LinkedHashMap<ChallengeType, Penalty> listOfPenalties){
        super(level, 0);
        this.listOfChallenges = listOfPenalties;
    }

    @Override
    public void executeCard(FlightBoard flightBoard) {
        Player[] listOfPlayer = flightBoard.getRanking();

        for (ChallengeType challenge: listOfChallenges.keySet()){
            Player losingPlayer = null;
            Penalty penaltyToApply;
            switch (challenge) {
                case MINIMUM_CANNON_STRENGTH:
                    for (Player player : listOfPlayer){
                        if(losingPlayer==null){
                            losingPlayer = player;
                        } else if(player.getCannonStrength() < losingPlayer.getCannonStrength()){
                            losingPlayer = player;
                        }
                    }
                    penaltyToApply = listOfChallenges.get(challenge);
                    penaltyToApply.applyPenalty(losingPlayer, flightBoard);
                    break;
                case MINIMUM_ENGINE_POWER:
                    for (Player player : listOfPlayer){
                        if(losingPlayer==null){
                            losingPlayer = player;
                        } else if(player.getEnginePower() < losingPlayer.getEnginePower()){
                            losingPlayer = player;
                        }
                    }
                    penaltyToApply = listOfChallenges.get(challenge);
                    penaltyToApply.applyPenalty(losingPlayer, flightBoard);
                    break;
                case MINIMUM_CREW_NUMBER:
                    for (Player player : listOfPlayer){
                        if(losingPlayer==null){
                            losingPlayer = player;
                        } else if(player.getCrewNumber() < losingPlayer.getCrewNumber()){
                            losingPlayer = player;
                        }
                    }
                    penaltyToApply = listOfChallenges.get(challenge);
                    penaltyToApply.applyPenalty(losingPlayer, flightBoard);
                    break;
            }
        }
    }
}

