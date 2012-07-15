package org.jclouds.cleanup.simpletest;

import org.jclouds.javax.annotation.Nullable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class comment of Parent.
 * A few lines....
 * Long
 */
public class Parent extends GrandParent {
   @Nullable
   public String parentAtt;

   @Nullable
   private String parentElem;

   private String parentReqElem;
}
