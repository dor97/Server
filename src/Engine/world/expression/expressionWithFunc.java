package Engine.world.expression;

import Engine.InvalidValue;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class expressionWithFunc extends expression implements Serializable {

    private boolean m_isFunc = false;
    private List<expressionWithFunc> params = new ArrayList<>();
    private Utilites m_util;
    private String entityName, propertyName;

    private List<Entity> EntityPramsToFunc = null;

    public expressionWithFunc(Utilites util){
        m_util = util;
    }

    public boolean isFunc() {
        return m_isFunc;
    }

    public void setEntityParams(List<Entity> entityParams){
        EntityPramsToFunc = entityParams;
    }

    public void addToEntityParams(Entity entity){
        if(EntityPramsToFunc == null){
            throw new RuntimeException("try to add to EntityPramsToFunc while it is null");
        }
        EntityPramsToFunc.add(entity);
    }

    public void switchLastEntityParam(Entity entity){
        if(EntityPramsToFunc == null){
            EntityPramsToFunc = new ArrayList<>();
        }
        if(EntityPramsToFunc.size() != 0) {
            EntityPramsToFunc.remove(EntityPramsToFunc.size() - 1);
        }
        EntityPramsToFunc.add(entity);
    }

    @Override
    public void convertValueInString(String value) {
        super.convertValueInString(value);
        m_isFunc = false;
        if (getType() == expressionType.STRING) {
            if (value.contains("(") && value.contains(")")) {
                int openParenIndex = value.indexOf('(');
                int closeParenIndex = value.lastIndexOf(')');

                if (openParenIndex != -1 && closeParenIndex != -1) {
                    // Extract the function name
                    String functionName = value.substring(0, openParenIndex);

                    // Extract the parameter
                    String parameters = value.substring(openParenIndex + 1, closeParenIndex);

                    // Split the parameter if needed
                    String[] parameterParts = parameters.split(",");



                    params.clear();
                    for (String parameter : parameterParts) {
                        expressionWithFunc temp = new expressionWithFunc(m_util);
                        temp.convertValueInString(parameter.trim());
                        params.add(temp);
                    }

                    if(functionName.equals("evaluate") || functionName.equals("ticks")){
                        if(params.get(0).getType() != expressionType.STRING){
                            throw new InvalidValue("func evaluate got wrong type of arg");
                        }
                        String pram = params.get(0).getString();
                        int separatorIndex = pram.indexOf(".");
                        entityName = pram.substring(0, separatorIndex);
                        propertyName = pram.substring(separatorIndex + 1);
                    }

                    if(!ifFunctionValid(functionName)){
                        return;
                    }
                    setValue(functionName);
                    m_isFunc = true;

                }




                /*
                String trimmedInput = value.trim().replace("(", "").replace(")", "");
                String[] parts = trimmedInput.split("\\(");

                if (parts.length == 2) {
                    String functionName = parts[0];
                    String[] parameters = parts[1].split(",");

                    setValue(functionName);
                    m_isFunc = true;
                    // Now you have the function name and parameters

                    for (String parameter : parameters) {
                        exprecn temp = new exprecn();
                        temp.convertValueInString(parameter);
                        params.add(temp);
                    }
                    return; // No need to proceed further
                }
                */

            }
        }
    }

    private boolean ifFunctionValid(String name){
        if(name.equals("environment")){
            if(params.size() == 1 && m_util.isEnvironmentExist(params.get(0).getString())){
                return true;
            }
            throw new InvalidValue("bad arg to fun environment");
        } else if (name.equals("random")) {
            if(params.size() == 1 && (params.get(0).getType() == expressionType.INT || params.get(0).getType() == expressionType.STRING)){
                return true;
            }
            throw new InvalidValue("bad arg to fun random");
        }else if(name.equals("evaluate")){
            if(params.size() == 1 && m_util.isEntityDifenichanExists(entityName) && m_util.isPropertyExists(entityName, propertyName)){
                return true;
            }
            throw new InvalidValue("bad arg to fun evaluate");
        } else if (name.equals("percent")) {
            return true;
        } else if (name.equals("ticks")) {
            if(params.size() == 1 && m_util.isEntityDifenichanExists(entityName) && m_util.isPropertyExists(entityName, propertyName)){
                return true;
            }
        }
        throw new InvalidValue("Not an existing function");
        //return false;
    }

    @Override
    public void setValue(int value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(boolean value) {
        super.setValue(value);
        m_isFunc = false;
    }

    public expressionWithFunc getParams(int i){
        return params.get(i);
    }

    public expressionWithFunc decipherValue(Entity entity, Utilites util, int currTick) {
        expressionWithFunc res = new expressionWithFunc(m_util);
        if (this.getType() == expressionType.STRING) {
            if (this.isFunc()) {
                if (this.getString().equals("environment")) {
                    this.getParams(0).setEntityParams(this.EntityPramsToFunc);
                    res.setValue(util.environment(this.getParams(0).decipherValue(entity, util, currTick).getString()));
                } else if (this.getString().equals("random")) {
                    this.getParams(0).setEntityParams(this.EntityPramsToFunc);
                    expressionWithFunc temp = this.getParams(0).decipherValue(entity, util, currTick);
                    if(temp.getType() != expressionType.INT){
                        throw new InvalidValue("wrong type of arg in random func");
                    }
                    res.setValue(util.random(temp.getInt()));
                } else if (this.getString().equals("evaluate")) {
                    Entity param = getEntityAsParam("evaluate");
                    res.setValue(param.getProperties().get(propertyName).getValue());
                } else if (this.getString().equals("percent")) {
                    this.getParams(0).setEntityParams(this.EntityPramsToFunc);
                    this.getParams(1).setEntityParams(this.EntityPramsToFunc);
                    res.setValue(percent(this.getParams(0).decipherValue(entity, util, currTick), this.getParams(1).decipherValue(entity, util, currTick)));
                } else if (this.getString().equals("ticks")) {
                    Entity param = getEntityAsParam("ticks");
                    res.setValue(currTick - param.getProperties().get(propertyName).getLastTickChanged());
                }
            } else {
                if (entity.isPropertyExists(this.getString())) {
                    res.setValue(entity.getProperty(this.getString()).getValue());
                } else {
                    res = this;
                }
            }
        }else {
            res = this;
        }
        return res;
    }

    private float percent(expressionWithFunc hole, expressionWithFunc percent){
        if(hole.getType() == expressionType.INT && percent.getType() == expressionType.INT){
            return m_util.percent(hole.getInt(), percent.getInt());
        } else if (hole.getType() == expressionType.FLOAT && percent.getType() == expressionType.INT){
            return m_util.percent(hole.getFloat(), percent.getInt());
        } else if (hole.getType() == expressionType.INT && percent.getType() == expressionType.FLOAT){
            return m_util.percent(hole.getInt(), percent.getFloat());
        } else if (hole.getType() == expressionType.FLOAT && percent.getType() == expressionType.FLOAT){
            return m_util.percent(hole.getFloat(), percent.getFloat());
        }
        throw new InvalidValue("wrong type of arg in random func");
    }

    private Entity getEntityAsParam(String funcName){
        for(Entity entityPrams : EntityPramsToFunc){
            if(entityPrams.getName().equals(entityName)){
                return entityPrams;
            }
        }
        InvalidValue e = new InvalidValue("wrong entity in arg to " + funcName + " func");
        e.setEntityName(entityName);
        throw e;
        //throw new InvalidValue("wrong entity in arg to " + funcName + " func");
    }

    public boolean checkEvaluateIsNumber(){
        if(!getString().equals("evaluate")){
            throw new InvalidValue("evaluate func not correct");
        }
        expressionType type = m_util.getPropertyType(entityName, propertyName);
        if(type == expressionType.FLOAT || type == expressionType.INT){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        if(!isFunc()){
            return super.toString();
        }
        StringBuilder result = new StringBuilder();
        result.append(getValue().toString() + "(");
        for(int i = 0; i < params.size() - 1 ; i++){
            result.append(params.get(i) + ", ");
        }
        if(params.size() != 0){
            result.append(params.get(params.size() - 1));
        }
        result.append(")");
        return result.toString();
    }

//    @Override
//    public String toString(){
//        return getValue().toString();
//    }
}
