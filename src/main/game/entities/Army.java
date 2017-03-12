package game.entities;

import game.entities.units.Unit;
import game.gameboard.Location;
import game.visitors.AddArmyVisitor;
import game.visitors.AddRallyPointVisitor;
import game.visitors.RemoveEntityVisitor;

public class Army extends Entity{
    private BattleGroup battleGroup;
    private Reinforcement reinforcement;
    private RallyPoint rallyPoint;

    //TODO Know when to add battlegroup to tile. Shouldn't show up unless units in battlegroup
    public Army(EntityId entityId, RallyPoint rp){
        super(entityId);
        battleGroup = new BattleGroup(rp.getLocation(), entityId);
        reinforcement = new Reinforcement();
        rallyPoint=rp;
        AddRallyPointVisitor addRallyPointVisitor = new AddRallyPointVisitor(rallyPoint,rp.getLocation());
        movementManager.accept(addRallyPointVisitor);
        updateArmy();
    }

    public void moveRallyPoint(Location loc){
        //Add to new tile
        AddRallyPointVisitor addRallyPointVisitor = new AddRallyPointVisitor(rallyPoint,loc);
        movementManager.accept(addRallyPointVisitor);
        //Remove from old tile
        RemoveEntityVisitor removeEntityVisitor = new RemoveEntityVisitor(rallyPoint.getEntityId(),rallyPoint.getLocation());
        movementManager.accept(removeEntityVisitor);
        rallyPoint.setLocation(loc);
    }

    public void updateArmy(){
        while(reinforcement.onLocation(battleGroup.getLocation())){
            Unit unitToAdd = reinforcement.getOnLocationUnit(battleGroup.getLocation());
            //Remove tile reference to unit
            RemoveEntityVisitor removeEntityVisitor = new RemoveEntityVisitor(unitToAdd.getEntityId(), unitToAdd.getLocation());
            movementManager.accept(removeEntityVisitor);

            battleGroup.addUnit(unitToAdd);
        }
    }

    public void moveBattleGroup(Location loc){
        //Move to Tile
        AddArmyVisitor addArmyVisitor = new AddArmyVisitor(battleGroup, loc);
        movementManager.accept(addArmyVisitor);
        //Delete old Tile reference
        RemoveEntityVisitor removeEntityVisitor = new RemoveEntityVisitor(getEntityId(),battleGroup.getLocation());
        movementManager.accept(removeEntityVisitor);
        //Update battlegroup and army location
        battleGroup.setLocation(loc);
    }

    public Location getLocation(){return battleGroup.getLocation();}
    public int getLocationX(){return getLocation().getX();}
    public int getLocationY(){return getLocation().getY();}

    public Location getRallyPointLocation(){return rallyPoint.getLocation();}

    public double getCurrentHealth(){
        return 0;
    }
    public HealthPercentage getHealthPercentage(){
        return null;
    }
    public void takeDamage(double damage){

    }
    public void heal(double healing){

    }
    public void decommissionEntity(){

    }

}
