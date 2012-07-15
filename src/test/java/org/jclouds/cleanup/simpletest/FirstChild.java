package org.jclouds.cleanup.simpletest;

import org.jclouds.javax.annotation.Nullable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Set;

/** Class comment for FirstChild - all on one line */
public class FirstChild extends GrandParent {

    @Nullable
    public String attX;
    
    private List<Set<String>> elemY;
}
