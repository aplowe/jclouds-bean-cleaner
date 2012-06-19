package org.jclouds.cleanup.docs;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Class comment for Docs example
 * A few lines....
 * Long
 */
public class Example {
   /**
    * The mime-type
    */
   public static final String TYPE = "com.example.exampleType";

   /**
    * @return name is required
    */
   @XmlAttribute(required = true)
   protected String name;

   /**
    * @return attribute is not null
    */
   @XmlAttribute(required = true)
   protected String id;

   /**
    * Description of the example object
    */
   @XmlAttribute
   protected String description;
}
