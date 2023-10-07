package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;

public interface subCondition {
    public boolean getBoolValue(Entity entity, int currTick) throws InvalidValue;
    public boolean getBoolValue(Entity entity, Entity secondaryEntity, int currTick) throws InvalidValue;

    public boolean shouldIgnore(Entity entity);

    public void makeActionDto(DTOActionData actionData);
    public String getSingularity();
    public subCondition clone(Utilites util);

}
