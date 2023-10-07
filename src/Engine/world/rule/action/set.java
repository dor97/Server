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


public class set extends action implements Serializable {

    private String m_entityName;
    private String m_propertyName;
    private expressionWithFunc m_value;
    private Entity m_entity = null;
    private Utilites m_util;
    //private int m_currTick;

    public set(PRDAction action, Utilites util, String ruleName) throws InvalidValue{
        super(action, util, ruleName);
        m_value = new expressionWithFunc(util);
        m_entityName = action.getEntity();
        m_propertyName = action.getProperty();
        //actionName = action.getType();
        try {
            m_value.convertValueInString(action.getValue());
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In action " + getActionName());
        }
        m_util = util;
        cheackUserInput();
    }

    public set(set action, Utilites util, String ruleName) throws InvalidValue{
        super(action, util, ruleName);
        m_value = new expressionWithFunc(util);
        m_entityName = action.getEntity();
        m_propertyName = action.getProperty();
        //actionName = action.getType();
        try {
            m_value.convertValueInString(action.getValue());
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In action " + getActionName());
        }
        m_util = util;
        cheackUserInput();
    }

    public set clone(Utilites util, String ruleName){
        return new set(this, util, ruleName);
    }

    public String getValue(){
        return m_value.getValue().toString();
    }
    @Override
    public String getEntity(){
        return m_entityName;
    }

    public String getProperty(){
        return m_propertyName;
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityAndPropertyExist();
        //checkCompatibilityBetweenPropertyAndExpression(); //disable because if exercise requirements
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() != m_value.getType()){
            if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.FLOAT && m_value.getType() == expressionType.INT)){
                if(m_value.getType() == expressionType.STRING) {
                    if (!m_value.isFunc()) {
                        checkExpressionIfProperty();
                    }
                    else{
                        checkExpressionIfFunction();
                    }
                }
                else {
                    throw new InvalidValue("In action set the property and value are not compatible");
                }
            }

        }
    }

    private void checkExpressionIfProperty() throws InvalidValue{
        if (!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(m_value.getString())) {  //value is string
            throw new InvalidValue("In action set the value is of the wrong type");
        }
        if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_value.getString()).getType() != m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType()){
            if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.FLOAT && m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_value).getType() == expressionType.INT)){
                throw new InvalidValue("In action set the value is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue {
        if (m_value.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(m_value.getParams(0).getString()));
            expressionType temp = m_util.getEnvironmentType(m_value.getParams(0).getString());
            if (m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() != temp) {
                if (!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.FLOAT && m_value.getType() == expressionType.INT)) {
                    throw new InvalidValue("In action set the value is of the wrong type");
                }
            }
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!m_util.isEntityDifenichanExists(m_entityName)){
            throw new OBJECT_NOT_EXIST("In action set the entity " + m_entityName + " does not exist.");
        }
        if(getCountForSecondaryEntities() != 0 && !m_util.isEntityDifenichanExists(getSecondaryName())){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + getSecondaryName() + " does not exist.");
        }
        if(!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(m_propertyName)){
            throw new OBJECT_NOT_EXIST("In action set the property " + m_propertyName + "of entity " + m_entityName +" does not exist.");
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
    protected void wrapper(int value)throws InvalidValue{
        m_entity.getProperty(m_propertyName).setProperty(value, m_currTick);
    }
    @Override
    protected void wrapper(float value)throws InvalidValue{
        m_entity.getProperty(m_propertyName).setProperty(value, m_currTick);
    }
    @Override
    protected void wrapper(boolean value)throws InvalidValue{
        m_entity.getProperty(m_propertyName).setProperty(value, m_currTick);
    }
    @Override
    protected void wrapper(String value)throws InvalidValue{
        m_entity.getProperty(m_propertyName).setProperty(value, m_currTick);
    }


    private void loopThroughEntities(Entity entity){
        m_entity = entity;
        activate(m_value, m_entity, m_util);
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity i_entity, int currTick, List<Entity> paramsForFuncs)throws InvalidValue {
        m_currTick = currTick;
        List<Entity> secondaryEntities = null;
        if(getCountForSecondaryEntities() != 0 && !getSecondaryName().equals(m_entityName)){
            secondaryEntities = getSecondaryEntities();
        }
        m_value.setEntityParams(paramsForFuncs);
        if(secondaryEntities == null){
            loopThroughEntities(i_entity);
        }else{
            m_value.addToEntityParams(i_entity);
            if(m_entityName.equals(i_entity.getName())){
                secondaryEntities.stream().forEach(secondaryEntity ->{m_value.switchLastEntityParam(secondaryEntity);
                                                        loopThroughEntities(i_entity);});
            }else {
                secondaryEntities.stream().forEach(secondaryEntity ->{m_value.switchLastEntityParam(secondaryEntity);
                                                                loopThroughEntities(secondaryEntity);});
            }
        }
        return new HashMap<>();

//        m_entity = i_entity;
//        activate(m_value, m_entity, m_util);
//        return false;

//        if(m_value.getType() == expressionType.INT){
//            i_entity.getProperty(m_propertyName).setProperty(m_value.getInt());
//        } else if (m_value.getType() == expressionType.FLOAT) {
//            i_entity.getProperty(m_propertyName).setProperty(m_value.getFloat());
//        } else if (m_value.getType() == expressionType.BOOL) {
//            i_entity.getProperty(m_propertyName).setProperty(m_value.getBool());
//        }else {
//            if (m_value.isFunc()){
//                if(m_value.getValue().equals("environment")){
//                    expression temp = new expression();
//                    temp.setValue(m_util.environment(m_value.getParams(0).getString()));
//                    if(temp.getType() == expressionType.INT) {
//                        i_entity.getProperty(m_propertyName).setProperty(temp.getInt());
//                    } else if (temp.getType() == expressionType.FLOAT) {
//                        i_entity.getProperty(m_propertyName).setProperty(temp.getFloat());
//                    } else if (temp.getType() == expressionType.BOOL){
//                        i_entity.getProperty(m_propertyName).setProperty(temp.getBool());
//                    } else{
//                        i_entity.getProperty(m_propertyName).setProperty(temp.getString());
//                    }
//                }
//                else if(m_value.getString().equals("random")){
//                    expression temp = new expression();
//                    temp.setValue(m_util.random(m_value.getParams(0).getInt()));
//                    if(temp.getType() == expressionType.INT) {
//                        i_entity.getProperty(m_propertyName).addToProperty(temp.getInt());
//                    }
//                    //excepcen
//                }
//            }
//            else {
//                if(i_entity.isPropertyExists(m_propertyName)){
//                    expression temp = new expression();
//                    temp.setValue(i_entity.getProperty(m_propertyName).getValue());
//                    if(m_value.getType() == expressionType.INT) {
//                        i_entity.getProperty(m_propertyName).setProperty(m_value.getInt());
//                    } else if (m_value.getType() == expressionType.FLOAT) {
//                        i_entity.getProperty(m_propertyName).setProperty(m_value.getFloat());
//                    } else if (m_value.getType() == expressionType.BOOL) {
//                        i_entity.getProperty(m_propertyName).setProperty(m_value.getBool());
//                    } else{
//                        i_entity.getProperty(m_propertyName).setProperty(m_value.getString());
//                    }
//                }
//                else {
//                    i_entity.getProperty(m_propertyName).setProperty(m_value.getString());
//                }
//            }
//        }
//
//        return false;
    }

    @Override
    public DTOActionData makeActionDto(){
        DTOActionData actionData = new DTOActionData(getActionName());
        actionData.putData("entity", m_entityName);
        actionData.putData("property", m_propertyName);
        actionData.putData("value", m_value.toString());
        actionData.putData("secondary", getSecondaryName());

        return actionData;
    }
}
