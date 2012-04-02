package org.jclouds.cleanup.simpletest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class comment of SecondChild
 * => Expect both generated getters to be commented.
 */
public class SecondChild extends Parent<SecondChild> {

    @XmlAttribute
    public String attX;

    /**
     * @return the Y!
     */
    @XmlElement
    private String elemY;

    /**
     * @return the X!
     */
    public String getAttX() {
        return attX;
    }

    public String getAttY() {
        return attX;
    }

}
