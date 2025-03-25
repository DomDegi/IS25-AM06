package it.polimi.ingsw.galaxytruckerproject.tiles;


import java.util.ArrayList;
import java.util.Optional;

public class EquipCabin extends Cabin {

    private AlienOptions alienability;
    private CrewType crewType;

    public EquipCabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
    }

    //THIS METHOD GET CALLED IN THE getStat
    public ArrayList<Coordinates> checkAlienability(){
        Optional<Tile>[][] tilesTable = shipBoard.getTilesTable();
        ArrayList<Coordinates> coordinatesAlienSupportSystem = new ArrayList<>();
        alienability = AlienOptions.NO;
    /*
        if(shipBoard.getTilesTable()[this.coordinates.getX()-1][this.coordinates.getY()].get().getAlienType().equals(CrewType.BROWN)
         || shipBoard.getTilesTable()[this.coordinates.getX()+1][this.coordinates.getY()].get().getAlienType().equals(CrewType.BROWN)
         || shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getAlienType().equals(CrewType.BROWN)
         || shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getAlienType().equals(CrewType.BROWN)) {
            alienability = AlienOptions.BROWN;
        }

        if(shipBoard.getTilesTable()[this.coordinates.getX()-1][this.coordinates.getY()].get().getAlienType().equals(CrewType.PURPLE)
                || shipBoard.getTilesTable()[this.coordinates.getX()+1][this.coordinates.getY()].get().getAlienType().equals(CrewType.PURPLE)
                || shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getAlienType().equals(CrewType.PURPLE)
                || shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getAlienType().equals(CrewType.PURPLE)){
            if(alienability == AlienOptions.BROWN){alienability = AlienOptions.BOTH;}
            else
                alienability = AlienOptions.PURPLE;
        }
    */
        ArrayList<Coordinates> adjacentTiles = new ArrayList<Coordinates>();
        if()
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()-1][this.coordinates.getY()].get().getCoordinates());
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()+1][this.coordinates.getY()].get().getCoordinates());
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getCoordinates());
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getCoordinates());


        alienability = AlienOptions.NO;
        CrewType alienLifeSupportSystemColor;
        boolean checkDouble = false;
        for(Coordinates c : adjacentTiles) {
            alienLifeSupportSystemColor = shipBoard.getTilesTable()[c.getX()][c.getY()].get().getAlienLifeSupportSystemColor();


            //CHECK IF THE ADJACENT TILE IS AN ALIEN LIFE SUPPORT SYSTEM
            if(alienLifeSupportSystemColor.equals(CrewType.BROWN) || alienLifeSupportSystemColor.equals(CrewType.PURPLE)) {
                coordinatesAlienSupportSystem.add(c);
                //CHECK IF WE ALREADY VERIFIED THE PRESENCE OF TWO DIFFERENT COLORS OF ALIEN LIFE SUPPORT SYSTEM
                if (!alienability.equals(AlienOptions.BOTH)) {
                    //CHECK IF THE CURRENT ALIEN LIFE SUPPORT SYSTEM IS BROWN
                    if (alienLifeSupportSystemColor.equals(CrewType.BROWN)) {
                        alienability = AlienOptions.BROWN;
                    }
                    //CHECK IF IT IS PURPLE
                    if (alienLifeSupportSystemColor.equals(CrewType.PURPLE)) {
                        //CHECK IF WE ALREADY FOUND A BROWN ONE
                        if (alienability == AlienOptions.BROWN) {
                            alienability = AlienOptions.BOTH;
                        }
                        else
                            alienability = AlienOptions.PURPLE;
                    }

                }
            }
        }
        return coordinatesAlienSupportSystem;
    }

    //SELECT THE CREWTYPE ACCORDING TO ITS ALIENOPTIONS
    public void setCrewType(CrewType crewType) {
        switch (crewType) {
            case HUMAN:
                this.crewType = CrewType.HUMAN;
                this.crew = 2;
            break;
            case PURPLE:
                if (this.alienability == AlienOptions.BOTH || this.alienability == AlienOptions.PURPLE){
                    this.crewType = CrewType.PURPLE;
                    this.crew = 1;
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
                    break;
                }
                else{
                    System.out.println("CAN'T FILL THIS CABIN WITH A BROWN ALIEN");
                }
                break;
        }
    }


    public void getStat(){
        //checkAlienability();
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
        return "EquipCabin" + " " + this.crewType +  " " + this.crew + " " + super.toString();
    }

    //METHODS FOR TESTING



}
