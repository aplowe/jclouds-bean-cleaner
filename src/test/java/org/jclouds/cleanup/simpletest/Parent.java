package org.jclouds.cleanup.simpletest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class comment of Parent.
 * A few lines....
 * Long
 */
public class Parent<T extends Parent<T>> extends GrandParent<T> {

    @XmlAttribute
    public String parentAtt;
    
    @XmlElement
    private String parentElem;

    @XmlElement(required = true)
    private String parentReqElem;

}
