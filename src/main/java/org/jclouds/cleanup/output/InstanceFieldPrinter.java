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
package org.jclouds.cleanup.output;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMap;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

public class InstanceFieldPrinter extends ClassDocPrinter {
   private final boolean modifiable;
   private final boolean annotations;
   private final boolean comments;

   public InstanceFieldPrinter(boolean annotations, boolean modifiable, boolean comments) {
      this.modifiable = modifiable;
      this.annotations = annotations;
      this.comments = comments;
   }

   @Override
   public void write(ClassDoc clazz, PrintWriter out) {
      for (FieldDoc field : instanceFields(clazz)) {
         writeField(field, out);
      }
      out.println();
   }

   protected void writeField(FieldDoc field, PrintWriter out) {
      String fieldName = field.name();

      if (comments) {
         writeFieldComment(field, out, ImmutableMap.<String, String>of());
      }

      if (annotations) {
         writeAnnotation(field, out);
      }

      out.println("private " + properTypeName(field) + " " + fieldName + getEqualsEmptyIfCollection(field, modifiable) + ";");
   }

   protected String getEqualsEmptyIfCollection(FieldDoc field, boolean modifiable) {
      if (modifiable) {
         if (collectionTypes.containsKey(field.type().simpleTypeName())) {
            return " = " + collectionTypes.get(field.type().simpleTypeName()) + "()";
         }
         return "";
      } else {
         return fiddleIfCollection(field, " = Immutable%1$s.of()", "");
      }
   }
}
