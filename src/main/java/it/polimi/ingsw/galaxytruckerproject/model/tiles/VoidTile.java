package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public class VoidTile extends Tile {
    public VoidTile(Link north, Link south, Link east, Link west) {
        //THE KEY IS SET AUTOMATICALLY TO -1, SINCE WE'RE NOT GOING TO NEED THEM
        super(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),-1);
    }

    public VoidTile() {
        super(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),-1);
    }

    @Override
    public boolean isCorrect() {
        return true;
    }
    public boolean fillable(){return false;}

    @Override
    public String toString() {
        return "\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }
    @Override
    public String toString1() {
        return "▓▓▓▓▓▓▓▓";
    }
    @Override
    public String toString2(){
        return "▓▓▓▓▓▓▓▓";
    }
    @Override
    public String toString3(){
        return "▓▓▓▓▓▓▓▓";
    }

    @Override
    public String toStringData() {
        return "VT";
    }

    @Override
    public void tileLoader(String[] attributes) {
    }
}