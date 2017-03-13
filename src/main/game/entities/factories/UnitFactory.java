package game.entities.factories;

import java.util.HashMap;
import java.util.Map;

import game.entities.EntityId;
import game.entities.EntitySubtypeEnum;
import game.entities.EntityTypeEnum;
import game.entities.factories.exceptions.*;
import game.entities.managers.MovementManager;
import game.entities.stats.UnitStats;
import game.entities.units.Colonist;
import game.entities.units.Explorer;
import game.entities.units.Melee;
import game.entities.units.Ranged;
import game.entities.units.Unit;
import game.entities.units.exceptions.UnitNotFoundException;
import game.gameboard.Gameboard;
import game.gameboard.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnitFactory {

    private final static Logger log = LogManager.getLogger(UnitFactory.class);

    private int playerId;
    private Map<EntitySubtypeEnum, UnitStats> unitStatistics;
    private MovementManager movementManager;

    public UnitFactory(int playerId, Gameboard gb) {
        this.playerId = playerId;
        this.unitStatistics = new HashMap<>();
        this.movementManager = new MovementManager(gb);

        try {
            this.unitStatistics.put(EntitySubtypeEnum.COLONIST, new UnitStats(EntitySubtypeEnum.COLONIST));
            this.unitStatistics.put(EntitySubtypeEnum.EXPLORER, new UnitStats(EntitySubtypeEnum.EXPLORER));
            this.unitStatistics.put(EntitySubtypeEnum.MELEE, new UnitStats(EntitySubtypeEnum.MELEE));
            this.unitStatistics.put(EntitySubtypeEnum.RANGE, new UnitStats(EntitySubtypeEnum.RANGE));
        }   catch(UnitNotFoundException e){
            throw new RuntimeException("Unit factory could not be instantiated: " + e.getLocalizedMessage());
        }
    }
  
    public Unit createUnit(EntitySubtypeEnum unitType, int id, int globalId, Location location)
            throws UnitTypeDoesNotExistException {

        switch(unitType) {
            case COLONIST: {
                EntityId entityId = new EntityId(playerId, EntityTypeEnum.UNIT, EntitySubtypeEnum.COLONIST, id, globalId);
                return new Colonist(unitStatistics.get(unitType), location, entityId, movementManager);
            }
            case EXPLORER: {
                EntityId entityId = new EntityId(playerId, EntityTypeEnum.UNIT, EntitySubtypeEnum.EXPLORER, id, globalId);
                return new Explorer(unitStatistics.get(unitType),location, entityId, movementManager);
            }
            case MELEE: {
                EntityId entityId = new EntityId(playerId, EntityTypeEnum.UNIT, EntitySubtypeEnum.MELEE, id, globalId);
                return new Melee(unitStatistics.get(unitType), location, entityId, movementManager);
            }
            case RANGE: {
                EntityId entityId = new EntityId(playerId, EntityTypeEnum.UNIT, EntitySubtypeEnum.RANGE, id, globalId);
                return new Ranged(unitStatistics.get(unitType), location, entityId, movementManager);
            }
            default: throw new UnitTypeDoesNotExistException();
        }
    }
}
