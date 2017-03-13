package game.entities.structures;

import game.entities.EntityId;
import game.entities.PowerState;
import game.entities.stats.StructureStats;
import game.entities.units.Unit;
import game.entities.workers.workerTypes.SoldierGenerator;
import game.gameboard.Location;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Fort extends Structure {
    private Queue<SoldierGenerator> unassigned;
    private Queue<SoldierGenerator> unitBuilder;

    public Fort(StructureStats stats, Location location , EntityId entityId ){
        super(stats, location, entityId);
        unassigned=new LinkedList<>();
        unitBuilder=new LinkedList<>();
    }

    public void assignToUnitBuilder(Location location){
        unassigned.peek().setLocation(location);
        unitBuilder.add(unassigned.poll());
    }

    public void unassignUnitBuilder(){
        Iterator<SoldierGenerator> iterator = unitBuilder.iterator();
        SoldierGenerator holder;
        while(iterator.hasNext()){
            holder=iterator.next();
            if(location.equals(holder.getLocation())) {
                holder.setLocation(location);
                unassigned.add(holder);
                iterator.remove();
                return;
            }
        }
    }

    public void addWorker(SoldierGenerator worker){
        unassigned.add(worker);
    }

    public void removeWorker(){
        unassigned.remove();
    }

    public int getTotalWorkers(){
        return unassigned.size() + unitBuilder.size();
    }

    public int getUnassignedWorkers(){
        return unassigned.size();
    }

    public int getBusyWorkers(){
        return unitBuilder.size();
    }

    public void combatState(){
        setPowerState(PowerState.COMBAT);
    }

    public Unit buildUnit(){
        //TODO Creating Unit Handling
        return null;
    }

    public void onTurnEnded() {

    }

}
