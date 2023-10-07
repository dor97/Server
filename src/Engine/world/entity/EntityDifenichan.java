package Engine.world.entity;

import DTO.DTOEntityData;
import DTO.DTOEntitysProperties;
import DTO.DTOProperty;
import Engine.InvalidValue;
import Engine.allReadyExistsException;
import Engine.generated.PRDEntity;
import Engine.generated.PRDProperty;
import Engine.world.entity.property.propertyDifenichan;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityDifenichan implements Serializable {
    private String m_name;
    private int m_amount;
    private Map<String, propertyDifenichan> m_propertys;

    public EntityDifenichan(PRDEntity e) throws allReadyExistsException, InvalidValue{
        m_name = e.getName();
        //m_amount = e.getPRDPopulation();
        m_amount = 0; //TODO change to zero
        m_propertys = new HashMap<>();
        for(PRDProperty p : e.getPRDProperties().getPRDProperty()){
            if(m_propertys.containsKey(p.getPRDName())){
                throw new allReadyExistsException("property varuble " + p.getPRDName() + " all ready exists in entity" + e.getName());
            }
            try {
                m_propertys.put(p.getPRDName(), new propertyDifenichan(p));
            }catch (InvalidValue invalidValue){
                throw new InvalidValue(invalidValue.getMessage() + ". referred in entity " + m_name);
            }
        }
    }

    public EntityDifenichan(EntityDifenichan e){
        m_name = e.getName();
        //m_amount = e.getPRDPopulation();
        m_amount = e.getAmount();
        m_propertys = new HashMap<>();
        for(propertyDifenichan p : e.getPropertys().values()){
            if(m_propertys.containsKey(p.getName())){
                throw new allReadyExistsException("property varuble " + p.getName() + " all ready exists in entity" + e.getName());
            }
            try {
                m_propertys.put(p.getName(), new propertyDifenichan(p));
            }catch (InvalidValue invalidValue){
                throw new InvalidValue(invalidValue.getMessage() + ". referred in entity " + m_name);
            }
        }
    }

    public void addProperty(propertyDifenichan propertyToAdd){
        m_propertys.put(propertyToAdd.getName(), propertyToAdd);
    }

    public void setPopulation(Integer population){
        m_amount = population;
    }

    public int getAmount(){
        return m_amount;
    }
    public String getName(){
        return  m_name;
    }

    public Map<String, propertyDifenichan> getPropertys(){
        return  m_propertys;
    }

    public DTOEntityData makeDtoEntity(){
        DTOEntityData DTO = new DTOEntityData(m_name, m_amount);

        for(propertyDifenichan property : m_propertys.values()){
            DTO.addProperty(property.makeDtoProperty());
        }

        return DTO;
    }

    public DTOEntitysProperties makeDtoEntitysProperties(){
        List<DTOProperty> properties = m_propertys.values().stream().map(propertyDifenichan -> new DTOProperty(propertyDifenichan.getName())).collect(Collectors.toList());
        return new DTOEntitysProperties(m_name, properties);
    }

    public Integer getPopulation(){
        return m_amount;
    }

}
