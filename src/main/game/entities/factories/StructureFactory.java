package game.entities.factories;

import entityResearch.iStructureResearchObserver;
import game.Player;
import game.entities.DeathNotifier;
import game.entities.EntityId;
import game.entities.EntitySubtypeEnum;
import game.entities.EntityTypeEnum;
import game.entities.factories.exceptions.StructureTypeDoesNotExist;
import game.entities.managers.PlacementManager;
import game.entities.managers.UnitManager;
import game.entities.managers.WorkerManager;
import game.entities.stats.StructureStats;
import game.entities.structures.*;
import game.entities.structures.exceptions.StructureNotFoundException;
import game.gameboard.Location;
import game.semantics.Percentage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class StructureFactory implements iStructureResearchObserver {

    private final static Logger log = LogManager.getLogger(StructureFactory.class);

    private Player player;
    private Map<EntitySubtypeEnum, StructureStats> structureStatistics;
    private PlacementManager placementManager;
    private WorkerManager workerManager;
    private UnitManager unitManager;

    public StructureFactory(Player player, PlacementManager placementManager, WorkerManager workerManager, UnitManager unitManager) {

        this.player = player;
        this.structureStatistics = new HashMap<>();
        this.placementManager = placementManager;
        this.workerManager = workerManager;
        this.unitManager = unitManager;

        try {
            this.structureStatistics.put(EntitySubtypeEnum.CAPITOL, new StructureStats(EntitySubtypeEnum.CAPITOL));
            this.structureStatistics.put(EntitySubtypeEnum.FARM, new StructureStats(EntitySubtypeEnum.FARM));
            this.structureStatistics.put(EntitySubtypeEnum.FORT, new StructureStats(EntitySubtypeEnum.FORT));
            this.structureStatistics.put(EntitySubtypeEnum.MINE, new StructureStats(EntitySubtypeEnum.MINE));
            this.structureStatistics.put(EntitySubtypeEnum.OBSERVE, new StructureStats(EntitySubtypeEnum.OBSERVE));
            this.structureStatistics.put(EntitySubtypeEnum.PLANT, new StructureStats(EntitySubtypeEnum.PLANT));
            this.structureStatistics.put(EntitySubtypeEnum.UNIVERSITY, new StructureStats(EntitySubtypeEnum.UNIVERSITY));

        } catch (StructureNotFoundException e) {
            throw new RuntimeException("Structure Factory could not be instantiated: " + e.getLocalizedMessage());
        }
    }

    public Structure createStructure(EntitySubtypeEnum structureType, int id, int globalId, Location location)
            throws  StructureTypeDoesNotExist {
        switch (structureType) {
            case CAPITOL: {
                EntityId entityId = new EntityId(player.getPlayerId(), EntityTypeEnum.STRUCTURE, EntitySubtypeEnum.CAPITOL, id, globalId);
                DeathNotifier notifier = new DeathNotifier(this.player);
                return new Capitol(structureStatistics.get(EntitySubtypeEnum.CAPITOL), location,
                                    entityId, placementManager,workerManager, notifier);
            }
            case FARM: {
                EntityId entityId = new EntityId(player.getPlayerId(), EntityTypeEnum.STRUCTURE, EntitySubtypeEnum.FARM, id, globalId);
                DeathNotifier notifier = new DeathNotifier(this.player);
                return new Farm(structureStatistics.get(EntitySubtypeEnum.FARM), location,
                                    entityId, placementManager,workerManager, notifier);
            }
            case FORT: {
                EntityId entityId = new EntityId(player.getPlayerId(), EntityTypeEnum.STRUCTURE, EntitySubtypeEnum.FORT, id, globalId);
                DeathNotifier notifier = new DeathNotifier(this.player);
                return new Fort(structureStatistics.get(EntitySubtypeEnum.FORT), location,
                                        entityId, placementManager, workerManager, unitManager, notifier);
            }
            case MINE: {
                EntityId entityId = new EntityId(player.getPlayerId(), EntityTypeEnum.STRUCTURE, EntitySubtypeEnum.MINE, id, globalId);
                DeathNotifier notifier = new DeathNotifier(this.player);
                return new Mine(structureStatistics.get(EntitySubtypeEnum.MINE), location,
                                    entityId, placementManager,workerManager, notifier);
            }
            case OBSERVE: {
                EntityId entityId = new EntityId(player.getPlayerId(), EntityTypeEnum.STRUCTURE, EntitySubtypeEnum.OBSERVE, id, globalId);
                DeathNotifier notifier = new DeathNotifier(this.player);
                return new ObservationTower(structureStatistics.get(EntitySubtypeEnum.OBSERVE), location,
                                                entityId, placementManager, notifier);
            }
            case PLANT: {
                EntityId entityId = new EntityId(player.getPlayerId(), EntityTypeEnum.STRUCTURE, EntitySubtypeEnum.PLANT, id, globalId);
                DeathNotifier notifier = new DeathNotifier(this.player);
                return new PowerPlant(structureStatistics.get(EntitySubtypeEnum.PLANT), location,
                                        entityId, placementManager, workerManager, notifier);
            }
            case UNIVERSITY: {
                EntityId entityId = new EntityId(player.getPlayerId(), EntityTypeEnum.STRUCTURE, EntitySubtypeEnum.UNIVERSITY, id, globalId);
                DeathNotifier notifier = new DeathNotifier(this.player);
                return new University(structureStatistics.get(EntitySubtypeEnum.UNIVERSITY), location,
                                    entityId, placementManager, workerManager, notifier);
            }
            default:
                throw new StructureTypeDoesNotExist();
        }
    }

    public void onVisibilityRadiusIncreased(EntitySubtypeEnum subtype, int increaseAmount) throws StructureTypeDoesNotExist {
        StructureStats stats = this.structureStatistics.get(subtype);
        if (stats == null) throw new StructureTypeDoesNotExist();

        stats.increaseVisibilityRadius(increaseAmount);
    }

    public void onAttackStrengthIncreased(EntitySubtypeEnum subtype, int increaseAmount) throws StructureTypeDoesNotExist {
        StructureStats stats = this.structureStatistics.get(subtype);
        if (stats == null) throw new StructureTypeDoesNotExist();

        stats.increaseAttackStrength(increaseAmount);
    }

    public void onDefenseStrengthIncreased(EntitySubtypeEnum subtype, int increaseAmount) throws StructureTypeDoesNotExist {
        StructureStats stats = this.structureStatistics.get(subtype);
        if (stats == null) throw new StructureTypeDoesNotExist();

        stats.increaseDefenseStrength(increaseAmount);
    }

    public void onArmorStrengthIncreased(EntitySubtypeEnum subtype, int increaseAmount) throws StructureTypeDoesNotExist {
        StructureStats stats = this.structureStatistics.get(subtype);
        if (stats == null) throw new StructureTypeDoesNotExist();

        stats.increaseArmorStrength(increaseAmount);
    }

    public void onHealthIncreased(EntitySubtypeEnum subtype, int increaseAmount) throws StructureTypeDoesNotExist {
        StructureStats stats = this.structureStatistics.get(subtype);
        if (stats == null) throw new StructureTypeDoesNotExist();

        stats.increaseHealth(increaseAmount);
    }

    public void onEfficiencyIncreased(EntitySubtypeEnum subtype, Percentage increasePercentage) throws StructureTypeDoesNotExist {
        StructureStats stats = this.structureStatistics.get(subtype);
        if (stats == null) throw new StructureTypeDoesNotExist();

        stats.increaseEfficiency(increasePercentage);
    }

    // Update worker density of all structures
    public void onWorkerDensityIncreased(int increaseAmount) {
        for (Object o : this.structureStatistics.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            StructureStats stats = (StructureStats) pair.getValue();
            stats.increaseWorkerDensity(increaseAmount);
        }
    }

    // Update worker radius of all structures
    public void onWorkerRadiusIncreased(int increaseAmount) {
        for (Object o : this.structureStatistics.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            StructureStats stats = (StructureStats) pair.getValue();
            stats.increaseWorkerRadius(increaseAmount);
        }
    }

}
