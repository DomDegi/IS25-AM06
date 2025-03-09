package it.polimi.ingsw.galaxytruckerproject.tiles;

import java.util.ArrayList;

public abstract class CargoHold extends Tile{
    final int totSpaces;
    ArrayList<Goods> cargo;

    public CargoHold(int totSpaces, Link nord, Link east, Link west, Link south) {
        super();
        this.totSpaces = totSpaces;
        this.cargo = new ArrayList<Goods>();
        // It makes more sense to use an ArrayList since they are much easier to manage in Java.
        // Instead of dealing with null values, we can simply use an ArrayList,
        // and to check if there are goods and how many, we just get the size of the ArrayList and compare it with totSpaces.
    }
    public void addGood(Goods good){
        if(cargo.size() == totSpaces){
            System.out.println("CargoHold is full");
        }
        else
         cargo.add(good);

    }

    public void removeGood(Goods good){
        if(cargo.isEmpty()){
            System.out.println("CargoHold is already empty");
        }
        else
            cargo.remove(good);
    }

    public void destroy(){
        //I could have only used the Destroyed flag set in the superclass, but for clarity,
        //I also emptied the goods array contained within it.
        cargo.clear();
        super.destroy();
    }
}
