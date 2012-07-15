package org.jclouds.cleanup.simpletest;

import org.jclouds.javax.annotation.Nullable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;
import java.util.Set;

/**
 * Class comment of SecondChild
 * => Expect both getAttX() and getAttY() to be commented.
 */
public class SecondChild extends Parent {

    public Set<String> attX;

    /**
     * @return the Y!
     */
    private Map<String, Parent> elemY;

    /**
     * @return the X!
     */
    public Set<String> getAttX() {
        return attX;
    }

    public Map<String, Parent> getAttY() {
        return elemY;
    }

}
