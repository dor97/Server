package Engine.world.rule.actionDefinition;

import Engine.generated.PRDAction;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import Engine.world.rule.action.single;

public class actionDefinition{
    private boolean isSecondaryAll = false;
    private int countForSecondaryEntities = 0;
    private String m_secondaryEntity = "";
    private single condition = null;
    private String m_ruleName;
    private String actionName = "";
    private String entityName;
    private String targetEntity;
    protected int m_currTick = 0;

    public actionDefinition(PRDAction action, String ruleName){
        actionName = action.getType();
        entityName = action.getEntity();
        if(actionName.equals("proximity")){
            entityName = action.getPRDBetween().getSourceEntity();
            targetEntity = action.getPRDBetween().getTargetEntity();
        } else if (actionName.equals("replace")) {
            entityName = action.getKill();
        }
        m_ruleName = ruleName;
        if(action.getPRDSecondaryEntity() != null){
            m_secondaryEntity = action.getPRDSecondaryEntity().getEntity();
            expression temp = new expression();
            temp.convertValueInString(action.getPRDSecondaryEntity().getPRDSelection().getCount());
            if(temp.getType() == expressionType.INT){
                countForSecondaryEntities = temp.getInt();
            }
            else{
                isSecondaryAll = true;
                countForSecondaryEntities = -1;
            }
            if(action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition() != null){
                //condition = new single(action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition());
            }
        }
    }
}
