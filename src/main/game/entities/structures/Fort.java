package game.entities.structures;

import game.entities.EntityId;
import game.entities.units.Unit;
import game.gameboard.Location;

public class Fort extends Structure {
    //private ArrayList<worker> workers;
    //private ArrayList<worker> unitBuilder;

    public Fort(Location loc , EntityId entityId ){
        super(loc, entityId);
    }

    public void assignToUnitBuilder(){

    }

    public void unassignUnitBuilder(){

    }

    public void addWorker(){

    }

    public void removeWorker(){

    }

    public Unit buildUnit(){
        //TODO Creating Unit Handling
        return null;
    }
}
