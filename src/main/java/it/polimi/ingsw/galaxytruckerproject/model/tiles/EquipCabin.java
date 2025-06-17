package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType.HUMAN;

/**
 * Represents a cabin tile capable of hosting crew members, including aliens.
 * It supports human crew, brown aliens, and purple aliens depending on adjacent life support systems.
 */
public class EquipCabin extends Cabin {

    /**
     * Indicates which types of aliens the cabin can host (none, brown, purple, or both).
     */
    private AlienOptions alienability = AlienOptions.NO;

    /**
     * The current crew type assigned to this cabin.
     */
    private CrewType crewType;

    /**
     * Temporary crew count used during tile loading and deserialization.
     */
    private int crewToLoad;

    /**
     * Full constructor for standard game usage.
     *
     * @param north     connector on the north side
     * @param east      connector on the east side
     * @param south     connector on the south side
     * @param west      connector on the west side
     * @param imagePath path to the tile image
     * @param rotation  rotation of the tile
     * @param key       unique identifier for this tile
     */
    public EquipCabin(Link north, Link east, Link south, Link west, String imagePath, int rotation, int key) {
        super(north, east, south, west, imagePath, rotation, key);
        alienability = null;
    }

    /**
     * Constructor used for testing without image or metadata.
     */
    public EquipCabin(Link north, Link east, Link south, Link west) {
        super(north, east, south, west, null, 0, 0);
        alienability = null;
    }

    /**
     * @return the crew type currently occupying the cabin
     */
    public CrewType getCrewType() {
        return this.crewType;
    }

    /**
     * Finds coordinates of adjacent alien life support systems (if present).
     *
     * @return list of coordinates adjacent to this cabin
     */
    public ArrayList<Coordinates> adjacentLifeSupport() {
        ArrayList<Coordinates> adjacentLifeSupport = new ArrayList<>();
        if (coordinates.getX() - 1 >= 0 && shipBoard.getTilesTable()[coordinates.getX() - 1][coordinates.getY()].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[coordinates.getX() - 1][coordinates.getY()].get().getCoordinates());
        if (coordinates.getX() + 1 <= 4 && shipBoard.getTilesTable()[coordinates.getX() + 1][coordinates.getY()].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[coordinates.getX() + 1][coordinates.getY()].get().getCoordinates());
        if (coordinates.getY() - 1 >= 0 && shipBoard.getTilesTable()[coordinates.getX()][coordinates.getY() - 1].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[coordinates.getX()][coordinates.getY() - 1].get().getCoordinates());
        if (coordinates.getY() + 1 <= 6 && shipBoard.getTilesTable()[coordinates.getX()][coordinates.getY() + 1].isPresent())
            adjacentLifeSupport.add(shipBoard.getTilesTable()[coordinates.getX()][coordinates.getY() + 1].get().getCoordinates());
        return adjacentLifeSupport;
    }

    /**
     * Updates the alienability of the cabin based on adjacent life support systems.
     */
    public void checkAlienability() {
        AlienOptions tempAlien = this.alienability;
        alienability = AlienOptions.NO;
        for (Coordinates c : adjacentLifeSupport()) {
            CrewType supportType = shipBoard.getTile(c).getAlienLifeSupportSystemColor();
            if (supportType == CrewType.BROWN) {
                alienability = (alienability == AlienOptions.PURPLE) ? AlienOptions.BOTH : AlienOptions.BROWN;
            } else if (supportType == CrewType.PURPLE) {
                alienability = (alienability == AlienOptions.BROWN) ? AlienOptions.BOTH : AlienOptions.PURPLE;
            }
        }
        if (tempAlien != null && alienability != tempAlien) {
            resetAlien(alienability, tempAlien);
        }
    }

    /**
     * Removes the alien crew if they are no longer supported by adjacent systems.
     *
     * @param current the new alienability
     * @param previous the previous alienability
     */
    public void resetAlien(AlienOptions current, AlienOptions previous) {
        if (current.equals(AlienOptions.BOTH)) return;
        if ((current != AlienOptions.PURPLE && crewType == CrewType.PURPLE) ||
                (current != AlienOptions.BROWN && crewType == CrewType.BROWN)) {
            this.removeCrew();
        }
    }

    /**
     * Sets the crew type for this cabin, validating against alien support.
     *
     * @param crewType the crew type to be assigned
     */
    public void setCrewType(CrewType crewType) {
        switch (crewType) {
            case HUMAN -> {
                this.crewType = HUMAN;
                this.crew = 2;
                shipBoard.addBreakHumanCrew(2);
            }
            case PURPLE -> {
                if (alienability == AlienOptions.PURPLE || alienability == AlienOptions.BOTH) {
                    this.crewType = CrewType.PURPLE;
                    this.crew = 1;
                    shipBoard.addBreakPurpleAliens(true);
                } else {
                    System.out.println("CAN'T FILL THIS CABIN WITH A PURPLE ALIEN");
                }
            }
            case BROWN -> {
                if (alienability == AlienOptions.BROWN || alienability == AlienOptions.BOTH) {
                    this.crewType = CrewType.BROWN;
                    this.crew = 1;
                    shipBoard.addBreakBrownAliens(true);
                } else {
                    System.out.println("CAN'T FILL THIS CABIN WITH A BROWN ALIEN");
                }
            }
        }
    }

    /**
     * Updates ship statistics and loads crew from deserialization data.
     */
    public void getStat() {
        checkAlienability();
        if (!shipBoard.getCabinsCoordinates().contains(coordinates))
            shipBoard.getCabinsCoordinates().add(coordinates);
        if (crewToLoad > 0) {
            this.crew = crewToLoad;
            crewToLoad = 0;
            switch (crewType) {
                case PURPLE -> shipBoard.addBreakPurpleAliens(true);
                case BROWN -> shipBoard.addBreakBrownAliens(true);
                case HUMAN -> shipBoard.addBreakHumanCrew(crew);
            }
        }
    }

    /**
     * Removes one unit of crew from the cabin.
     *
     * @return true if crew was removed, false if cabin was already empty
     */
    public boolean removeCrew() {
        if (this.crew > 0) {
            this.crew--;
            switch (crewType) {
                case HUMAN -> shipBoard.addBreakHumanCrew(-1);
                case PURPLE -> shipBoard.addBreakPurpleAliens(false);
                case BROWN -> shipBoard.addBreakBrownAliens(false);
            }
            if (this.crew == 0)
                shipBoard.getCabinsCoordinates().remove(this.coordinates);
            return true;
        }
        System.out.println("THIS CABIN IS EMPTY");
        return false;
    }

    /**
     * Removes all crew from this cabin and updates ship stats.
     */
    @Override
    public void destroy() {
        super.destroy();
        if (this.crew > 0) {
            switch (crewType) {
                case HUMAN -> shipBoard.addBreakHumanCrew(-crew);
                case PURPLE -> shipBoard.addBreakPurpleAliens(false);
                case BROWN -> shipBoard.addBreakBrownAliens(false);
            }
            this.crew = 0;
            shipBoard.getCabinsCoordinates().remove(this.coordinates);
        }
    }

    @Override
    public String toString() {
        return "EquipCabin " + crewType + " " + crew + " " + super.toString() + super.toString() +
                "\n┌────────┐\n│" + toString1() + "│\n│" + toString2() + "│\n│" + toString3() + "│\n└────────┘";
    }

    @Override
    public String toString1() {
        if(crewType == null){
            return " /  " + getNorth() + " / ";
        }
        return switch (crewType) {
            case HUMAN -> (crew == 2) ? " H  " + getNorth() + " H " : " H  " + getNorth() + "   ";
            case PURPLE -> " P  " + getNorth() + "   ";
            case BROWN -> " B  " + getNorth() + "   ";
            default -> " /  " + getNorth() + " / ";
        };
    }

    @Override
    public String toString2() {
        return " " + getWest() + " EC " + getEast() + " ";
    }

    @Override
    public String toString3() {
        if (getKey() >= 100) return "   " + getSouth() + " " + getKey();
        if (getKey() >= 10) return "   " + getSouth() + " " + getKey() + " ";
        return "   " + getSouth() + "  " + getKey() + " ";
    }

    /**
     * Returns the alienability status of this cabin.
     */
    @Override
    public AlienOptions getAlienability() {
        return alienability;
    }

    /**
     * Used in tests to manually set crew type and crew amount.
     */
    public void setCrewTypeOfTestTile(CrewType type) {
        this.crewType = type;
        this.alienability = AlienOptions.BOTH;
        this.crew = (type == HUMAN) ? 2 : 1;
    }

    @Override
    public String toStringData() {
        String image;
        image = Objects.requireNonNullElse(imagePath, "N");
        return "EC " + key + " "+ image + " " + north + " " + east + " " + south + " " + west + " " + crew + " " +
                (crewType != null ? crewType : "N");
    }

    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.crewToLoad = Integer.parseInt(attributes[7]);
        if (!attributes[8].equals("N")) {
            this.crewType = CrewType.fromString(attributes[8]);
        }
        if (crewType != CrewType.HUMAN && crewToLoad > 1) {
            System.out.println("This equip cabin has something wrong going on");
            throw new IllegalArgumentException();
        }
    }

    /**
     * Default constructor for deserialization.
     */
    public EquipCabin() {}
}
