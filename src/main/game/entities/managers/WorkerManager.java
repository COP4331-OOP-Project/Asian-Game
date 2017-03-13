package game.entities.managers;

import game.entities.EntityId;
import game.entities.factories.WorkerFactory;
import game.entities.managers.exceptions.WorkerDoesNotExistException;
import game.entities.managers.exceptions.WorkerLimitExceededException;
import game.entities.managers.exceptions.WorkerTypeDoesNotExist;
import game.entities.workers.workerTypes.Worker;
import game.entities.workers.workerTypes.WorkerTypeEnum;
import game.gameboard.Location;
import game.iTurnObservable;
import game.iTurnObserver;
import game.techTree.nodeTypes.WorkerDensityResearchNode;
import game.workerResearch.iWorkerResearchObservable;
import game.workerResearch.iWorkerResearchObserver;
import game.semantics.Percentage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class WorkerManager implements iWorkerResearchObservable, iTurnObserver, iTurnObservable {

    private final static Logger log = LogManager.getLogger(WorkerManager.class);

    private ArrayList<Worker> workers;
    private WorkerIdManager workerIdManager;
    private List<iWorkerResearchObserver> observers;
    private ArrayList<iTurnObserver> turnObservers;

    public WorkerManager(int playerId) {
        this.observers = new ArrayList<>();
        this.turnObservers = new ArrayList<>();
        WorkerFactory workerFactory = new WorkerFactory(playerId);
        this.attach(workerFactory);
        this.workerIdManager = new WorkerIdManager(workerFactory);
        this.workers = new ArrayList<>();
    }


    public Worker addWorker(WorkerTypeEnum workerType, Location location) throws WorkerLimitExceededException, WorkerTypeDoesNotExist {
        Worker w = this.workerIdManager.createWorker(workerType, location);
        this.workers.add(w);
        this.turnObservers.add(w);
        return w;
    }

    public void removeWorker(EntityId id) throws WorkerDoesNotExistException {
        boolean removed = false;
        for (Worker w : this.workers) {
            if (w.getId() == id) {
                this.workers.remove(w);
                this.turnObservers.remove(w);
                removed = true;
            }
        }

        if (!removed) throw new WorkerDoesNotExistException("Worker does not exist in WorkerManager");

        this.workerIdManager.removeWorker(id);
    }

    public Worker getWorker(EntityId id) {
        for (Worker w : this.workers) {
            if (w.getId() == id) {
                return w;
            }
        }

        return null;
    }


    public void attach(iWorkerResearchObserver observer) {
        this.observers.add(observer);
    }

    public ArrayList<Worker> getWorkers() {
        return this.workers;
    }

    public void increaseProductionRateByPercentage(Percentage productionRateIncrease, WorkerTypeEnum workerType) {
        for (iWorkerResearchObserver observer : this.observers) {
            try {
                observer.onProductionRateIncreased(productionRateIncrease, workerType);
            } catch (WorkerTypeDoesNotExist e) {
                log.error("Could not increase production rate because worker type " + workerType + " does not exist");
            }
        }
    }

    public void changeProductionRateByAmount(int changeAmount, WorkerTypeEnum workerType) throws WorkerTypeDoesNotExist {
        for (iWorkerResearchObserver observer : this.observers) {
            try {
                observer.onChangeProductionRateByAmount(changeAmount, workerType);
            } catch (WorkerTypeDoesNotExist e) {
                log.error("Could not increase production rate because worker type " + workerType + " does not exist");
            }
        }
    }

    public void attach(iTurnObserver observer) {
        this.turnObservers.add(observer);
    }

    public void endTurn() {
        for (iTurnObserver observer : this.turnObservers) {
            observer.onTurnEnded();
        }
    }

    public void onTurnEnded() {
        this.endTurn();
    }


}
