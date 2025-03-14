package it.polimi.ingsw.galaxytruckerproject.tiles;

public class VoidTile extends Tile {
    public VoidTile(Link north, Link south, Link east, Link west) {
        super(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));
    }

    @Override
    public boolean isCorrect() {
        return true;
    }
}

