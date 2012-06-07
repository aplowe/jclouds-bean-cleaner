/*
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.cleanup.data;

import com.google.common.base.Objects;

import java.util.Collection;

/**
 * A normal field of a Bean
 */
public class InstanceField extends Field {
   public static final String[] booleanAccesorNames = {"is", "may", "can", "should", "cannot", "not"};
   private final boolean nullable;
   private String serializedName;

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

   public String getSerializedName() {
      return serializedName;
   }

   public void setSerializedName(String serializedName) {
      this.serializedName = serializedName;
   }
}
