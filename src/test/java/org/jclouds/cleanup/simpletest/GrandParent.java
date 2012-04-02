package org.jclouds.cleanup.simpletest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class comment for GrandParent!
 * A few lines....
 * Long
 */
public class GrandParent<T extends GrandParent<T>> {

    /** @return name is required */
    @XmlAttribute(required=true)
    protected String name;

    /** @return attribute is not null */
    @XmlAttribute(required=true)
    protected String id;

    /** @return this is nullable */
    @XmlAttribute
    protected String description;
    
    @XmlElement(name="Freddy", required=true)
    protected String freddy;

    @XmlElement(name="Brian")
    protected String brian;

    /** This element is NOT required
     * @return the value of brian
     */    
    public String getBrian() {
        return brian;
    }
}
