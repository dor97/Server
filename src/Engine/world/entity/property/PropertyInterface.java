package Engine.world.entity.property;

import DTO.DTOEnvironmentVariablesValues;

import java.util.List;

public interface PropertyInterface {

    public propertyType getType();
    public void addToProperty(int add, int currTick);
    public void addToProperty(float add, int currTick);
    public String getName();
    public Object getValue();
    public void setProperty(int v, int currTick);
    public void setProperty(float v, int currTick);
    public void setProperty(boolean v, int currTick);
    public void setProperty(String v, int currTick);
    public DTOEnvironmentVariablesValues makeDtoEnvironment();
    public int getLastTickChanged();
    public List<Integer> getDeltaTicksChangedValue();
    public Float getDeltaTicksChangedValueAve();
    public void addDeltaTicksChanged(Integer delta);


}
