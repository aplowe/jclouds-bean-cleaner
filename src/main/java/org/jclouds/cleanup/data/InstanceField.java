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

import java.util.Collection;

/**
 * A normal field of a Bean
 */
public class InstanceField extends Field {
   private final boolean nullable;
   private final String serializedName;
   private final String accessorName;

   public InstanceField(String name, String serializedName, String accessorName, String type, boolean nullable, Collection<String> annotations, Collection<String> javadocComment) {
      super(name, type, annotations, javadocComment);
      this.nullable = nullable;
      this.accessorName = accessorName;
      this.serializedName = serializedName;
   }
   
   public boolean isNullable() {
      return !isPrimative() && nullable;
   }

   public String getSerializedName() {
      return serializedName == null ? getName() : serializedName;
   }

   public String getAccessorName() {
      return accessorName == null ? getName() : accessorName;
   }
}
