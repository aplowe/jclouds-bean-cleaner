package org.jclouds.cleanup.data;

import com.google.common.base.Objects;

import java.util.Collection;

/**
 * Base
 */
public class InstanceField extends Field {
   public static final String[] booleanAccesorNames = {"is", "may", "can", "should", "cannot", "not"};
   private boolean nullable;

   public InstanceField(String name, String type, boolean nullable, Collection<String> annotations, Collection<String> javadocComment) {
      super(name, type, annotations, javadocComment);
      this.nullable = nullable;
   }

   // TODO this should probably be determined beforehand and passed to constructor!
   public String getAccessorName() {
      String fieldNameUC = getName().substring(0, 1).toUpperCase() + getName().substring(1);
      String getterName = null;
      if (Objects.equal(getType().toLowerCase(), "boolean")) {
         for (String starter : booleanAccesorNames) {
            if (getName().startsWith(starter)) {
               getterName = getName();
            }
         }
         if (getterName == null) {
            getterName = "is" + fieldNameUC;
         }
      } else {
         getterName = "get" + fieldNameUC;
      }
      return getterName;
   }

   public boolean isNullable() {
      return nullable;
   }
}
