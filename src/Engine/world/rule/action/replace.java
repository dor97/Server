package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.util.*;
import java.util.stream.Collectors;

public class replace extends action{
    private String m_entityName;
    private String toCreate;
    private Entity m_entity = null;
    private String mode;
    private Utilites m_util;

    public replace(PRDAction action, Utilites util, String ruleName){
        super(action, util, ruleName);
        m_entityName = action.getKill();
        toCreate = action.getCreate();
        m_util = util;
        //actionName = action.getType();
        mode = action.getMode();
        checkEntityAndPropertyExist();
        if(!mode.equals("scratch") && !mode.equals("derived")){
            throw new InvalidValue("In action replace mode is not defined");
        }
    }

    public replace(replace action, Utilites util, String ruleName){
        super(action, util, ruleName);
        m_entityName = action.getToKill();
        toCreate = action.getToCreate();
        m_util = util;
        //actionName = action.getType();
        mode = action.getMode();
        checkEntityAndPropertyExist();
        if(!mode.equals("scratch") && !mode.equals("derived")){
            throw new InvalidValue("In action replace mode is not defined");
        }
    }

    public replace clone(Utilites util, String ruleName){
        return new replace(this, util, ruleName);
    }

    public String getMode(){
        return mode;
    }

    private String getToCreate(){
        return toCreate;
    }

    public String getToKill(){
        return m_entityName;
    }

    private void checkEntityAndPropertyExist(){
        if(!m_util.isEntityDifenichanExists(m_entityName)){
            throw new OBJECT_NOT_EXIST("In action replace the entity " + m_entityName + " does not exist.");
        }
        if(!m_util.isEntityDifenichanExists(toCreate)){
            throw new OBJECT_NOT_EXIST("In action replace the entity " + toCreate + " does not exist.");
        }
        if(getCountForSecondaryEntities() != 0 && !m_util.isEntityDifenichanExists(getSecondaryName())){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + getSecondaryName() + " does not exist.");
        }
    }

    private Entity creatEntity(Entity entity){
        if(mode.equals("scratch")){
            return new Entity(m_util.getEntityDifenichan(toCreate));
        }
        else{
            return new Entity(m_util.getEntityDifenichan(toCreate), entity.getProperties());
        }
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int m_currTick, List<Entity> paramsForFuncs){
        if(entity.isDead()){
            return new HashMap<>();
        }
        entity.setIsDead(true);
        List<Entity> secondaryEntities = null;
        if(getCountForSecondaryEntities() != 0 && !getSecondaryName().equals(m_entityName)){
            secondaryEntities = getSecondaryEntities();
        }
        Map<String, List<Entity>> killAndCreat = new HashMap<>();
        if(secondaryEntities == null){
            killAndCreat.put("kill", new ArrayList<>(Arrays.asList(entity)));
            killAndCreat.put("creat", new ArrayList<>(Arrays.asList(creatEntity(entity))));
            //loopThroughEntities(entity);
        }else{
            if(m_entityName.equals(entity.getName()) || secondaryEntities.size() != 0){
                killAndCreat.put("kill", new ArrayList<>(Arrays.asList(entity)));
                killAndCreat.put("creat", new ArrayList<>(Arrays.asList(creatEntity(entity))));
            }else {
                killAndCreat.put("kill", secondaryEntities);
                killAndCreat.put("creat", secondaryEntities.stream().limit(m_util.getAmountOfFreeSpace() + 1).map(secondaryEntity -> creatEntity(secondaryEntity)).collect(Collectors.toList()));

            }
        }
        return killAndCreat;
    }

    @Override
    public DTOActionData makeActionDto(){
        DTOActionData actionData = new DTOActionData(getActionName());
        actionData.putData("entity", m_entityName);
        actionData.putData("create", toCreate);
        actionData.putData("mode", mode);
        actionData.putData("secondary", getSecondaryName());

        return actionData;
    }
}
