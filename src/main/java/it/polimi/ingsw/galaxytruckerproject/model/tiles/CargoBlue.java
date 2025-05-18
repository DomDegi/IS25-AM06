package it.polimi.ingsw.galaxytruckerproject.model.tiles;


public class CargoBlue extends CargoHold {

    public CargoBlue(int totSpaces, Link north, Link east, Link west, Link south,String imagePath, int key) {
        super(totSpaces, north, east, west, south,imagePath, key);
        hazard = false;
    }
    //CONSTRUCTOR METHOD FOR THE TESTING
    public CargoBlue(int totSpaces, Link north, Link east, Link west, Link south,  String imagePath) {
        super(totSpaces, north, east, west, south,imagePath,0);
        hazard = false;
    }
    @Override
    public String toString() {
        return "Cargo Blue" +super.toString()+"\n┌────────┐\n│"+toString1()+"│\n│"+toString2()+"│\n│"+toString3()+"│\n└────────┘";
    }
    @Override
    public String toString1(){
        if(getTotSpaces()==0) {
            return "    " + getNorth() + "   ";
        } else if(getTotSpaces()==1) {
            if (getCargo().size() == 1)
                return " "+getCargo().getFirst().toString() +"  "+ getNorth() + "   ";
            else
                return " ░  " + getNorth() + "   ";
        } else {
            if (getCargo().size() == 1)
                return " "+getCargo().getFirst().toString() +"  "+ getNorth() + " ░ ";
            else if (getCargo().size() == 2 || getCargo().size() == 3)
                return " "+getCargo().getFirst().toString() +"  "+ getNorth() +" "+ getCargo().get(1).toString()+" ";
            else
                return " ░  " + getNorth() + " ░ ";
        }
    }
    @Override
    public String toString2(){
        return " "+getWest()+" CB "+getEast()+" ";
    }
    @Override
    public String toString3(){
        if(getTotSpaces()==3) {
            if (getKey()>=100) {
                if (getCargo().size() == 3)
                    return " "+getCargo().get(2).toString()+" " + getSouth() + " " +getKey();
                else
                    return " ░ " + getSouth() +" " + getKey();
            }else if(getKey()>=10&&getKey()<100) {
                if (getCargo().size() == 3)
                    return " "+getCargo().get(2).toString()+" " + getSouth() +" " + getKey()+ " ";
                else
                    return " ░ " + getSouth() + " " +getKey()+ " ";
            } else {
                if (getCargo().size() == 3)
                    return " "+getCargo().get(2).toString()+" " + getSouth() +"  "+ getKey()+ " ";
                else
                    return " ░ " + getSouth() +"  " + getKey()+ " ";
            }
        }else {
            if (getKey() >= 100)
                return "   " + getSouth() +" " + getKey();
            else if (getKey() >= 10 && getKey() < 100)
                return "   " + getSouth() +" " + getKey() + " ";
            else
                return "   " + getSouth() + "  " + getKey() + " ";
        }
    }

    @Override
    public String toStringData() {
        StringBuilder sb = new StringBuilder();
        sb.append("CB ").append(key).append(" ").append(north.toString())
                .append(" ").append(east.toString()).append(" ").append(south.toString()).
                append(" ").append(west.toString()).append(" ").append(totSpaces).append(" ");
        for(int i = 0; i < totSpaces; i++) {
            if (i < cargo.size())
                sb.append(cargo.get(i)).append(" ");
            else {
                sb.append("N ");
            }
        }
        return sb.toString();
    }

    public CargoBlue() {
        super();
    }

    @Override
    public void tileLoader(String[] attributes) {
        super.tileLoader(attributes);
        this.hazard = false;
    }
}
