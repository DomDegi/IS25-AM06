package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.ArrayList;

import static it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType.NotSupportSystem;

public class AlienLifeSupportsSystem extends Tile {
    CrewType colorAlienSupported;

    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west, int key, CrewType colorAlienSupported) {
        super(north, east, south, west,key);
        this.colorAlienSupported = colorAlienSupported;
        if (colorAlienSupported == CrewType.HUMAN)
            System.out.println("AlienLifeSupportsSystem only supports BROWN AND PURPLE");
    }

    //METODO COSTRUTTORE PER IL TEST
    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west, CrewType colorAlienSupported) {
        super(north, east, south, west,0);
        this.colorAlienSupported = colorAlienSupported;
        if (colorAlienSupported == CrewType.HUMAN)
            System.out.println("AlienLifeSupportsSystem only supports BROWN AND PURPLE");
    }

    public CrewType getAlienLifeSupportSystemColor() {return colorAlienSupported;}

    //RETURNS THE COORDINATES OF ALL THE ADJACENT EQUIP CABIN THAT HAS AN ALIEN WITH SAME COLOUR SUPPORTED
    // BY THE ALIEN LIFE SUPPORT SYSTEM
    public ArrayList<Coordinates> adjacentEquipCabinWithAlien() {
        ArrayList<Coordinates> adjacentEquipCabin = new ArrayList<Coordinates>();
       /* if(this.coordinates.getX()!=0 && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().Placeable()
            && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCrewType()!=null &&  !shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCrewType().equals(CrewType.NotAcabin)
                && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCrewType().equals(colorAlienSupported)){

            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCoordinates());
        }

        if(this.coordinates.getX()!=4 && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().Placeable()
            && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCrewType()!=null &&  !shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCrewType().equals(CrewType.NotAcabin)
                &&   shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCrewType().equals(colorAlienSupported)){
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCoordinates());
        }

        if(this.coordinates.getY()!=0 && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().Placeable()
            && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].get().getCrewType()!=null &&  !shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].get().getCrewType().equals(CrewType.NotAcabin)
                 && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].get().getCrewType().equals(colorAlienSupported)){
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].get().getCoordinates());
        }

        if(this.coordinates.getY()!=6 && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().Placeable()
            && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() + 1].get().getCrewType()!=null && !shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() + 1].get().getCrewType().equals(CrewType.NotAcabin)
                && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() + 1].get().getCrewType()!=null && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() + 1].get().getCrewType().equals(colorAlienSupported)) {
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() + 1].get().getCoordinates());
        }
        return adjacentEquipCabin;
        */
        if(this.coordinates.getX()!=0 && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].isPresent()){

            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCoordinates());
        }

        if(this.coordinates.getX()!=4 && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].isPresent()){
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCoordinates());
        }

        if(this.coordinates.getY()!=0 && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].isPresent()){
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() - 1].get().getCoordinates());
        }

        if(this.coordinates.getY()!=6 && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].isPresent()) {
            adjacentEquipCabin.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY() + 1].get().getCoordinates());
        }
        return adjacentEquipCabin;
    }

    public void destroy() {
        /*
        ArrayList<Coordinates> adjacentEquipCabinwithAlien = adjacentEquipCabinWithAlien();


        for (Coordinates c : adjacentEquipCabinwithAlien) {
            boolean covered = false;
            //Initialize with 0,0 that never get used in the ShipBoard
            Coordinates cabinToEmpty = new Coordinates(0, 0);
            ArrayList<Coordinates> otherAlienLifeSupport = shipBoard.getTile(c).adjacentLifeSupport();
            for (Coordinates other : otherAlienLifeSupport) {
                if (shipBoard.getTile(c).getAlienLifeSupportSystemColor().equals(colorAlienSupported)) {
                    covered = true;
                    cabinToEmpty = other;
                }else
                    shipBoard.getTile(c).removeCrew();
            }
        }*/
        this.colorAlienSupported=NotSupportSystem;
        ArrayList<Coordinates> adjacentEquipCabinwithAlien = adjacentEquipCabinWithAlien();
        for (Coordinates c : adjacentEquipCabinwithAlien) {
            shipBoard.getTile(c).checkAlienability();
        }
        super.destroy();
    }


    @Override
    public String toString() {
        return "AlienLifeSupportsSystem alienType=" + colorAlienSupported + " " + super.toString();
    }
}
