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

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * BaseObject
 */
public abstract class BaseObject {
   protected final String type;
   protected final List<String> annotations = Lists.newArrayList();
   protected final List<String> javadocComment = Lists.newArrayList();

   public BaseObject(String type, Collection<String> annotations, Collection<String> javadocComment) {
      this.type = type;
      addAnnotations(annotations);
      this.javadocComment.addAll(javadocComment);
   }

   public String getType() {
      return type;
   }

   public List<String> getAnnotations() {
      return annotations;
   }

   public void addAnnotations(Collection<String> anno) {
      this.annotations.addAll(anno);
   }

   public void adjustJavaDoc(Collection<String> otherJavadocComment) {
      // TODO order these!
      this.javadocComment.addAll(otherJavadocComment);
   }

   public Collection<String> getJavadocComment() {
      return javadocComment;
   }
}
