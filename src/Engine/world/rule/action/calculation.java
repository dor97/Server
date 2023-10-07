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
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class calculation extends action implements Serializable {
    private String m_entityName;
    private String m_propertyName;
    private String m_value1, m_value2;
    private expressionWithFunc m_arg1, m_arg2;
    private List<expression> m_args = new ArrayList<>();
    private boolean isMultiply = true;
    private Entity m_entity = null;
    private expression v1Expression = new expression(), v2Expression = new expression();
    private Utilites m_util;
    //private int m_currTick;

    public calculation(String entity, String property, String v1, String v2){
        m_entityName = entity;
        m_propertyName = property;
        m_value1 = v1;
        m_value2 = v2;
    }

    public calculation(PRDAction action, Utilites util, String ruleName) throws InvalidValue{
        super(action, util, ruleName);
        m_entityName = action.getEntity();
        m_propertyName = action.getResultProp();
        m_util = util;
        if(action.getPRDMultiply() != null){
            m_value1 = action.getPRDMultiply().getArg1();
            m_value2 = action.getPRDMultiply().getArg2();
        }else{
            m_value1 = action.getPRDDivide().getArg1();
            m_value2 = action.getPRDDivide().getArg2();
            isMultiply = false;
        }
        m_arg1 = new expressionWithFunc(util);
        m_arg2 = new expressionWithFunc(util);
        try {
            m_arg1.convertValueInString(m_value1);
            m_arg2.convertValueInString(m_value2);
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In action " + action.getType());
        }
        //actionName = action.getType();
        cheackUserInput();
    }

    public calculation(calculation action, Utilites util, String ruleName) throws InvalidValue{
        super(action, util, ruleName);
        m_entityName = action.getEntity();
        m_propertyName = action.getPropertyName();
        m_util = util;
        m_value1 = action.getArg1();
        m_value2 = action.getArg2();
        isMultiply = action.isMultiply();
//        if(action.isMultiply()){
//            m_value1 = action.getPRDMultiply().getArg1();
//            m_value2 = action.getPRDMultiply().getArg2();
//        }else{
//            m_value1 = action.getPRDDivide().getArg1();
//            m_value2 = action.getPRDDivide().getArg2();
//            isMultiply = false;
//        }
        m_arg1 = new expressionWithFunc(util);
        m_arg2 = new expressionWithFunc(util);
        try {
            m_arg1.convertValueInString(m_value1);
            m_arg2.convertValueInString(m_value2);
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In action " + action.getType());
        }
        //actionName = action.getType();
        cheackUserInput();
    }

    @Override
    public action clone(Utilites util, String ruleName){
        return new calculation(this, util, ruleName);
    }

    public String getArg1(){
        return m_value1;
    }

    public String getArg2(){
        return m_value2;
    }
    private Boolean isMultiply(){
        return isMultiply;
    }

    private void cheackUserInput() throws InvalidValue{
        checkEntityAndPropertyExist();
        checkTypeValid(m_arg1);
        checkTypeValid(m_arg2);
        checkCompatibilityBetweenPropertyAndExpression(m_arg1);
        checkCompatibilityBetweenPropertyAndExpression(m_arg2);
    }

    private void checkCompatibilityBetweenPropertyAndExpression(expressionWithFunc arg) throws InvalidValue{
        if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() != arg.getType()){
            if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.INT && arg.getType() == expressionType.FLOAT){
                throw new InvalidValue("In action calculation the property and ond of the expression are not compatible");
            }
            if(arg.getType() == expressionType.STRING) {
                if (!arg.isFunc()) {
                    checkExpressionIfProperty(arg);
                }
                else{
                    checkExpressionIfFunction(arg);
                }
            }
        }
    }

    private void checkExpressionIfProperty(expressionWithFunc arg) throws InvalidValue{
        if (!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(arg.getString())) {
            throw new InvalidValue("In action calculation there is an expression of the wrong type");
        }
        if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(arg.getString()).getType() == expressionType.INT)){
            if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(arg.getString()).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action calculation one of the expression is a property of the wrong type");
            }
            if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action calculation one of the expression is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction(expressionWithFunc arg) throws InvalidValue{
        if (arg.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(arg.getParams(0).getString()));
            expressionType temp = m_util.getEnvironmentType(arg.getParams(0).getString());
            if (temp == expressionType.STRING || temp == expressionType.BOOL) {
                throw new InvalidValue("In action calculation one of the expression is of the wrong type");
            }
            if (temp == expressionType.FLOAT && m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.INT) {
                throw new InvalidValue("In action calculation the property and one of the expression are not compatible");
            }
        } else if(arg.getString().equals("evaluate")){
            if(arg.checkEvaluateIsNumber()){
                throw new InvalidValue("In action " + getActionName() + " got wrong property type in evaluate in arg");
            }
        }
    }

    private void checkTypeValid(expressionWithFunc arg) throws InvalidValue{
        if(arg.getType() == expressionType.BOOL){
            throw new InvalidValue("In action calculation the value  by is of the wrong type");
        }
        if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.STRING || m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.BOOL){
            throw new InvalidValue("In action calculation got a wrong type property");
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!m_util.isEntityDifenichanExists(m_entityName)){
            throw new OBJECT_NOT_EXIST("In action calculation the entity " + m_entityName + " does not exist.");
        }
        if(getCountForSecondaryEntities() != 0 && !m_util.isEntityDifenichanExists(getSecondaryName())){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + getSecondaryName() + " does not exist.");
        }
        if(!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(m_propertyName)){
            throw new OBJECT_NOT_EXIST("In action calculation the property " + m_propertyName + " of entity " + m_entityName +" does not exist.");
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

    private void setArgumentValue(expression argument, expressionWithFunc v, Entity entity) throws InvalidValue{
        if(v.getType() == expressionType.INT){
            argument.setValue(v.getInt());
        } else if (v.getType() == expressionType.FLOAT) {
            argument.setValue(v.getFloat());
        } else if (v.getType() == expressionType.STRING) {
            if (v.isFunc()) {
                if(v.getString().equals("environment")) {
                    expression temp = new expression();
                    temp.setValue(m_util.environment(v.getParams(0).getString()));
                    if (temp.getType() == expressionType.INT) {
                        argument.setValue(temp.getInt());
                    } else if (temp.getType() == expressionType.FLOAT) {
                        argument.setValue(temp.getFloat());
                    } else {
                        throw new InvalidValue("In action calculation can't use value arg");
                    }
                }
                else if(v.getString().equals("random")){
                    expression temp = new expression();
                    temp.setValue(m_util.random(v.getParams(0).getInt()));
                    if(temp.getType() == expressionType.INT) {
                        entity.getProperty(m_propertyName).addToProperty(temp.getInt(), m_currTick);
                    }
                    else {
                        throw new InvalidValue("In action calculation can't use value arg");
                    }
                }


            } else {
                if (entity.isPropertyExists(v.getString())) {
                    expression temp = new expression();
                    temp.setValue(entity.getProperty(v.getString()).getValue());
                    if (temp.getType() == expressionType.INT) {
                        argument.setValue(temp.getInt());
                    } else if (temp.getType() == expressionType.FLOAT) {
                        argument.setValue(temp.getFloat());
                    } else {
                        throw new InvalidValue("In action calculation can't use value arg");
                    }
                }
                else {
                    throw new InvalidValue("In action calculation can't use value arg");
                }
            }
        } else if (v.getType() == expressionType.BOOL) {
            throw new InvalidValue("In action calculation can't use value arg");
        }
        //do not get here
    }
    @Override
    protected void wrapper(int value)throws InvalidValue{
        expression temp = new expression();
        temp.setValue(value);
        m_args.add(temp);
    }
    @Override
    protected void wrapper(float value)throws InvalidValue{
        expression temp = new expression();
        temp.setValue(value);
        m_args.add(temp);
    }

    public boolean doCalculation(Entity entity) throws InvalidValue{
        m_entity = entity;
        activate(m_arg1, entity, m_util);
        activate(m_arg2, entity, m_util);

        expression v1 = m_args.get(m_args.size() - 1), v2 = m_args.get(m_args.size() - 2);
        //setArgumentValue(v1, m_arg1, entity);
        //setArgumentValue(v2, m_arg2, entity);
        try {
            if (v1.getType() == v2.getType() && v2.getType() == expressionType.INT) {
                setProperty(v1.getInt(), v2.getInt(), entity);
                return false;
            }
            if (v1.getType() == v2.getType() && v2.getType() == expressionType.FLOAT) {
                setProperty(v1.getFloat(), v2.getFloat(), entity);
                return false;
            }
            if (v1.getType() == expressionType.INT && v2.getType() == expressionType.FLOAT) {
                setProperty(v1.getInt(), v2.getFloat(), entity);
                return false;
            }
            if (v1.getType() == expressionType.FLOAT && v2.getType() == expressionType.INT) {
                setProperty(v1.getFloat(), v2.getInt(), entity);
                return false;
            }
            throw new InvalidValue("In action calculation can't use value arg");
        }
        finally{
            m_args.clear();
        }
        //return false;
        //exepcen
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int currTick, List<Entity> paramsForFuncs) throws InvalidValue{
        m_currTick = currTick;
        List<Entity> secondaryEntities = null;
        if(getCountForSecondaryEntities() != 0){
            secondaryEntities = getSecondaryEntities();
        }
        m_arg1.setEntityParams(paramsForFuncs);
        m_arg2.setEntityParams(paramsForFuncs);
        if(secondaryEntities == null){
            loopThroughEntities(entity);
        }else{
            m_arg1.addToEntityParams(entity);
            m_arg2.addToEntityParams(entity);
            if(m_entityName.equals(entity.getName())){
                secondaryEntities.stream().forEach(secondaryEntity ->{m_arg1.switchLastEntityParam(secondaryEntity);
                                                                m_arg2.switchLastEntityParam(secondaryEntity);
                                                                loopThroughEntities(entity);});
            }else {
                secondaryEntities.stream().forEach(secondaryEntity ->{m_arg1.switchLastEntityParam(secondaryEntity);
                                                    m_arg2.switchLastEntityParam(secondaryEntity);
                                                    loopThroughEntities(secondaryEntity);});
            }
        }
        return new HashMap<>();
    }

    private void loopThroughEntities(Entity entity){
        m_entity = entity;
        doCalculation(entity);
    }

    public void setProperty(int v1, int v2, Entity entity){
        if(isMultiply){
            entity.getProperty(m_propertyName).setProperty(v1 * v2, m_currTick);
        }
        else{
            if(v2 == 0){
                throw new ArithmeticException("In action calculation with entity " + m_entityName + "and property" + m_propertyName + " ,divided by zero in");
            }
            if(v1 % v2 == 0) {
                entity.getProperty(m_propertyName).setProperty(v1 / v2, m_currTick);
            }
        }

    }

    public void setProperty(float v1, float v2, Entity entity){
        if(isMultiply){
            entity.getProperty(m_propertyName).setProperty(v1 * v2, m_currTick);
        }
        else{
            if(v2 == 0){
                throw new ArithmeticException("In action calculation with entity " + m_entityName + "and property" + m_propertyName + " ,divided by zero in");
            }
            entity.getProperty(m_propertyName).setProperty(v1 / v2, m_currTick);
        }
    }

    @Override
    public boolean setValues(PropertyInterface v1, PropertyInterface v2) {

        return false;
    }

    @Override
    public DTOActionData makeActionDto(){
        DTOActionData actionData = new DTOActionData(getActionName());
        actionData.putData("entity", m_entityName);
        actionData.putData("property", m_propertyName);
        actionData.putData("calculationType", isMultiply == true ? "multiply" : "divide");
        actionData.putData("arg1", m_value1);
        actionData.putData("arg2", m_value2);
        actionData.putData("secondary", getSecondaryName());

        return actionData;
    }
}
