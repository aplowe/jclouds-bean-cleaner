package org.jclouds.cleanup.simpletest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/** Class comment for FirstChild - all on one line */
public class FirstChild extends GrandParent<FirstChild> {

    @XmlAttribute
    public String attX;
    
    @XmlElement
    private String elemY;
}
