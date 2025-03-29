package it.polimi.ingsw.galaxytruckerproject.tiles;


import java.util.ArrayList;

public class EquipCabin extends Cabin {

    private AlienOptions alienability;
    private CrewType crewType;

    public EquipCabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }

    //GETTER METHOD
    public CrewType getCrewType(){
        return this.crewType;
    }

    //THIS METHOD RETURNS THE LIST OF THE ADJACENT ALIEN LIFE SUPPORT SYSTEM
    public ArrayList<Coordinates> adjacentLifeSupport(){
        ArrayList<Coordinates> adjacentLifeSupport = new ArrayList<>();

        if(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].isPresent()
                && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().Placeable()
                && !shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()].get().getAlienLifeSupportSystemColor().equals(CrewType.NotSupportSystem))
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCoordinates());

        if(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].isPresent()
                && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().Placeable()
                && !shipBoard.getTilesTable()[this.coordinates.getX()+ 1][this.coordinates.getY()].get().getAlienLifeSupportSystemColor().equals(CrewType.NotSupportSystem))
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCoordinates());

        if(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].isPresent()
                && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().Placeable()
                && !shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getAlienLifeSupportSystemColor().equals(CrewType.NotSupportSystem))
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getCoordinates());

        if(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].isPresent()
                && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().Placeable()
                && !shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getAlienLifeSupportSystemColor().equals(CrewType.NotSupportSystem))
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getCoordinates());

        return adjacentLifeSupport;
    }

    //THIS METHOD CHECKS IF THE CABIN CAN HAVE AN ALIEN AND WHICH
    public void checkAlienability(){

        alienability = AlienOptions.NO;
        ArrayList<Coordinates> adjacentLifeSupport = new ArrayList<Coordinates>();
        adjacentLifeSupport = adjacentLifeSupport();

        CrewType alienLifeSupportSystemColor;
        boolean checkDouble = false;
        for(Coordinates c : adjacentLifeSupport) {
            alienLifeSupportSystemColor = shipBoard.getTilesTable()[c.getX()][c.getY()].get().getAlienLifeSupportSystemColor();
            //CHECK WHICH COLOR IS THE ADJACENT ALIEN LIFE SUPPORT SYSTEM

            //CHECK IF THE CURRENT ALIEN LIFE SUPPORT SYSTEM IS BROWN
            if (alienLifeSupportSystemColor.equals(CrewType.BROWN)) {
                //CHECK IF WE ALREADY FOUND A PURPLE ONE
                if (alienability == AlienOptions.PURPLE) {
                    alienability = AlienOptions.BOTH;
                    break;
                } else
                    alienability = AlienOptions.BROWN;
            }
            //CHECK IF THE CURRENT ALIEN LIFE SUPPORT SYSTEM IS PURPLE
            else if (alienLifeSupportSystemColor.equals(CrewType.PURPLE)) {
                //CHECK IF WE ALREADY FOUND A BROWN ONE
                if (alienability == AlienOptions.BROWN) {
                    alienability = AlienOptions.BOTH;
                    break;
                } else
                    alienability = AlienOptions.PURPLE;
            }
        }
    }


    //SELECT THE CREWTYPE ACCORDING TO ITS ALIENOPTIONS
    public void setCrewType(CrewType crewType) {
        //checkAlienability();
        switch (crewType) {
            case HUMAN:
                this.crewType = CrewType.HUMAN;
                this.crew = 2 ;
                shipBoard.addBreakHumanCrew(+2);
            break;

            case PURPLE:
                if (this.alienability == AlienOptions.BOTH || this.alienability == AlienOptions.PURPLE){
                    this.crewType = CrewType.PURPLE;
                    this.crew = 1;
                    shipBoard.addBreakPurpleAliens(true);
                    break;
                }
                else{
                    System.out.println("CAN'T FILL THIS CABIN WITH A PURPLE ALIEN");
                }
                break;

            case BROWN:
                if(this.alienability == AlienOptions.BOTH || this.alienability == AlienOptions.BROWN){
                    this.crewType = CrewType.BROWN;
                    this.crew = 1;
                    shipBoard.addBreakBrownAliens(true);
                    break;
                }
                else{
                    System.out.println("CAN'T FILL THIS CABIN WITH A BROWN ALIEN");
                }
                break;
        }
    }


    public void getStat(){
        checkAlienability();
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
            shipBoard.getCabinsCoordinates().remove(this.coordinates);
            return false;
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



    @Override
    public String toString() {
        return "EquipCabin" + " " + this.crewType +  " " + this.crew + " " + super.toString();
    }

    //METHODS FOR TESTING
    public AlienOptions getAlienability() {
        return alienability;
    }


}
