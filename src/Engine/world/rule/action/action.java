package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import Engine.world.entity.property.PropertyInterface;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import Engine.world.expression.expressionWithFunc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class action implements ActionInterface, Serializable {
    private boolean isSecondaryAll = false;
    private int countForSecondaryEntities = 0;
    private String m_secondaryEntity = "";
    private single condition = null;
    private String m_ruleName;
    private String actionName = "";
    private String entityName;
    private Utilites m_util;
    private String targetEntity;
    protected int m_currTick = 0;

    public action(){}
    public action(ActionInterface action, Utilites util, String ruleName){
        actionName = action.getType();
        entityName = action.getEntity();
//        if(actionName.equals("proximity")){
//            entityName = action.getPRDBetween().getSourceEntity();
//            //targetEntity = action.getPRDBetween().getTargetEntity();
//        } else if (actionName.equals("replace")) {
//            entityName = action.getKill();
//        }
        m_ruleName = ruleName;
        m_util = util;
        if(action.getCountForSecondaryEntities() != 0){
            m_secondaryEntity = action.getSecondaryName();
            expression temp = new expression();
            countForSecondaryEntities = action.getCountForSecondaryEntities();
            if(action.getCondition() != null){
                condition = new single(action.getCondition(), util);
            }
        }
    }
    public action(PRDAction action, Utilites util, String ruleName){
        actionName = action.getType();
        entityName = action.getEntity();
        if(actionName.equals("proximity")){
            entityName = action.getPRDBetween().getSourceEntity();
            targetEntity = action.getPRDBetween().getTargetEntity();
        } else if (actionName.equals("replace")) {
            entityName = action.getKill();
        }
        m_ruleName = ruleName;
        m_util = util;
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
                condition = new single(action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition(), util);
            }
        }
    }

    public action clone(Utilites util, String ruleName){
        return new action(this, util, ruleName);
    }
    @Override
    public String getType(){
        return actionName;
    }
    @Override
    public String getEntity(){
        return entityName;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    public String getTargetEntity(){
        return targetEntity;
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public String getRuleName(){
        return  m_ruleName;
    }

    @Override
    public String getActionName(){
        return actionName;
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int currTick, List<Entity> paramsForFuncs)throws InvalidValue {
        return null;
    }

    @Override
    public boolean setValues(PropertyInterface v1, PropertyInterface v2) {
        return false;
    }

    @Override
    public String getName(){
        return actionName;
    }

    @Override
    public boolean isSecondaryAll() {
        return isSecondaryAll;
    }

    @Override
    public int getCountForSecondaryEntities() {
        return countForSecondaryEntities;
    }

    @Override
    public String getSecondaryName() {
        return m_secondaryEntity;
    }

    @Override
    public single getCondition() {
        return condition;
    }

    protected void activate(expressionWithFunc value, Entity entity, Utilites util)throws InvalidValue{
        if(value.getType() == expressionType.INT) {
            wrapper(value.getInt());
        }
        else if (value.getType() == expressionType.FLOAT) {
            wrapper(value.getFloat());
        }
        else if (value.getType() == expressionType.BOOL){
            wrapper(value.getBool());
        }
        else if(value.getType() == expressionType.STRING){
            expressionWithFunc temp;
            try {
                temp = value.decipherValue(entity, util, m_currTick);
            }catch (InvalidValue e){
                throw new InvalidValue(e.getMessage() + " In action " + actionName);
            }
            if(temp == value){
                wrapper(value.getString());
            }else{
                activate(temp, entity, util);
            }
        }
    }

    protected void wrapper(int value)throws InvalidValue{
        throw new InvalidValue("In action " + actionName + "can't use expression");
    }

    protected void wrapper(float value)throws InvalidValue{
        throw new InvalidValue("In action " + actionName + "can't use expression");
    }

    protected void wrapper(boolean value)throws InvalidValue{
        throw new InvalidValue("In action " + actionName + "can't use expression");
    }

    protected void wrapper(String value)throws InvalidValue{
        throw new InvalidValue("In action " + actionName + "can't use expression");
    }

    protected void wrapper(expression value)throws InvalidValue{
        throw new InvalidValue("In action " + actionName + "can't use expression");
    }

    protected List<Entity> getSecondaryEntities(){
        List<Entity> secondaries = condition == null ? m_util.getEntitiesByName(m_secondaryEntity) :
                                        m_util.getEntitiesByName(m_secondaryEntity).stream().filter(secondary -> condition.getBoolValue(secondary, m_currTick)).collect(Collectors.toList());

        if (isSecondaryAll()) {
            return secondaries;
        } else {
            Random random = new Random();
            random.nextInt();
            //List<Entity> EntitiesOfSecondaryType = m_entities.stream().filter(entity1 -> entity1.getName() == actionInterface.getSecondaryName()).filter(entity1 -> (actionInterface.getCondition()).getBoolValue(entity1)).collect(Collectors.toList());
            return IntStream.range(0, getCountForSecondaryEntities()).mapToObj(i -> secondaries.get(random.nextInt(secondaries.size()))).limit(secondaries.size()).collect(Collectors.toList());
            //secondary = temp.stream().mapToObj(i -> temp.get(random.nextInt(temp.size()))).limit(actionInterface.getCountForSecondaryEntities())
        }
    }
    @Override
    public DTOActionData makeActionDto(){
        DTOActionData actionData = new DTOActionData(getActionName());

        return actionData;
    }
}
