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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * BaseObject
 * 
 * @author Adam Lowe
 */
public abstract class BaseObject {
   protected final String type;
   protected final List<String> annotations;
   protected final List<String> javadocComment;

   public BaseObject(String type, Collection<String> annotations, Collection<String> javadocComment) {
      this.type = type;
      this.annotations = Lists.newArrayList(annotations);
      this.javadocComment = ImmutableList.copyOf(javadocComment);
   }

   protected void addAnnotation(String annotation) {
      if (!annotations.contains(annotation)) {
         annotations.add(annotation);
      }
   }

   public String getType() {
      return type;
   }

   public List<String> getAnnotations() {
      return annotations;
   }

   public Collection<String> getJavadocComment() {
      return javadocComment;
   }
}
