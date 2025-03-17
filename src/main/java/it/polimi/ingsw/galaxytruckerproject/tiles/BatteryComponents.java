package it.polimi.ingsw.galaxytruckerproject.tiles;

public class BatteryComponents extends Tile {
    int batteryCells;

    public BatteryComponents(Link nord, Link south, Link east, Link west, int numCells) {
        super(nord, south, west, east);
        this.batteryCells = numCells;
    }

    public int getNumBatteries() {
        return this.batteryCells;
    }

    public void consumeBattery() {
        if (this.batteryCells > 0) {
            this.batteryCells--;
            shipBoard.addBreakBatteries(-1);
            if (this.batteryCells == 0) {
                shipBoard.getBatteryCoordinates().remove(this.coordinates);
            }
        } else {
            System.out.println("Run out of batteries in this Tile");
        }
    }

    public void getStat() {
        shipBoard.addBreakBatteries(batteryCells);
        shipBoard.getBatteryCoordinates().add(this.coordinates);
    }

    public void destroy() {
        shipBoard.addBreakBatteries(-batteryCells);
        super.destroy();
    }
}
