package game.entities.structures;

import game.entities.DeathNotifier;
import game.commands.CommandEnum;
import game.entities.EntityId;
import game.entities.managers.PlacementManager;
import game.entities.managers.WorkerManager;
import game.entities.managers.exceptions.WorkerDoesNotExistException;
import game.entities.managers.exceptions.WorkerLimitExceededException;
import game.entities.managers.exceptions.WorkerTypeDoesNotExist;
import game.entities.stats.StructureStats;
import game.entities.workers.workerTypes.OreGatherer;
import game.entities.workers.workerTypes.Worker;
import game.entities.workers.workerTypes.WorkerTypeEnum;
import game.gameboard.Location;
import game.resources.Resource;
import game.resources.ResourceTypeEnum;
import game.visitors.TransferWorkerVisitor;

import java.util.ArrayList;
import java.util.Iterator;

public class Mine extends Structure {
    private ArrayList<Worker> unassigned;
    private ArrayList<OreGatherer> miner;
    private WorkerManager workerManager;

    public Mine(StructureStats stats, Location location , EntityId entityId , PlacementManager placementManager, WorkerManager workerManager, DeathNotifier notifier){
        super(stats, location, entityId, placementManager, notifier);
        unassigned=new ArrayList<>();
        miner=new ArrayList<>();
        this.workerManager=workerManager;
        addCommand(CommandEnum.ASSIGN_WORKER);
        addCommand(CommandEnum.UNASSIGN_ALL_WORKERS);
        addCommand(CommandEnum.WORKER_MINE);

    }

    public void assignToMiner(Location location)throws WorkerLimitExceededException, WorkerTypeDoesNotExist, WorkerDoesNotExistException {
        if(!unassigned.isEmpty()){
            Iterator<Worker> iterator = unassigned.iterator();
            while(iterator.hasNext()){
                Worker holder = iterator.next();
                iterator.remove();
                TransferWorkerVisitor transferWorkerVisitor = new TransferWorkerVisitor(holder.getId(), WorkerTypeEnum.ORE_GATHERER, location);
                miner.add((OreGatherer) workerManager.accept(transferWorkerVisitor));
                return;
            }
        }
    }

    public void unassignMiner()throws WorkerLimitExceededException, WorkerTypeDoesNotExist, WorkerDoesNotExistException {
        if(!unassigned.isEmpty()){
            Iterator<Worker> iterator = unassigned.iterator();
            while(iterator.hasNext()){
                Worker holder = iterator.next();
                iterator.remove();
                TransferWorkerVisitor transferWorkerVisitor = new TransferWorkerVisitor(holder.getId(), WorkerTypeEnum.ORE_GATHERER, location);
                miner.add((OreGatherer) workerManager.accept(transferWorkerVisitor));
                return;
            }
        }
    }

    public int getTotalWorkers(){
        return unassigned.size() + miner.size();
    }

    public int getUnassignedWorkers(){
        return unassigned.size();
    }

    public int getBusyWorkers(){
        return miner.size();
    }

    public Resource harvest(){
        Iterator<OreGatherer> iterator = miner.iterator();
        Resource resource = new Resource(0, ResourceTypeEnum.ORE);
        while(iterator.hasNext()){
            resource.combine(iterator.next().doProduction());
        }
        return resource;
    }

    public void onTurnEnded() {

    }

}
