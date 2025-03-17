package it.polimi.ingsw.galaxytruckerproject.tiles;

import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;

public class StartingCabin extends Cabin {
    PlayersColor playerColor;
    public StartingCabin(Link nord, Link east, Link south, Link west) {
        super(nord, east, south, west);
        crew = 2;
        playerColor = shipBoard.player.getPlayerColor();
    }

    public void getStat(){shipBoard.addBreakHumanCrew(+2);}
    public boolean removeCrew(){
        if(crew > 0) {
            crew--;
            shipBoard.addBreakHumanCrew(-1);
            return true;
        }
        return false;
    }


    public void destroy(){
        shipBoard.addBreakHumanCrew(-crew);
        crew=0;
    }


}
