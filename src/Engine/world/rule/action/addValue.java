package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import Engine.world.expression.expressionType;
import Engine.world.expression.expressionWithFunc;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addValue extends action implements Serializable {  //increase or decrease
    private String m_entityName;
    private String m_propertyName;
    private expressionWithFunc m_by;
    private int sign;
    private Entity m_entity = null;
    private Utilites m_util;
    //private int m_currTick;
//    private boolean isSecondaryAll = false;
//    private int countForSecondaryEntities = 0;
//    private String m_secondaryEntity;
//    private single condition = null;

    public addValue(String entity, String property, String by) {
        m_entityName = entity;
        m_propertyName = property;
        m_by.convertValueInString(by);
    }

    public addValue(PRDAction action, Utilites util, String ruleName) throws InvalidValue {
        super(action, util, ruleName);
        m_by = new expressionWithFunc(util);
        try {
            m_by.convertValueInString(action.getBy());
        }
        catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In action " + action.getType());
        }
        m_entityName = action.getEntity();
        m_propertyName = action.getProperty();
        sign = action.getType().equals("increase") ? 1 : -1;
        //getActionName() = action.getType();
        m_util = util;
        cheackUserInput();
    }

    public addValue(addValue action, Utilites util, String ruleName) throws InvalidValue {
        super(action, util, ruleName);
        m_by = new expressionWithFunc(util);
        try {
            m_by.convertValueInString(action.getBy());
        }
        catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In action " + action.getType());
        }
        m_entityName = action.getEntity();
        m_propertyName = action.getProperty();
        sign = action.getType().equals("increase") ? 1 : -1;
        //getActionName() = action.getType();
        m_util = util;
        cheackUserInput();
    }
    @Override
    public action clone(Utilites util, String ruleName){
        return new addValue(this, util, ruleName);
    }

    public String getBy(){
        return m_by.getFullValue().toString();
    }

    public String getType(){
        return sign == 1 ? "increase" : "decrease";
    }

    public String getEntity(){
        return m_entityName;
    }

    public String getProperty(){
        return m_propertyName;
    }

    private void cheackUserInput() throws InvalidValue{
        checkEntityAndPropertyExist();
        checkTypeValid();
        checkCompatibilityBetweenPropertyAndExpression();
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() != m_by.getType()){
            if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.INT && m_by.getType() == expressionType.FLOAT){
                throw new InvalidValue("In action " + getActionName() +" the property and the value by are not compatible");
            }
            if(m_by.getType() == expressionType.STRING) {
                if (!m_by.isFunc()) {
                    checkExpressionIfProperty();
                }
                else{
                    checkExpressionIfFunction();
                }
            }
        }
    }

    private void checkExpressionIfProperty() throws InvalidValue{
        if (!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(m_by.getString())) {
            throw new InvalidValue("In action " + getActionName() + " the value by is of the wrong type");
        }
        if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_by.getString()).getType() == expressionType.INT)){
            if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_by.getString()).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action " + getActionName() + " the value by is a property of the wrong type");
            }
            if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action " + getActionName() + " the value by is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue{
        if (m_by.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(m_by.getParams(0).getString()));
            expressionType temp = m_util.getEnvironmentType(m_by.getParams(0).getString());
            if (temp == expressionType.STRING || temp == expressionType.BOOL) {
                throw new InvalidValue("In action " + getActionName() + " the value by is of the wrong type");
            }
            if (temp == expressionType.FLOAT && m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.INT) {
                throw new InvalidValue("In action " + getActionName() + " the property and the value by are not compatible");
            }
        } else if(m_by.getString().equals("evaluate")){
            if(!m_by.checkEvaluateIsNumber()){
                throw new InvalidValue("In action " + getActionName() + " got wrong property type in evaluate in by");
            }
        }
    }

    private void checkTypeValid() throws InvalidValue{
        if(m_by.getType() == expressionType.BOOL){
            throw new InvalidValue("In action " + getActionName() + " the value  by is of the wrong type");
        }
        if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.STRING || m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.BOOL){
            throw new InvalidValue("In action " + getActionName() + " got a wrong type property");
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!m_util.isEntityDifenichanExists(m_entityName)){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + m_entityName + " does not exist.");
        }
        if(getCountForSecondaryEntities() != 0 && !m_util.isEntityDifenichanExists(getSecondaryName())){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + getSecondaryName() + " does not exist.");
        }
        if(!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(m_propertyName)){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the property " + m_propertyName + " of entity " + m_entityName +" does not exist.");
        }
    }

    @Override
    public String getEntityName(){
        return m_entityName;
    }
    @Override
    public String getPropertyName(){
        return m_propertyName;
    }
    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int currTick, List<Entity> paramsForFuncs)throws InvalidValue{
        m_currTick = currTick;
        List<Entity> secondaryEntities = null;
        if(getCountForSecondaryEntities() != 0){
            secondaryEntities = getSecondaryEntities();
        }
        m_by.setEntityParams(paramsForFuncs);
        if(secondaryEntities == null){
            loopThroughEntities(entity);
        }else{
            m_by.addToEntityParams(entity);
            if(m_entityName.equals(entity.getName())){
                secondaryEntities.stream().forEach(secondaryEntity ->{m_by.switchLastEntityParam(secondaryEntity);loopThroughEntities(entity);});
            }else {
                secondaryEntities.stream().forEach(secondaryEntity -> {m_by.switchLastEntityParam(secondaryEntity);
                    loopThroughEntities(secondaryEntity);});
            }
        }
        return new HashMap<>();
    }

    private void loopThroughEntities(Entity entity){
        m_entity = entity;
        activate(m_by, entity, m_util);
    }

//    @Override
//    public boolean isSecondaryAll() {
//        return false;
//    }
//
//    @Override
//    public int getCountForSecondaryEntities() {
//        return 0;
//    }
//
//    @Override
//    public String getSecondaryName() {
//        return null;
//    }
//
//    @Override
//    public single getCondition() {
//        return null;
//    }

    @Override
    protected void wrapper(int value)throws InvalidValue{
        m_entity.getProperty(m_propertyName).addToProperty(sign * value, m_currTick);
    }
    @Override
    protected void wrapper(float value)throws InvalidValue{
        m_entity.getProperty(m_propertyName).addToProperty(sign * value, m_currTick);
    }


//    private void activate(expressionWithFunc value, Entity entity)throws InvalidValue{
//        if(value.getType() == expressionType.INT) {
//            entity.getProperty(m_propertyName).addToProperty(sign * value.getInt());
//        }
//        else if (value.getType() == expressionType.FLOAT) {
//            entity.getProperty(m_propertyName).addToProperty(sign * value.getFloat());
//        }
//        else if (value.getType() == expressionType.BOOL){
//            throw new InvalidValue("In action " + getActionName() + "can't use value by");
//        }
//        else if(value.getType() == expressionType.STRING){
//            expressionWithFunc temp = value.decipherValue(entity);
//            if(temp == value){
//                throw new InvalidValue("In action " + getActionName() + "can't use value by");
//            }else{
//                activate(temp, entity);
//            }
//        }
//    }

    @Override
    public DTOActionData makeActionDto(){
        DTOActionData actionData = new DTOActionData(getActionName());
        actionData.putData("entity", m_entityName);
        actionData.putData("property", m_propertyName);
        actionData.putData("by", m_by.toString());
        actionData.putData("secondary", getSecondaryName());

        return actionData;
    }
}
