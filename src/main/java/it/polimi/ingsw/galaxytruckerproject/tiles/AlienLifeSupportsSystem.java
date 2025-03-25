package it.polimi.ingsw.galaxytruckerproject.tiles;

import java.util.ArrayList;

public class AlienLifeSupportsSystem extends Tile{
    CrewType alienType;
    ArrayList<Coordinates> adjacentEquipCabin;


    public AlienLifeSupportsSystem(Link north, Link east, Link south, Link west, CrewType alienType) {
        super(north,east,south,west);
        this.alienType = alienType;
        if(alienType == CrewType.HUMAN)
            System.out.println("AlienLifeSupportsSystem only supports BROWN AND PURPLE");
    }
    public CrewType getAlienType() {
        return alienType;
    }
    @Override
    public String toString() {
        return "AlienLifeSupportsSystem alienType=" + alienType +" " + super.toString();
    }
    //CHECK IF THERE IS A CABIN NEAR THE TILE AND WHICH ALIEN OPTION IT HAS
    public void checkEquipCabin(){
        ArrayList<Coordinates> adjacentTiles = new ArrayList<Coordinates>();
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()-1][this.coordinates.getY()].get().getCoordinates());
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()+1][this.coordinates.getY()].get().getCoordinates());
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()-1].get().getCoordinates());
        adjacentTiles.add(shipBoard.getTilesTable()[this.coordinates.getX()][this.coordinates.getY()+1].get().getCoordinates());

        for(Coordinates c: adjacentTiles){
            if(shipBoard.getTilesTable()[c.getX()][c.getY()].get().getCrewType().equals(CrewType.BROWN)||shipBoard.getTilesTable()[c.getX()][c.getY()].get().getCrewType().equals(CrewType.PURPLE)){
                adjacentEquipCabin.add(c);
            }
        }

    }

    public void destroy(){
        super.destroy();
        checkEquipCabin();
        for(Coordinates c: adjacentEquipCabin){
            if(shipBoard.getTilesTable()[c.getX()][c.getY()].get().getCrewType().equals(alienType)){
                ArrayList<Coordinates> adjacentToCabin = shipBoard.getTilesTable()[c.getX()][c.getY()].get().checkAlienability();

                boolean foundAnotherOne = false;
                for (Coordinates c1:adjacentToCabin){
                    if(shipBoard.getTilesTable()[c1.getX()][c1.getY()].get().getAlienType().equals(alienType)){
                        foundAnotherOne = true;
                        break;
                    }
                }
                if(!foundAnotherOne){

                }

            }
        }


        //more
    }

}
