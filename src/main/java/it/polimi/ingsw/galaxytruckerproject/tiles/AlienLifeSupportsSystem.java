package it.polimi.ingsw.galaxytruckerproject.tiles;

import java.util.ArrayList;

public class AlienLifeSupportsSystem extends Tile {
    CrewType colorAlienSupported;


    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west, CrewType colorAlienSupported) {
        super(north, east, south, west);
        this.colorAlienSupported = colorAlienSupported;
        if (colorAlienSupported == CrewType.HUMAN)
            System.out.println("AlienLifeSupportsSystem only supports BROWN AND PURPLE");
    }

    public CrewType getAlienLifeSupportSystemColor() {
        return colorAlienSupported;
    }

    @Override
    public String toString() {
        return "AlienLifeSupportsSystem alienType=" + colorAlienSupported + " " + super.toString();
    }

    //RETURNS THE COORDINATES OF ALL THE ADJACENT EQUIP CABIN THAT HAS AN ALIEN
    public ArrayList<Coordinates> adjacentEquipCabinWithAlien() {
        ArrayList<Coordinates> adjacentTiles = new ArrayList<Coordinates>();
        if(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().Placeable())
            adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX() - 1][this.coordinates.getY()].get().getCoordinates());

        if(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().Placeable())
            adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX() + 1][this.coordinates.getY()].get().getCoordinates());

        if(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().Placeable())
            adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getCoordinates());

        if(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].isPresent() && shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().Placeable())
            adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getCoordinates());


        ArrayList<Coordinates> adjacentEquipCabin = new ArrayList<Coordinates>();

        for (Coordinates c : adjacentTiles) {
            if (shipBoard.getTilesTable()[c.getX()][c.getY()].get().getCrewType().equals(colorAlienSupported)) {
                adjacentEquipCabin.add(c);
            }
        }
        return adjacentEquipCabin;
    }

    public void destroy() {
        //super.destroy();
        ArrayList<Coordinates> adjacentEquipCabinwithAlien = adjacentEquipCabinWithAlien();


        for (Coordinates c : adjacentEquipCabinwithAlien) {
            boolean covered = false;
            //Initialize with 0,0 that never get used in the ShipBoard
            Coordinates cabinToEmpty = new Coordinates(0, 0);
            ArrayList<Coordinates> otherAlienLifeSupport = shipBoard.getTile(c).checkAlienability();
            for (Coordinates other : otherAlienLifeSupport) {
                if (shipBoard.getTile(c).getAlienLifeSupportSystemColor().equals(colorAlienSupported)) {
                    covered = true;
                    cabinToEmpty = other;
                    break;
                }
            }
            if (covered == false) {
                shipBoard.getTile(cabinToEmpty).removeCrew();
            }
        }

    }
}
