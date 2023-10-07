package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;
import java.util.*;

public class condition extends action implements Serializable {
    private String m_entity;
    private subCondition m_subCon;
    private List<ActionInterface> m_then = null;
    private List<ActionInterface> m_else = null;
    //private int m_currTick;

//    private boolean isSecondaryAll = false;
//    private int countForSecondaryEntities = 0;
//    private String m_secondaryEntity;
//    private single condition = null;

    public condition(PRDAction action, Utilites util, String ruleName) throws InvalidValue {
        super(action, util, ruleName);
        m_entity = action.getEntity();
        //actionName = action.getType();
//        if(action.getPRDSecondaryEntity() != null){
//            m_secondaryEntity = action.getPRDSecondaryEntity().getEntity();
//            expression temp = new expression();
//            temp.convertValueInString(action.getPRDSecondaryEntity().getPRDSelection().getCount());
//            if(temp.getType() == expressionType.INT){
//                countForSecondaryEntities = temp.getInt();
//            }
//            else{
//                isSecondaryAll = true;
//            }
//            if(action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition() != null){
//                condition = new single(action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition(), util);
//            }
//        }
        if(action.getPRDCondition().getSingularity().equals("multiple")){
            m_subCon = new multiple(action.getPRDCondition(), util);
        }
        else if (action.getPRDCondition().getSingularity().equals("single")){
            m_subCon = new single(action.getPRDCondition(), util);
        }
        actionFactory factory = new actionFactory();
        m_then = new ArrayList<>();
        for(PRDAction thanAction : action.getPRDThen().getPRDAction()){
            m_then.add(factory.makeAction(thanAction, util, getRuleName()));
        }
        if(action.getPRDElse() != null){
            m_else = new ArrayList<>();
            for(PRDAction elseAction : action.getPRDElse().getPRDAction()){
                m_else.add(factory.makeAction(elseAction, util, getRuleName()));
            }
        }
        checkEntityAndPropertyExist(util);
    }

    public condition(condition action, Utilites util, String ruleName) throws InvalidValue {
        super(action, util, ruleName);
        m_entity = action.getEntityName();
        m_subCon = action.getSubCondition().clone(util);
//        if(action.getPRDCondition().getSingularity().equals("multiple")){
//            m_subCon = new multiple(action.getPRDCondition(), util);
//        }
//        else if (action.getPRDCondition().getSingularity().equals("single")){
//            m_subCon = new single(action.getPRDCondition(), util);
//        }
        actionFactory factory = new actionFactory();
        m_then = new ArrayList<>();


        for(ActionInterface thanAction : action.getThen()){
            m_then.add(thanAction.clone(util, ruleName));
        }


        if(action.isHaveElse()){
            m_else = new ArrayList<>();
            for(ActionInterface elseAction : action.getElse()){
                m_else.add(elseAction.clone(util, ruleName));
            }
        }
        checkEntityAndPropertyExist(util);
    }

    @Override
    public action clone(Utilites util, String ruleName){
        return new condition(this, util, ruleName);
    }

    public Boolean isHaveElse(){
        return m_else != null;
    }

    public List<ActionInterface> getThen(){
        return m_then;
    }

    public List<ActionInterface> getElse(){
        return m_else;
    }

    public subCondition getSubCondition(){
        return m_subCon;
    }
    private void checkEntityAndPropertyExist(Utilites util){
        if(!util.isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + m_entity + " does not exist.");
        }
        if(getCountForSecondaryEntities() != 0 && !util.isEntityDifenichanExists(getSecondaryName())){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + getSecondaryName() + " does not exist.");
        }
    }
    public void doCondition(){

    }
    private Map<String, List<Entity>> loopThroughEntities(Entity entity, List<Entity> secondaryEntities){
        List<Entity> passCondition = new ArrayList<>();
        List<Entity> didntPassCondition = new ArrayList<>();

        for(Entity secondaryEntity : secondaryEntities){
            if(m_subCon.getBoolValue(entity, secondaryEntity, m_currTick)){
                passCondition.add(secondaryEntity);
            }else {
                didntPassCondition.add(secondaryEntity);
            }
        }

        Map<String, List<Entity>> killAndCreat = new HashMap<>();

        if(passCondition.size() != 0){
            m_then.stream().forEach(actionInterface -> activateActionWithSecondary(actionInterface, entity, passCondition).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            })));
        }

        if(m_else != null && didntPassCondition.size() != 0){
            m_then.stream().forEach(actionInterface -> activateActionWithSecondary(actionInterface, entity, didntPassCondition).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            })));
        }

        return killAndCreat;
    }

    private Map<String, List<Entity>> activeOnce(Entity entity){
        Map<String, List<Entity>> killAndCreat = new HashMap<>();
        if(m_subCon.shouldIgnore(entity)){
            return killAndCreat;
        }
        if (m_subCon.getBoolValue(entity, m_currTick)) {
            m_then.stream().forEach(actionInterface -> activateActionWithSecondary(actionInterface,entity, new ArrayList<>()).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            })));
//            for (ActionInterface action : m_then) {
                //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
//                if(action.activateAction(entity, temp)){
//                    return true;
//                }
                //}
            //}
        } else {
            if (m_else != null) {
                m_else.stream().forEach(actionInterface -> activateActionWithSecondary(actionInterface, entity, new ArrayList<>()).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                })));
//                for (ActionInterface action : m_else) {
//                    //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
//                    if(action.activateAction(entity, temp)){
//                        return true;
//                    }
//                    //}
//                }
            }
        }
        return killAndCreat;
    }

    private Map<String, List<Entity>> activateActionWithSecondary(ActionInterface action, Entity entity, List<Entity> secondaries){
        Map<String, List<Entity>> killAndCreat = new HashMap<>();
        try {
            if (action.getEntityName().equals(entity.getName())) {
                if (secondaries == null || secondaries.size() == 0) {
                    return action.activateAction(entity, m_currTick, new ArrayList<>(Arrays.asList(entity)));
                } else {
                    secondaries.stream().forEach(secondary -> action.activateAction(entity, m_currTick, new ArrayList<>(Arrays.asList(entity, secondary))).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    })));
                    return killAndCreat;
                }

            } else {
                secondaries.stream().forEach(secondary -> action.activateAction(secondary, m_currTick, new ArrayList<>(Arrays.asList(secondary, entity))).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                })));
                return killAndCreat;
            }
        } catch (InvalidValue e){
        if(e.getEntityName() != null){
            if(!e.getEntityName().equals(m_entity) && !e.getEntityName().equals(getSecondaryName())) {
                throw e;
            }
        }
    }
        return killAndCreat;
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int currTick, List<Entity> paramsForFuncs) throws InvalidValue{
        m_currTick = currTick;
        List<Entity> secondaryEntities = null;
        if(getCountForSecondaryEntities() != 0){
            secondaryEntities = getSecondaryEntities();
        }
        if(secondaryEntities == null){  //TODO add && secondaryEntities.size() == 0 if want to activate condition even if no secondaries
            return activeOnce(entity);
        }else{
            return loopThroughEntities(entity, secondaryEntities);
        }

//        if (m_subCon.getBoolValue(entity)) {
//            for (ActionInterface action : m_then) {
//                //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
//                if(action.activateAction(entity, secondaryEntities)){
//                    return true;
//                }
//                //}
//            }
//        } else {
//            if (m_else != null) {
//                for (ActionInterface action : m_else) {
//                    //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
//                    if(action.activateAction(entity, secondaryEntities)){
//                        return true;
//                    }
//                    //}
//                }
//            }
//        }
//
//        return false;
    }

    @Override
    public String getEntityName(){
        return m_entity;
    }

//    public boolean isSecondaryAll() {
//        return isSecondaryAll;
//    }
//
//    public int getCountForSecondaryEntities(){
//        return countForSecondaryEntities;
//    }
//
//    public String getSecondaryName(){
//        return m_secondaryEntity;
//    }
//
//    public single getCondition(){
//        return condition;
//    }

    @Override
    public DTOActionData makeActionDto(){
        DTOActionData actionData = new DTOActionData(getActionName());
        actionData.putData("entity", m_entity);
        actionData.putData("then", ((Integer)(m_then.size())).toString());
        actionData.putData("else", ((Integer)(m_else!=null? m_else.size() : 0)).toString());
        actionData.putData("secondary", getSecondaryName());
        m_subCon.makeActionDto(actionData);

        return actionData;
    }
}
