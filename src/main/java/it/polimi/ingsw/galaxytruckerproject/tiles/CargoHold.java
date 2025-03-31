package it.polimi.ingsw.galaxytruckerproject.tiles;

<<<<<<< Updated upstream:src/main/java/it/polimi/ingsw/galaxytruckerproject/tiles/CargoHold.java
import it.polimi.ingsw.galaxytruckerproject.Goods;
=======
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
>>>>>>> Stashed changes:src/main/java/it/polimi/ingsw/galaxytruckerproject/model/tiles/CargoHold.java

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class CargoHold extends Tile{
    final int totSpaces;
    ArrayList<Goods> cargo;

    public CargoHold(int totSpaces, Link north, Link east, Link south, Link west) {
        super(north, east, south, west);
        this.totSpaces = totSpaces;
        this.cargo = new ArrayList<>();
        // It makes more sense to use an ArrayList since they are much easier to manage in Java.
        // Instead of dealing with null values, we can simply use an ArrayList.
        // and to check if there are goods and how many, we just get the size of the ArrayList and compare it with totSpaces.
    }

    @Override
    public String toString() {
        for(Goods g : cargo)
            g.toString();
        return cargo.stream()
                .map(Goods::toString)
                .collect(Collectors.joining()) +" "+super.toString();
    }

    public void getStat(){
        shipBoard.getCargoHoldCoordinates().add(this.coordinates);
    }
    public boolean addGood(Goods good){
        if(cargo.size() == totSpaces){
            System.out.println("\nCargoHold is full");
            return false;
        }
        else {
            cargo.add(good);
            return true;
        }
    }

    public void removeGood(Goods good){
        if(cargo.isEmpty()){
            System.out.println("CargoHold is already empty");
        }
        else
            cargo.removeIf(g -> g.getColor().equals(good.getColor()));
    }

    public void destroy(){
        //I could have only used the Destroyed flag set in the superclass, but for clarity,
        //I also emptied the goods array contained within it.
        cargo.clear();

        super.destroy();
    }

    public ArrayList<Goods> getCargo() {
        return cargo;
    }

}
