package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.generated.PRDCondition;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class multiple implements subCondition, Serializable {

    enum logicalOp{ OR, AND}
    logicalOp m_logical;
    List<subCondition> m_conditions;

    public multiple(PRDCondition condition, Utilites util) throws InvalidValue {
        m_conditions = new ArrayList<>();
        if(condition.getLogical().equals("or")){
            m_logical = logicalOp.OR;
        }
        else if(condition.getLogical().equals("and")){
            m_logical = logicalOp.AND;
        }
        for(PRDCondition subCondition : condition.getPRDCondition()){
            if(subCondition.getSingularity().equals("multiple")){
                m_conditions.add(new multiple(subCondition, util));
            } else if (subCondition.getSingularity().equals("single")) {
                m_conditions.add(new single(subCondition, util));
            }
        }
    }

    public multiple(multiple condition, Utilites util) throws InvalidValue {
        m_conditions = new ArrayList<>();
        m_logical = condition.getLogical();

        for(subCondition subCondition : condition.getConditions()){
            if(subCondition.getSingularity().equals("multiple")){
                m_conditions.add(subCondition.clone(util));
            } else if (subCondition.getSingularity().equals("single")) {
                m_conditions.add(subCondition.clone(util));
            }
        }
    }

    public subCondition clone(Utilites util){
        return new multiple(this, util);
    }

    public String getSingularity(){
        return "multiple";
    }

    public List<subCondition> getConditions(){
        return m_conditions;
    }

    public logicalOp getLogical(){
        return m_logical;
    }

    @Override
    public boolean getBoolValue(Entity entity, int currTick)throws InvalidValue{
        if(m_logical == logicalOp.AND){
            for(subCondition condition : m_conditions){
                if(condition.getBoolValue(entity, currTick) == false){
                    return false;
                }
            }
            return true;
        } else if (m_logical == logicalOp.OR) {
            for(subCondition condition : m_conditions){
                if(condition.getBoolValue(entity, currTick) == false){
                    return true;
                }
            }
            return false;
        }
        else{ //do not get to here
            return false;
        }
    }
    @Override
    public boolean shouldIgnore(Entity entity){
        for(subCondition condition : m_conditions){
            if(!condition.shouldIgnore(entity)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean getBoolValue(Entity entity, Entity secondaryEntity, int currTick){
        if(m_logical == logicalOp.AND){
            for(subCondition condition : m_conditions){
                if(!condition.shouldIgnore(entity) && condition.getBoolValue(entity, secondaryEntity, currTick) == false){
                    return false;
                }
            }
            return true;
        } else if (m_logical == logicalOp.OR) {
            for(subCondition condition : m_conditions){
                if(!condition.shouldIgnore(entity) && condition.getBoolValue(entity, secondaryEntity, currTick) == false){
                    return true;
                }
            }
            return false;
        }
        else{ //do not get to here
            return false;
        }
    }
    @Override
    public void makeActionDto(DTOActionData actionData) {
        actionData.putData("condition", "multiple");
        actionData.putData("logic", m_logical.toString());
        actionData.putData("logic size", ((Integer)(m_conditions.size())).toString());
    }


    }
