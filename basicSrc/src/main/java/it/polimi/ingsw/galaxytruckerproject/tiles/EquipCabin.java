package it.polimi.ingsw.galaxytruckerproject.tiles;

public class EquipCabin extends Cabin {

    AlienOptions alienabilty;
    CrewType crewType;

    public EquipCabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }
    public void getStat(){
        switch (crewType) {
            case Human:
                shipBoard.addBreakCrew(+2);
                break;
            case Purple:
                shipBoard.addBreakPurpleAliens(true);
                break;
            case Brown:
                shipBoard.addBreakBrownAliens(true);
                break;
        }
    }

    public void removeCrew(){
        switch (crewType) {
            case Human:
                shipBoard.addBreakCrew(-2);
                break;
            case Purple:
                shipBoard.addBreakPurpleAliens(false);
                break;
            case Brown:
                shipBoard.addBreakBrownAliens(false);
        }

    }

}
