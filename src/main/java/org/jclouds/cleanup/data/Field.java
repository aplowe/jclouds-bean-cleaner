/**
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
 * Field
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

   public boolean isSet() {
      String simpleType = type.replaceAll("<.*>", "").replaceAll(".*\\.", "");
      return Objects.equal(simpleType, "Set");
   }

   public boolean isList() {
      String simpleType = type.replaceAll("<.*>", "").replaceAll(".*\\.", "");
      return Objects.equal(simpleType, "List");
   }

}
