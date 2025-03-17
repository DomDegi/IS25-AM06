package it.polimi.ingsw.galaxytruckerproject.tiles;


public class EquipCabin extends Cabin {

    AlienOptions alienabilty;
    CrewType crewType;

    public EquipCabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }

    //SELECT THE CREWTYPE ACCORDING TO ITS ALIENOPTIONS
    public void setCrewType(CrewType crewType) {
        switch (crewType) {
            case Human:
                this.crewType = CrewType.Human;
                this.crew = 2;
            break;
            case Purple:
                if (this.alienabilty == AlienOptions.BOTH || this.alienabilty == AlienOptions.PURPLE){
                    this.crewType = CrewType.Purple;
                    this.crew = 1;
                    break;
                }
                else{
                    System.out.println("CAN'T FILL THIS CABIN WITH A PURPLE ALIEN");
                    break;
                }
            case Brown:
                if(this.alienabilty == AlienOptions.BOTH || this.alienabilty == AlienOptions.BROWN){
                    this.crewType = CrewType.Brown;
                    this.crew = 1;
                    break;
                }
                else{
                    System.out.println("CAN'T FILL THIS CABIN WITH A BROWN ALIEN");
                    break;
                }
        }
    }

    public void getStat(){
        switch (crewType) {
            case Human:
                shipBoard.addBreakHumanCrew(+2);
                break;
            case Purple:
                shipBoard.addBreakPurpleAliens(true);
                break;
            case Brown:
                shipBoard.addBreakBrownAliens(true);
                break;
        }
    }


    public boolean removeCrew(){
        if (this.crew>0) {
            crew--;
            switch (crewType) {
                case Human:
                    shipBoard.addBreakHumanCrew(-1);
                    break;
                case Purple:
                    shipBoard.addBreakPurpleAliens(false);
                    break;
                case Brown:
                    shipBoard.addBreakBrownAliens(false);
                    break;
            }
        }
        else{
            System.out.println("THIS CABIN IS EMPTY");
            return false;
        }
        if (this.crew == 0) {
            shipBoard.getCabinsCoordinates().remove(this.coordinates);
        }
        return true;
    }

    public void destroy(){
        super.destroy();
        if (this.crew>0) {
            switch (crewType) {
                case Human:
                    shipBoard.addBreakHumanCrew(-this.crew);
                    break;
                case Purple:
                    shipBoard.addBreakPurpleAliens(false);
                    break;
                case Brown:
                    shipBoard.addBreakBrownAliens(false);
                    break;
            }
            crew=0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCoordinates().getX()).append(" ").append(getCoordinates().getY()).
                                    append(" ").append(this.crew).append(" ").append(this.crewType);
        return sb.toString();
    }
}
