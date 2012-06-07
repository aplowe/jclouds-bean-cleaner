package org.jclouds.cleanup.simpletest;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Map;
import java.util.Set;

/**
 * Class comment of ThirdChild
 * => Expect both getAttX() and getAttY() to be commented.
 * => Expect attX and elemY AND elemZ to have the @Named attributes in the new class!
 */
public class ThirdChild extends Parent<ThirdChild> {

   public Set<String> attX;

   /**
    * @return the Y!
    */
   @XmlAttribute(name="YName")
   private Multimap<String, Parent> elemY;

   /**
    * @return the Z
    */
   private Optional<String> elemZ;

   /**
    * @return the X!
    */
   public Set<String> getAttX() {
      return attX;
   }

   public Multimap<String, Parent> getAttY() {
      return elemY;
   }

   @Named("ZName")
   public Optional<String> getElemZ() {
      return elemZ;
   }

   @Inject
   public ThirdChild(@Named("Xenon") Set<String> attX) {
      this.attX = attX;
   }
}
