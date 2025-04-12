package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;

public class StartingCabin extends Cabin {


    PlayersColor playerColor;
    public StartingCabin(Link nord, Link east, Link south, Link west,int key) {
        super(nord, east, south, west,key);
        crew = 2;
    }

    public void getStat(){
        shipBoard.getCabinsCoordinates().add(this.coordinates);
        shipBoard.addBreakHumanCrew(+2);
    }
    public boolean removeCrew(){
        if(crew > 0) {
            crew--;
            shipBoard.addBreakHumanCrew(-1);
            return true;
        }
        return false;
    }

    @Override
    public void setCoordinates(Coordinates coordinates) {
        playerColor= shipBoard.getPlayer().getPlayerColor();
        super.setCoordinates(coordinates);
    }

    public void destroy(){
        shipBoard.getCabinsCoordinates().remove(this.coordinates);
        shipBoard.addBreakHumanCrew(-crew);
        crew=0;
        super.destroy();
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(playerColor.toString()).append(" Starting Cabin: ");
        s.append(" numCrew:");
        s.append(this.crew).append(" ").append("Human");
        return s.toString()+ super.toString();
    }

}
