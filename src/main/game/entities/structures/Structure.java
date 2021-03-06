package game.entities.structures;

import game.entities.*;
import java.util.ArrayList;

import game.commands.CommandEnum;
import game.entities.Entity;
import game.entities.EntityId;
import game.entities.EntitySubtypeEnum;
import game.entities.HealthPercentage;
import game.entities.managers.PlacementManager;
import game.entities.stats.StructureStats;
import game.gameboard.Location;
import game.gameboard.iTileObserver;
import game.iTurnObserver;
import game.semantics.Percentage;
import game.visitors.AddStructureVisitor;

public abstract class Structure extends Entity implements iTurnObserver, iTileObserver {
    protected StructureStats stats;

    public Structure(StructureStats stats, Location location , EntityId entityId ,
                     PlacementManager placementManager, DeathNotifier notifier) {

        super(entityId, placementManager, notifier);
        this.stats = stats;
        this.health = stats.getHealth();
        this.healthPercent = new HealthPercentage();
        this.location=location;

        AddStructureVisitor addStructureVisitor = new AddStructureVisitor(this,this.location);
        placementManager.accept(addStructureVisitor);

        standby();
        addCommand(CommandEnum.DEFEND);
    }

    public Location getLocation(){return location;}
    public int getLocationX(){return location.getX();}
    public int getLocationY(){return location.getY();}
    public int getVisibilityRadius(){return this.stats.getVisibilityRadius();}

    public EntitySubtypeEnum getType() {
        return (EntitySubtypeEnum) getEntityId().getSubTypeId();
    }

    public StructureStats getStats() {
        return stats;
    }

    /* stats accessors */
    public int getInfluence() { return stats.getInfluence(); }

    public void increaseVisibilityRadius(int increaseAmount) {
        this.stats.increaseVisibilityRadius(increaseAmount);
    }

    public void increaseAttackStrength(int increaseAmount) {
        this.stats.increaseAttackStrength(increaseAmount);
    }

    public void increaseDefensiveStrength(int increaseAmount) {
        this.stats.increaseDefenseStrength(increaseAmount);
    }

    public void increaseArmorStrength(int increaseAmount) {
        this.stats.increaseArmorStrength(increaseAmount);
    }

    public void increaseHealth(int increaseAmount) {
        this.stats.increaseHealth(increaseAmount);
    }

    public void increaseEfficiency(Percentage increasePercentage) {
        this.stats.increaseEfficiency(increasePercentage);
    }

    /* Stat-adjusted damage taking*/
    @Override
    public void takeDamage(double damage){
        double armor = stats.getArmor();
        double damageX = 10/(10+armor);
        double adjDamage = damage * damageX;

        this.health -= adjDamage;
        this.healthPercent.updateHealthPercentage((double)this.health);

        if (this.health <= 0)
            this.notifer.publishEntityDeath(this.entityId.getTypeId(), (EntitySubtypeEnum) this.entityId.getSubTypeId(), this.entityId, this.location);
    }

    /* iTileObserver */

}
