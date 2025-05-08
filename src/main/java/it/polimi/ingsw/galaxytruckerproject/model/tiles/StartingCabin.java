package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;

public class StartingCabin extends Cabin {


    PlayersColor playerColor;
    public StartingCabin(Link nord, Link east, Link south, Link west,int key) {
        super(nord, east, south, west,key);
        crew = 2;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public StartingCabin(Link nord, Link east, Link south, Link west) {
        super(nord, east, south, west,0);
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
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(playerColor.toString()).append(" Starting Cabin: ");
        s.append(" numCrew:");
        s.append(this.crew).append(" ").append("Human");
        return s.toString()+ super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }
    @Override
    public String toString1(){
        if (getCrew() == 2)
            return " H  " + getNorth() + " H ";
        else if (getCrew() == 1)
            return " H  " + getNorth() + "   ";
        return "    " + getNorth() + "   ";
    }
    @Override
    public String toString2(){
        return " "+getWest()+" SC "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if (getKey() >= 100)
            return "   " + getSouth() +" " + getKey();
        else if (getKey() >= 10 && getKey() < 100)
            return "   " + getSouth() + " " +getKey() + " ";
        else
            return "   " + getSouth() + "  " + getKey() + " ";
    }

}
