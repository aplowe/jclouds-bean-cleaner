package org.jclouds.cleanup.data;

import java.util.Collection;

/**
 * Base
 */
public class InnerClass extends BaseObject {
   private final Collection<String> content;
   private final String modifiers;

   public InnerClass(String modifiers, String type, Collection<String> annotations, Collection<String> javadocComment, Collection<String> content) {
      super(type, annotations, javadocComment);
      this.modifiers = modifiers;
      this.content = content;
   }

   public Collection<String> getContents() {
      return content;
   }

   public String getModifiers() {
      return modifiers;
   }
}
