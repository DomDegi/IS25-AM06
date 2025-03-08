package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.cards.penalties.Penalty;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;

import static it.polimi.ingsw.galaxytruckerproject.cards.ChallengeType.*;

public class CombatZone extends Card{
    private final LinkedHashMap<Challengetype, Penalty> listOfChallenges;

    public CombatZone(int level, LinkedHashMap<Challengetype, Penalty> listOfPenalties){
        super(level, 0);
        this.listOfChallenges = listOfPenalties;
    }

    @Override
    public void executeCard(FlightBoard flightBoard) {
        Player[] listOfPlayer = flightBoard.getRanking();

        for (Challengetype challenge: listOfChallenges.keySet()){
            Player losingPlayer;
            Penalty penaltyToApply;
            switch (challenge) {
                case MINIMUM_CANNON_STRENGTH:
                    losingPlayer = Arrays.stream(listOfPlayer).min(Comparator.comparingInt(Player::getCannonStrength));
                    penaltyToApply = listOfChallenges.get(challenge);
                    penaltyToApply.applyPenalty(losingPlayer, flightBoard);
                    break;
                case MINIMUM_ENGINE_POWER:
                    losingPlayer = Arrays.stream(listOfPlayer).min(Comparator.comparingInt(Player::getEnginePower));
                    penaltyToApply = listOfChallenges.get(challenge);
                    penaltyToApply.applyPenalty(losingPlayer, flightBoard);
                    break;
                case MINIMUM_CREW_NUMBER:
                    losingPlayer = Arrays.stream(listOfPlayer).min(Comparator.comparingInt(Player::getCrewNumber));
                    penaltyToApply = listOfChallenges.get(challenge);
                    penaltyToApply.applyPenalty(losingPlayer, flightBoard);
                    break;
            }
        }
    }
}

