package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public abstract class Cabin extends Tile {
    protected int crew;
    public Cabin(Link north, Link east, Link south, Link west,String imagePath,int rotation, int key) {
        super(north, east, south, west,imagePath,rotation,key);
    }
    //METODO COSTRUTTORE PER IL TESTING
    public Cabin(Link north, Link east, Link south, Link west,String imagePath) {
        super(north, east, south, west,imagePath,0,0);
    }
    @Override
    public int getCrew() {
        return crew;
    }
    public Cabin() { super(); }
}
