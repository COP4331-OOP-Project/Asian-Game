package game.entityTypeResearch.nodeTypes.structureAdvancements;

import entityResearch.iStructureResearchVisitor;
import entityResearch.researchVisitors.StructureAttackStrengthResearchVisitor;
import game.entities.EntitySubtypeEnum;
import game.entities.factories.EntityTypeDoesNotExistException;
import game.entities.factories.exceptions.StructureTypeDoesNotExist;
import game.entities.managers.StructureManager;
import game.entityTypeResearch.nodeTypes.EntityTypeAdvancementNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StructureAttackAdvancementNode extends EntityTypeAdvancementNode {

    private final static Logger log = LogManager.getLogger(StructureAttackAdvancementNode.class);

    private boolean isResearchCompleted;
    private EntitySubtypeEnum structureType;
    private int increaseAmount;
    private StructureManager structureManager;

    public StructureAttackAdvancementNode(StructureManager structureManager, EntitySubtypeEnum structureType, int increaseAmount, String name, String description) {
        super(name, description);
        this.structureManager = structureManager;
        this.structureType = structureType;
        this.increaseAmount = increaseAmount;
        this.isResearchCompleted = false;
    }

    public StructureAttackAdvancementNode(StructureManager structureManager, EntitySubtypeEnum structureType, int increaseAmount, String name, String description, StructureAttackAdvancementNode parent) {
        super(name, description, parent);
        this.structureManager = structureManager;
        this.structureType = structureType;
        this.increaseAmount = increaseAmount;
        this.isResearchCompleted = false;
    }

    public boolean isResearchCompleted() {
        return this.isResearchCompleted;
    }

    public void doResearch() throws EntityTypeDoesNotExistException{
        this.isResearchCompleted = true;
        iStructureResearchVisitor visitor = new StructureAttackStrengthResearchVisitor(this.structureType, this.increaseAmount);
        try {
            visitor.visitStructureManager(this.structureManager);
        } catch (StructureTypeDoesNotExist e) {
            log.error(e.getLocalizedMessage());
            throw new EntityTypeDoesNotExistException();
        }
    }
}

