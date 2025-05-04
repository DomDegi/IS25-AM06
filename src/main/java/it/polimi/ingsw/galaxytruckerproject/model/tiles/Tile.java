package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public abstract class Tile implements Serializable,Cloneable {
    protected Link north;
    protected Link east;
    protected Link south;
    protected Link west;
    protected Coordinates coordinates;

    protected ShipBoardInterface shipBoard;
    protected final int key;
    protected boolean booked;

    public Tile(Link north, Link east, Link south, Link west, int key) {
        this.north =north;
        this.east=east;
        this.south=south;
        this.west=west;
        this.coordinates = new Coordinates(0,0);
        this.key = key;
        this.booked = false;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public Tile(Link north, Link east, Link south, Link west) {
        this.north =north;
        this.east=east;
        this.south=south;
        this.west=west;
        this.coordinates = new Coordinates(0,0);
        this.key = 0;
        this.booked = false;
    }

    public int getKey() {
        return key;
    }
    @Override
    public String toString() {
        return "Tile north:"+north.toString() + " east:" + east.toString() + " south:" + south.toString() + " west:" + west.toString()+"\n"
                +"coordinates: "+coordinates.getX()+coordinates.getY();
    }


    //GETTER METHODS LINKS
    public Link getNorth(){

        return north;
    }
    public Link getEast(){

        return east;
    }
    public Link getSouth(){

        return south;
    }
    public Link getWest(){

        return west;
    }

    public boolean isCorrect(){
        Optional<Tile> other;
        Optional<Tile>[][] tileTable= shipBoard.getTilesTable();
        //Check north
        /*
        if(this.coordinates.getX()!=0) {
            other = tileTable[this.coordinates.getX()-1][this.coordinates.getY()];
            if(!other.isEmpty() && !(other.get() instanceof VoidTile) && !north.isConnected(other.get().getSouth()) ) {
                return false;
            }
        } */
        //Check east
        if(this.coordinates.getY()!=6) {
            other = tileTable[this.coordinates.getX()][this.coordinates.getY()+1];
            if(other.isPresent() && other.get().fillable() && !this.east.isConnected(other.get().getWest())) {
                return false;
            }
        }

        //Check south
        if(this.coordinates.getX()!=4) {
            other = tileTable[this.coordinates.getX()+1][this.coordinates.getY()];
            if(!other.isEmpty() && !(other.get() instanceof VoidTile) && !south.isConnected(other.get().getNorth())) {
                return false;
            }
        }/*
        //Check west

        if(this.coordinates.getY()!=0) {
            other = tileTable[this.coordinates.getX()][this.coordinates.getY()-1];
            if(!other.isEmpty() && !(other.get() instanceof VoidTile) && !west.isConnected(other.get().getEast())) {
                return false;
            }
        }*/
        //Checked
        return true;
    }
    public void rotate(){
        Link tmp= north;
        north =west;
        Link tmp2=east;
        east=tmp;
        west=south;
        south=tmp2;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public void destroy(){
        shipBoard.addPenalty();
    }
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public void setShipBoard(ShipBoardInterface shipBoard) {
        this.shipBoard = shipBoard;
    }

    //METHOD THAT GETS OVERRIDE ONLY BY THE SPECIFIC CLASSES
    public void setCrewType(CrewType crewType) {System.out.println("THIS TILE IS NOT A CABIN"); }
    public void consumeBattery(){System.out.println("THIS TILE IS NOT A BATTERYCOMPONENT"); }
    public boolean removeCrew(){ System.out.println("THIS TILE IS NOT A CABIN");return false; }
    public void removeGood(Goods good){System.out.println("THIS TILE IS NOT A GOOD");}
    public Coverage getCoveredArea(){System.out.println("THIS TILE IS NOT A SHIELD"); return Coverage.NONE;}
    public int addGood(Goods good){System.out.println("THIS TILE IS NOT A CARGO_HOLD"); return 0;}
    public void addBattery(){
        return;
    }
    //
    public ArrayList<Goods> getCargo(){System.out.println("THIS TILE IS NOT A CARGO_HOLD");return null;}
    public int getNumBatteries() {
        System.out.println("THIS TILE IS NOT A BATTERY_COMPONENT");
        return 0;
    }
    public void getStat(){
    }
    public Direction getDirection(){
        return null;
    }

    //negative return in case of single cannon--positive return in case of double cannon
    public int getStrength(){
        return 0;
    }
    public int getEngineStrength(){
        return 0;
    }
    public boolean fillable(){return true;}
    public CrewType getAlienLifeSupportSystemColor(){
        //System.out.println("THIS TILE IS NOT A ALIEN LIFE SUPPORT SYSTEM");
        return CrewType.NotSupportSystem;
    }
    public ArrayList<Coordinates> adjacentLifeSupport(){System.out.println("THIS TILE IS NOT A CABIN"); return new ArrayList<>();}


    public CrewType getCrewType(){System.out.println("THIS TILE IS NOT A CABIN"); return CrewType.NotAcabin;}
    public AlienOptions getAlienability() { return AlienOptions.NO;}
    public void checkAlienability(){System.out.println("THIS TILE IS NOT A CABIN");}
    public int getCrew() {return 0;}

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
    public Tile send(){
        try {
            Tile cloned= (Tile) super.clone();
            cloned.setShipBoard(null);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
