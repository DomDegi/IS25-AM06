package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

public class TileLoader {

    private TileLoader() {}

    public static Tile load(String line) {
        String[] lineSplit = line.split(" ");
        switch(lineSplit[0]) {
            case "AL" -> {
                AlienLifeSupportsSystem al = new AlienLifeSupportsSystem();
                al.tileLoader(lineSplit);
                return al;
            }
            case "BC" -> {
                BatteryComponents bc = new BatteryComponents();
                bc.tileLoader(lineSplit);
                return bc;
            }
            case "CB" -> {
                CargoBlue cb = new CargoBlue();
                cb.tileLoader(lineSplit);
                return cb;
            }
            case "CR" -> {
                CargoRed cr = new CargoRed();
                cr.tileLoader(lineSplit);
                return cr;
            }
            case "DC"  -> {
                DoubleCannon dc = new DoubleCannon();
                dc.tileLoader(lineSplit);
                return dc;
            }
            case "DE" -> {
                DoubleEngine de = new DoubleEngine();
                de.tileLoader(lineSplit);
                return de;
            }
            case "EC"  -> {
                EquipCabin ec = new EquipCabin();
                ec.tileLoader(lineSplit);
                return ec;
            }
            case "SH" -> {
                Shields sh = new Shields();
                sh.tileLoader(lineSplit);
                return sh;
            }
            case "SC" -> {
                SingleCannon sc = new SingleCannon();
                sc.tileLoader(lineSplit);
                return sc;
            }
            case "SE"  -> {
                SingleEngine se = new SingleEngine();
                se.tileLoader(lineSplit);
                return se;
            }
            case "ST" -> {
                StartingCabin st =  new StartingCabin();
                st.tileLoader(lineSplit);
                return st;
            }
            case "PP" -> {
                Pipe pp = new Pipe();
                pp.tileLoader(lineSplit);
                return pp;
            }
            case "VT" -> {
                return new VoidTile();
            }
        }
        return null;
    }
}
