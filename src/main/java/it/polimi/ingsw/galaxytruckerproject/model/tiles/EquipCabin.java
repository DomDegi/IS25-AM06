package it.polimi.ingsw.galaxytruckerproject.model.tiles;


import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType.HUMAN;

public class EquipCabin extends Cabin {

    private AlienOptions alienability = AlienOptions.NO;
    private CrewType crewType;
    private int crewToLoad;


    public EquipCabin(Link north, Link east, Link south, Link west, int key) {

        super(north, east, south, west, key);
        alienability=null;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public EquipCabin(Link north, Link east, Link south, Link west) {

        super(north, east, south, west, 0);
        alienability=null;
    }

    //GETTER METHOD
    public CrewType getCrewType(){
        return this.crewType;
    }

    //THIS METHOD RETURNS THE LIST OF THE ADJACENT ALIEN LIFE SUPPORT SYSTEM
    public ArrayList<Coordinates> adjacentLifeSupport(){
        ArrayList<Coordinates> adjacentLifeSupport = new ArrayList<>();
/*
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
*/
        if(this.coordinates.getX() - 1 >=0 && this.coordinates.getX() - 1 <= 4 && this.coordinates.getY()>=0 && this.coordinates.getY()<=6 && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCoordinates());

        if(this.coordinates.getX() + 1 >=0 && this.coordinates.getX() + 1 <= 4 && this.coordinates.getY()>=0 && this.coordinates.getY()<=6 && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCoordinates());

        if(this.coordinates.getX() >=0 && this.coordinates.getX() <= 4 && this.coordinates.getY()-1>=0 && this.coordinates.getY()-1<=6&&shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getCoordinates());

        if(this.coordinates.getX() >=0 && this.coordinates.getX() <= 4 && this.coordinates.getY()+1>=0 && this.coordinates.getY()+1<=6&&shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getCoordinates());
        return adjacentLifeSupport;
    }

    //THIS METHOD CHECKS IF THE CABIN CAN HAVE AN ALIEN AND WHICH
    public void checkAlienability(){
        AlienOptions tempAlien = null;
        if(this.alienability!=null)
        {
            tempAlien = this.alienability;
        }
        alienability = AlienOptions.NO;
        ArrayList<Coordinates> adjacentLifeSupport = new ArrayList<Coordinates>();
        adjacentLifeSupport = adjacentLifeSupport();

        CrewType alienLifeSupportSystemColor;
        boolean checkDouble = false;
        for(Coordinates c : adjacentLifeSupport) {
            alienLifeSupportSystemColor = shipBoard.getTile(c).getAlienLifeSupportSystemColor();
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
        if(tempAlien!=null && this.alienability != tempAlien){
            resetAlien(this.alienability, tempAlien);
        }
    }

    public void resetAlien(AlienOptions current, AlienOptions prec){
        if(current.equals(AlienOptions.BOTH))
            return;
        if((!(current==AlienOptions.PURPLE) && crewType==CrewType.PURPLE) ||  (!(current==AlienOptions.BROWN) && crewType==CrewType.BROWN) )
        {
            this.removeCrew();
        }
    }

    //SELECT THE CREWTYPE ACCORDING TO ITS ALIENOPTIONS
    public void setCrewType(CrewType crewType) {
        //checkAlienability();
        switch (crewType) {
            case HUMAN:
                this.crewType = HUMAN;
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
        if(!shipBoard.getCabinsCoordinates().contains(this.coordinates))
            shipBoard.getCabinsCoordinates().add(this.coordinates);
        if (crewToLoad > 0) {
            this.crew = crewToLoad;
            crewToLoad = 0;
            switch (crewType) {
                case PURPLE -> shipBoard.addBreakPurpleAliens(true);
                case BROWN -> shipBoard.addBreakBrownAliens(true);
                case HUMAN -> shipBoard.addBreakHumanCrew(crew);
            }
        }
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
            if(this.crew==0)
                if(shipBoard.getCabinsCoordinates()!=null && !shipBoard.getCabinsCoordinates().isEmpty())
                    shipBoard.getCabinsCoordinates().remove(this.coordinates);
        }
        else {
            System.out.println("THIS CABIN IS EMPTY");
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
        return "EquipCabin" + " " + this.crewType +  " " + this.crew + " " + super.toString()+ super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }

    @Override
    public String toString1(){
        if(getCrew()==0) {
            return "    " + getNorth() + "   ";
        }else if(crewType==CrewType.HUMAN) {
            if (getCrew() == 2)
                return " H  " + getNorth() + " H ";
            else if (getCrew() == 1)
                return " H  " + getNorth() + "   ";
        }else if (crewType==CrewType.PURPLE) {
            return " P  " + getNorth() + "   ";
        }else if (crewType==CrewType.BROWN) {
            return " B  " + getNorth() + "   ";
        }
        return " /  " + getNorth() + " / ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" EC "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if (getKey() >= 100)
            return "   " + getSouth() +" " + getKey();
        else if (getKey() >= 10 && getKey() < 100)
            return "   " + getSouth() +" " + getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
    }
    //METHODS FOR TESTING
    @Override
    public AlienOptions getAlienability() {
        return alienability;
    }

    public void setCrewTypeOfTestTile(CrewType type) {
        this.crewType = type;
        this.alienability = AlienOptions.BOTH;
        if (Objects.requireNonNull(crewType) == HUMAN) {
            this.crew = 2;
        } else {
            this.crew = 1;
        }
    }

    @Override
    public String toStringData() {
        String string = "EC " + key + " " + north.toString() + " " + east.toString() + " " + south.toString() + " " + west.toString() + " " + crew + " ";
        string = string + Objects.requireNonNullElse(crewType, "N");
        return string;
    }

    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.crewToLoad =  Integer.parseInt(attributes[6]);
        if (!attributes[7].equals("N")) {
            this.crewType = CrewType.fromString(attributes[7]);
        }
        if (crewType != CrewType.HUMAN && crewToLoad > 1) {
            System.out.println("This equip cabin has something wrong going on");
            throw new IllegalArgumentException();
        }
    }

    public EquipCabin() {}
}
