package it.polimi.ingsw.galaxytruckerproject.tiles;

import java.util.Optional;

import java.util.ArrayList;

public abstract class Tile {
    protected Link north;
    protected Link east;
    protected Link south;
    protected Link west;
    protected Coordinates coordinates;
    protected ShipBoard shipBoard;

    public Tile(Link north, Link east, Link south, Link west){
        this.north =north;
        this.east=east;
        this.south=south;
        this.west=west;

    }

    @Override
    public String toString() {
        return "Tile north"+north.toString() + " east:" + east.toString() + " south" + south.toString() + " west" + west.toString()+"\n"
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
            if(!other.isEmpty() && !(other.get() instanceof VoidTile) && !east.isConnected(other.get().getWest())) {
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
        south=east;
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


    //METHOD THAT GETS OVERRIDE ONLY BY THE SPECIFIC CLASSES
    public void setCrewType(CrewType crewType) {System.out.println("THIS TILE IS NOT A CABIN"); }
    public void consumeBattery(){System.out.println("THIS TILE IS NOT A BATTERYCOMPONENT"); }
    public void removeCrew(){
        System.out.println("THIS TILE IS NOT A CABIN");
    };
    public void removeGood(Goods good){System.out.println("THIS TILE IS NOT A GOOD");}
    public Coverage getCoveredArea(){System.out.println("THIS TILE IS NOT A SHIELD"); return Coverage.NONE;}
    public ArrayList<Goods> getCargo(){System.out.println("THIS TILE IS NOT A CARGOHOLD"); return null;}
    public boolean Pleaceble(){return true;};

    public int getStrength(){
        System.out.println("THIS TILE IS NOT A DOUBLE CANNON");
        return 0;
    }
    public boolean fillable(){
        return true;
    }
}
