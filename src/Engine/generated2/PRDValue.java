//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.22 at 02:36:32 PM IDT 
//


package Engine.generated2;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="random-initialize" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="init" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PRD-value")
public class PRDValue {

    @XmlAttribute(name = "random-initialize", required = true)
    protected boolean randomInitialize;
    @XmlAttribute(name = "init")
    protected String init;

    /**
     * Gets the value of the randomInitialize property.
     * 
     */
    public boolean isRandomInitialize() {
        return randomInitialize;
    }

    /**
     * Sets the value of the randomInitialize property.
     * 
     */
    public void setRandomInitialize(boolean value) {
        this.randomInitialize = value;
    }

    /**
     * Gets the value of the init property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInit() {
        return init;
    }

    /**
     * Sets the value of the init property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInit(String value) {
        this.init = value;
    }

}