package Engine.world.entity.property;

import DTO.DTOPropertyData;
import DTO.DTOPropertyType;
import Engine.InvalidValue;
import Engine.generated.PRDProperty;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;

import java.io.Serializable;

public class propertyDifenichan implements Serializable {
    private String m_name;
    private expressionType m_type;
    private Double m_lowRange, m_highRang;
    private Boolean m_randomlyIneceat;
    private expression m_init;
    private Boolean haveRange = false;

    public propertyDifenichan(PRDProperty p) throws InvalidValue{
        m_name = p.getPRDName();
        m_type = myType(p.getType());
        if(p.getPRDRange() != null) {
            haveRange = true;
            m_lowRange = p.getPRDRange().getFrom();
            m_highRang = p.getPRDRange().getTo();
            if(m_highRang < m_lowRange){
                throw new InvalidValue("In property " + m_name + " the value in 'to' is bigger than the one in 'from'.");
            }
        }
        m_randomlyIneceat = p.getPRDValue().isRandomInitialize();
        if(m_randomlyIneceat == true){
            return;
        }
        m_init = new expression();
        m_init.convertValueInString(p.getPRDValue().getInit());
        if(m_type != m_init.getType() && (!(m_type == expressionType.FLOAT && m_init.getType() == expressionType.INT))){
            throw new InvalidValue("In property " + m_name + " got a wrong type value");
        }

    }

    public propertyDifenichan(propertyDifenichan p) throws InvalidValue{
        m_name = p.getName();
        m_type =p.getType();
        if(p.haveRange()) {
            haveRange = true;
            m_lowRange = p.getLowRange();
            m_highRang = p.getHighRange();
            if(m_highRang < m_lowRange){
                throw new InvalidValue("In property " + m_name + " the value in 'to' is bigger than the one in 'from'.");
            }
        }
        m_randomlyIneceat = p.isRandom();
        if(m_randomlyIneceat == true){
            return;
        }
        m_init = new expression();
        m_init.convertValueInString(p.getInit().getValue().toString());
        if(m_type != m_init.getType() && (!(m_type == expressionType.FLOAT && m_init.getType() == expressionType.INT))){
            throw new InvalidValue("In property " + m_name + " got a wrong type value");
        }

    }

    public expressionType getType(){
        return m_type;
    }

    public double getLowRange(){
        return m_lowRange;
    }

    public double getHighRange(){
        return m_highRang;
    }

    public expression getInit(){
        return m_init;
    }

    public boolean isRandom(){
        return m_randomlyIneceat;
    }

    public boolean haveRange(){
        return haveRange;
    }

    public String getName(){
        return m_name;
    }

    private expressionType myType(String t){
        if(t.equals("decimal")){
            return expressionType.INT;
        } else if (t.equals("float")) {
            return expressionType.FLOAT;
        } else if (t.equals("string")) {
            return expressionType.STRING;
        } else if (t.equals("boolean")) {
            return expressionType.BOOL;
        }
        else{
            return  null;
            //exepcen
        }
    }

    public DTOPropertyData makeDtoProperty(){
        DTOPropertyType type;
        if(m_type == expressionType.INT) {
            type = DTOPropertyType.INT;
        } else if (m_type == expressionType.FLOAT) {
            type = DTOPropertyType.FLOAT;
        } else if (m_type == expressionType.STRING) {
            type = DTOPropertyType.STRING;
        } else {
            type = DTOPropertyType.BOOL;
        }
        DTOPropertyData DTO;
        if(haveRange){
            DTO = new DTOPropertyData(m_name, type, haveRange, m_randomlyIneceat, m_highRang, m_lowRange);
        }
        else{
            DTO = new DTOPropertyData(m_name, type, haveRange, m_randomlyIneceat);
        }

        DTO.putData("Name", m_name);
        DTO.putData("Type", type.toString());
        DTO.putData("HaveRange", haveRange.toString());
        if(haveRange){
            DTO.putData("lowRange", m_lowRange.toString());
            DTO.putData("highRange", m_highRang.toString());
        }
        DTO.putData("Random", m_randomlyIneceat.toString());
        return DTO;
    }

}
