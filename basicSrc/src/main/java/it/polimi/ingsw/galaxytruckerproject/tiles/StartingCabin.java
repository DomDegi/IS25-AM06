package it.polimi.ingsw.galaxytruckerproject.tiles;

import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;

public class StartingCabin extends Cabin {


    PlayersColor playerColor;
    public StartingCabin(Link nord, Link east, Link south, Link west, PlayersColor playerColor) {
        super(nord, east, south, west);
        crew = 2;
        playerColor = shipBoard.player.getPlayerColor();
    }

    public void getStat(){shipBoard.addBreakHumanCrew(+2);}
    public void removeCrew(){
        crew--;
        shipBoard.addBreakHumanCrew(-1);
    }


    public void destroy(){
        shipBoard.addBreakHumanCrew(-crew);
        crew=0;
    }


}
