package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import Engine.world.entity.property.PropertyInterface;

import java.util.List;
import java.util.Map;


public interface ActionInterface {
    //public void setEntityAndProperty(Entity e, PropertyInterface p);
    public String getEntityName();
    public String getPropertyName();
    public String getRuleName();
    public String getActionName();
    public Map<String, List<Entity>> activateAction(Entity entity, int currTick, List<Entity> paramsForFuncs)throws InvalidValue;
    public boolean setValues(PropertyInterface v1, PropertyInterface v2);
    public String getName();
    public boolean isSecondaryAll();
    public int getCountForSecondaryEntities();
    public String getSecondaryName();
    public single getCondition();
    public DTOActionData makeActionDto();

    public action clone(Utilites util, String ruleName);
    public String getType();
    public String getEntity();
}
