package game.entities.structures;

import game.entities.Entity;
import game.entities.EntityId;
import game.entities.EntitySubtypeEnum;
import game.entities.HealthPercentage;
import game.entities.stats.StructureStats;
import game.gameboard.Location;
import game.semantics.Percentage;

public abstract class Structure extends Entity {
    protected StructureStats stats;
    public Structure(StructureStats stats, Location location , EntityId entityId ){
        super(location, entityId);
        this.stats = stats;
        this.health = stats.getHealth();
        this.healthPercent = new HealthPercentage();
        standby();
    }

    public EntitySubtypeEnum getType() {
    	return (EntitySubtypeEnum) getEntityId().getSubTypeId();
    }
    
    public StructureStats getStats() {
    	return stats;
    }

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
    
}
