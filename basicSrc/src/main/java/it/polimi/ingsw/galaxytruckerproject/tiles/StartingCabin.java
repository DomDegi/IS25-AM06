package it.polimi.ingsw.galaxytruckerproject.tiles;

import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;

public class StartingCabin extends Cabin {

    int crew;
    PlayersColor playerColor;
    public StartingCabin(){
        super();
        crew = 2;
        playerColor = shipBoard.player.getPlayerColor();
    }

    public void getStat(){shipBoard.addBreakCrew(+2);}
    public void destroy(){
        shipBoard.addBreakCrew(-2);
        super.destroy();
    }


}
