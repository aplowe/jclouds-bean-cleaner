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

import java.util.Collection;

/**
 * Represents an inner class in a Bean
 * 
 * @author Adam Lowe
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
