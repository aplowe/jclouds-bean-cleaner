package org.jclouds.cleanup.simpletest;

import org.jclouds.javax.annotation.Nullable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class comment of Parent.
 * A few lines....
 * Long
 */
public class Parent<T extends Parent<T>> extends GrandParent<T> {
   @Nullable
   public String parentAtt;

   @Nullable
   private String parentElem;

   private String parentReqElem;
}
