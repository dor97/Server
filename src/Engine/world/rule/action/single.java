package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.generated.PRDCondition;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import Engine.world.expression.expressionWithFunc;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class single implements subCondition, Serializable {

    public enum opertor{ EQUAL, UNEQUAL, BIGGER, LITTLE}
    private String m_entityName;
    private String m_propertyName;
    private opertor m_op;
    private expressionWithFunc m_exprecn;
    private Utilites m_util;
    private int m_currTick;

    public single(PRDCondition condition, Utilites util) throws InvalidValue{
        m_entityName = condition.getEntity();
        m_propertyName = condition.getProperty();
        m_op = getOpFromString(condition.getOperator());
        m_exprecn = new expressionWithFunc(util);
        try {
            m_exprecn.convertValueInString(condition.getValue());
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In single in action condition");
        }
        m_util = util;
        cheackUserInput();
    }

    public single(single condition, Utilites util){
        m_entityName = condition.getEntity();
        m_propertyName = condition.getProperty();
        m_op = getOp();
        m_exprecn = new expressionWithFunc(util);
        try {
            m_exprecn.convertValueInString(condition.getValue());
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In single in action condition");
        }
        m_util = util;
        cheackUserInput();
    }

    public subCondition clone(Utilites util){
        return new single(this, util);
    }
    public String getSingularity(){
        return "single";
    }

    public String getValue(){
        return m_exprecn.getValue().toString();
    }

    public opertor getOp(){
        return m_op;
    }

    public String getProperty(){
        return m_propertyName;
    }

    public String getEntity(){
        return m_entityName;
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityAndPropertyExist();
        //checkTypeValid();
        //checkCompatibilityBetweenPropertyAndExpression();   //disable because it is exercise requirements
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(m_exprecn.getType() == expressionType.STRING) {
            if (!m_exprecn.isFunc()) {
                checkExpressionIfProperty();
            }
            else{
                checkExpressionIfFunction();
            }
        }
    }

    private void checkExpressionIfProperty() throws InvalidValue{
        if (!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(m_exprecn.getString())) {
            throw new InvalidValue("In action condition in single value is of the wrong type");
        }
        if(!(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_exprecn.getString()).getType() == expressionType.INT || m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_exprecn).getType() == expressionType.FLOAT)){
            throw new InvalidValue("In action condition in single value is a property of the wrong type");
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue{
        if (m_exprecn.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(m_exprecn.getParams(0).getString()));
            expressionType temp = m_util.getEnvironmentType(m_exprecn.getParams(0).getString());
            if (temp == expressionType.STRING || temp == expressionType.BOOL) {
                throw new InvalidValue("In action condition in single value is of the wrong type");
            }
        }
    }

    private void checkTypeValid() throws InvalidValue{
        if(m_exprecn.getType() == expressionType.BOOL){
            throw new InvalidValue("In action condition in single value is of the wrong type");
        }
        if(m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.STRING || m_util.getEntityDifenichan(m_entityName).getPropertys().get(m_propertyName).getType() == expressionType.BOOL){
            throw new InvalidValue("In action condition in single got a wrong type property");
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!m_util.isEntityDifenichanExists(m_entityName)){
            throw new OBJECT_NOT_EXIST("In action condition in single the entity " + m_entityName + " does not exist.");
        }
//        if(!m_util.getEntityDifenichan(m_entityName).getPropertys().containsKey(m_propertyName)){
//            throw new OBJECT_NOT_EXIST("In action condition in single the property " + m_propertyName + "of entity " + m_entityName +" does not exist.");
//        }
    }

    private opertor getOpFromString(String op){
        if(op.equals("=")){
            return opertor.EQUAL;
        } else if(op.equals("!=")){
            return opertor.UNEQUAL;
        } else if (op.equals("bt")){
            return opertor.BIGGER;
        } else if (op.equals("lt")) {
            return opertor.LITTLE;
        }
        return opertor.LITTLE;  //do not get to here
    }

    private boolean active(expression propertyValue, expressionWithFunc value, Entity entity)throws InvalidValue{
        if (value.getType() == expressionType.INT || value.getType() == expressionType.FLOAT || value.getType() == expressionType.BOOL) {
            return getOpValue(propertyValue, value);    //cheak if propertyValue type string or bool later and throw excepcen
        } else if (value.getType() == expressionType.STRING) {
            expressionWithFunc temp = value.decipherValue(entity, m_util, m_currTick);
            if (temp == value) {
                return getOpValue(propertyValue, value);
            } else {
                return active(propertyValue, temp, entity);
            }
        }
        return  false; //do not get here
    }

    @Override
    public boolean getBoolValue(Entity entity, Entity secondaryEntity, int currTick){
        m_currTick = currTick;
        expressionWithFunc propertyValue = new expressionWithFunc(m_util);
        Entity temp = entity.getName().equals(m_entityName) ? entity : secondaryEntity;
        propertyValue.setEntityParams(new ArrayList<>(Arrays.asList(temp)));
        m_exprecn.setEntityParams(new ArrayList<>(Arrays.asList(temp)));
        try {
            propertyValue.convertValueInString(m_propertyName);
            propertyValue = propertyValue.decipherValue(temp, m_util, m_currTick);
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". In single in action condition");
        }
        return active(propertyValue, m_exprecn, temp);

    }
    @Override
    public boolean shouldIgnore(Entity entity){
        if(!m_entityName.equals(entity.getName())){
            return true;
        }
        return false;
    }

    @Override
    public boolean getBoolValue(Entity entity, int currTick) throws InvalidValue{
        m_currTick = currTick;
        expressionWithFunc propertyValue = new expressionWithFunc(m_util);
        propertyValue.setEntityParams(new ArrayList<>(Arrays.asList(entity)));
        m_exprecn.setEntityParams(new ArrayList<>(Arrays.asList(entity)));
        try {
            propertyValue.convertValueInString(m_propertyName);
            propertyValue = propertyValue.decipherValue(entity, m_util, m_currTick);
        }catch (InvalidValue e){
        throw new InvalidValue(e.getMessage() + ". In single in action condition");
        }
        return active(propertyValue, m_exprecn, entity);

//        if (m_exprecn.getType() == expressionType.INT || m_exprecn.getType() == expressionType.FLOAT || m_exprecn.getType() == expressionType.BOOL) {
//            return getOpValue(propertyValue, m_exprecn);    //cheak if propertyValue type string or bool later and throw excepcen
//        } else if (m_exprecn.getType() == expressionType.STRING) {
//            if (m_exprecn.isFunc()) {
//                if (m_exprecn.getValue().equals("environment")) {
//                    expression temp = new expression();
//                    temp.setValue(m_util.environment(m_exprecn.getParams(0).getString()));
//                    return getOpValue(propertyValue, temp);
////                    if (temp.getType() == expressionType.INT || temp.getType() == expressionType.FLOAT) {
////                        return getOpValue(propertyValue, temp);    //cheak if propertyValue type string of bool later and throw excepcen
////                    }
////                    else {
////                        throw new InvalidValue("In action condition in single value is of the wrong type");
////                    }
//                } else if(m_exprecn.getString().equals("random")){
//                    expression temp = new expression();
//                    temp.setValue(m_util.random(m_exprecn.getParams(0).getInt()));
//                    return getOpValue(propertyValue, temp);
////                    if(temp.getType() == expressionType.INT) {
////                        return getOpValue(propertyValue, m_exprecn);
////                    }
////                    else{
////                        throw new InvalidValue("In action condition in single value is of the wrong type");
////                    }
//                }
//            } else {
//                if (entity.isPropertyExists(m_exprecn.getString())) {
//                    expression temp = new expression();
//                    temp.setValue(entity.getProperty(m_exprecn.getString()).getValue());
//                    return getOpValue(propertyValue, temp);
////                    if (temp.getType() == expressionType.INT || temp.getType() == expressionType.FLOAT) {
////                        return getOpValue(propertyValue, temp);    //cheak if propertyValue type string of bool later and trow excepcen
////                    }
////                    else {
////                        throw new InvalidValue("In action condition in single property is of the wrong type");
////                    }
//                }
//                else{
//                    return getOpValue(propertyValue, m_exprecn);
//                    //throw new InvalidValue("In action condition in single property is of the wrong type");
//                }
//            }
//        }
//        return false;   //do not get to here
    }

    private boolean getOpValue(expression expression1, expression expression2)throws InvalidValue{
        if(m_op == opertor.EQUAL){
            return getIsEqual(expression1, expression2);
        } else if (m_op == opertor.UNEQUAL) {
            return !getIsEqual(expression1, expression2);
        } else if (m_op == opertor.BIGGER) {
            return getIsBigger(expression1, expression2);
        } else if (m_op == opertor.LITTLE) {
            return getIsBigger(expression2, expression1); //swap between exprecn1 and eprecn2
        }
        return  false; //do not get here
    }
    
    private boolean getIsEqual(expression expression1, expression expression2)throws InvalidValue{
        if(expressionType.INT == expression2.getType() && expression1.getType() == expressionType.INT){
            return expression1.getInt() == expression2.getInt();
        } else if (expressionType.FLOAT == expression2.getType() && expression1.getType() == expressionType.FLOAT) {
            return expression1.getFloat() == expression2.getFloat();
        } else if (expression1.getType() == expressionType.INT && expression2.getType() == expressionType.FLOAT) {
            return expression1.getInt() == expression2.getFloat();
        } else if (expression1.getType() == expressionType.FLOAT && expression2.getType() == expressionType.INT) {
            return expression1.getFloat() == expression2.getInt();
        } else if (expression1.getType() == expressionType.STRING && expression2.getType() == expressionType.STRING) {
            return expression1.equals(expression2);
        } else if (expression1.getType() == expressionType.BOOL && expression2.getType() ==expressionType.BOOL) {
            return expression1.getBool() == expression2.getBool();
        }
        throw new InvalidValue("In action condition in single values are incompatible");
        //return false;//excpcen
    }

    private boolean getIsBigger(expression expression1, expression expression2)throws InvalidValue{
        if(expression1.getType() == expression2.getType() && expression1.getType() == expressionType.INT){
            return expression1.getInt() > expression2.getInt();
        } else if (expression1.getType() == expression2.getType() && expression1.getType() == expressionType.FLOAT) {
            return expression1.getFloat() > expression2.getFloat();
        } else if (expression1.getType() == expressionType.INT && expression2.getType() == expressionType.FLOAT) {
            return expression1.getInt() > expression2.getFloat();
        } else if (expression1.getType() == expressionType.FLOAT && expression2.getType() == expressionType.INT) {
            return expression1.getFloat() > expression2.getInt();
        }
        throw new InvalidValue("In action condition in single property/value is of the wrong type");
        //return false;//excpcen
    }

//    private boolean equal(Integer v1, Integer v2){
//        return v1.equals(v2);
//    }
//
//    private boolean equal(Float v1, Float v2){
//        return v1.equals(v2);
//    }
//
//    private boolean bigger(Integer v1, Integer v2){
//        return v1 > v2;
//    }
//
//    private boolean bigger(Float v1, Float v2){
//        return v1 > v2;
//    }
    @Override
    public void makeActionDto(DTOActionData actionData){
        actionData.putData("condition", "single");
        actionData.putData("property", m_propertyName);
        actionData.putData("operator", m_op.toString());
        actionData.putData("value", m_exprecn.toString());
    }
}
