package it.polimi.ingsw.galaxytruckerproject.tiles;


public class EquipCabin extends Cabin {

    AlienOptions alienabilty;
    CrewType crewType;

    public EquipCabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }

    public void setAlienAbility(AlienOptions alienabilty){
        
    }

    //SELECT THE CREWTYPE ACCORDING TO ITS ALIENOPTIONS
    public void setCrewType(CrewType crewType) {
        switch (crewType) {
            case HUMAN:
                this.crewType = CrewType.HUMAN;
                this.crew = 2;
            break;
            case PURPLE:
                if (this.alienabilty == AlienOptions.BOTH || this.alienabilty == AlienOptions.PURPLE){
                    this.crewType = CrewType.PURPLE;
                    this.crew = 1;
                    break;
                }
                else{
                    System.out.println("CAN'T FILL THIS CABIN WITH A PURPLE ALIEN");
                    break;
                }
            case BROWN:
                if(this.alienabilty == AlienOptions.BOTH || this.alienabilty == AlienOptions.BROWN){
                    this.crewType = CrewType.BROWN;
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
            case HUMAN:
                shipBoard.addBreakHumanCrew(+2);
                break;
            case PURPLE:
                shipBoard.addBreakPurpleAliens(true);
                break;
            case BROWN:
                shipBoard.addBreakBrownAliens(true);
                break;
        }
        shipBoard.getCabinsCoordinates().add(this.coordinates);
    }

    public boolean removeCrew(){
        if (this.crew>0) {
            crew--;
            switch (crewType) {
                case HUMAN:
                    shipBoard.addBreakHumanCrew(-1);
                    break;
                case PURPLE:
                    shipBoard.addBreakPurpleAliens(false);
                    break;
                case BROWN:
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
                case HUMAN:
                    shipBoard.addBreakHumanCrew(-this.crew);
                    break;
                case PURPLE:
                    shipBoard.addBreakPurpleAliens(false);
                    break;
                case BROWN:
                    shipBoard.addBreakBrownAliens(false);
                    break;
            }
            crew=0;
            shipBoard.getCabinsCoordinates().remove(this.coordinates);

        }
        super.destroy();
    }

    public CrewType getCrewType(){
        return this.crewType;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EquipCabin: ");
        sb.append(getCoordinates().getX()).append(" ").append(getCoordinates().getY()).
                                    append(" ").append(this.crew).append(" ").append(this.crewType);
        return sb.toString() +" " + super.toString();
    }
}
