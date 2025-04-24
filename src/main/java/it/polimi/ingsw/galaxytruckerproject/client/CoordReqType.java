package it.polimi.ingsw.galaxytruckerproject.client;

public enum CoordReqType {
    CHOOSE_TO_BREAK,// for correctness
    CHOOSE_TO_MAINTAIN,//for destroy
    CHOOSE_BATTERY ,
    CHOOSE_DOUBLE_CANNON,
    CHOOSE_DOUBLE_ENGINE,
    CHOOSE_CREW,
    REMOVE_GOODS;

    @Override
    public String toString() {
        switch (this){
            case CHOOSE_BATTERY -> {return "choose your battery to use";}
            case CHOOSE_TO_MAINTAIN -> {return "choose a tile of the branch you want to keep";}
            case CHOOSE_TO_BREAK -> {return "choose the tiles you want to destroy";}
            case CHOOSE_DOUBLE_CANNON -> {return "choose the double cannons you want to use, and they're batteries";}
            case CHOOSE_DOUBLE_ENGINE -> {return "choose the double engine you want to use, and they're batteries";}
            case CHOOSE_CREW -> {return "choose the crew you want to remove";}
            case REMOVE_GOODS -> {return "choose your goods to remove";}
        }
        return "error in coord request toString";
    }
}
