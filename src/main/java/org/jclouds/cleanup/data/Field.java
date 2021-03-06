/*
 * Copyright 2013 Cloudsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.cleanup.data;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for a field of a Bean
 * 
 * @author Adam Lowe
 */
public abstract class Field extends BaseObject {
   private final String name;

   public Field(String name, String type, Collection<String> annotations, Collection<String> javadocComment) {
      super(type, annotations, javadocComment);
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public boolean isPrimative() {
      return ImmutableSet.of("byte", "short", "int", "long", "float", "double", "char", "boolean").contains(getSimpleType());
   }

   public boolean isSet() {
      return Objects.equal(getSimpleType(), "Set");
   }

   public boolean isList() {
      return Objects.equal(getSimpleType(), "List");
   }

   public boolean isMap() {
      return Objects.equal(getSimpleType(), "Map");
   }

   public boolean isOptional() {
      return Objects.equal(getSimpleType(), "Optional");
   }
   
   public boolean isMultimap() {
      return Objects.equal(getSimpleType(), "Multimap");
   }

   public String getSimpleType() {
      return type.replaceAll("^java\\.util\\.", "").replaceAll("<.*>", "");
   }
   
   public String getParameterType() {
      Matcher matchy = Pattern.compile("[^<]*<(.*)>\\s*$").matcher(type);
      if (matchy.matches()) {
         return matchy.group(1);
      } else {
         return "Object";
      }
   }
}
