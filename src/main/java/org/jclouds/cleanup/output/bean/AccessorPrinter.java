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
package org.jclouds.cleanup.output.bean;

import java.io.PrintWriter;
import java.util.Set;

import org.jclouds.cleanup.output.ClassDocPrinter;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

public class AccessorPrinter extends ClassDocPrinter {

   // TODO add GSON and other 'required' flags as needed!
   public static final Set<String> annotationsWithRequiredField = ImmutableSet.of("XmlElement", "XmlAttribute");

   @Override
   public void write(ClassDoc clazz, PrintWriter out) {
      for (FieldDoc field : instanceFields(clazz)) {
         boolean nullable = true;
         for (AnnotationDesc anno : field.annotations()) {
            if (annotationsWithRequiredField.contains(anno.annotationType().simpleTypeName())) {
               for (AnnotationDesc.ElementValuePair evp : anno.elementValues()) {
                  if (Objects.equal(evp.element().name(), "required") &&
                        evp.value().value() == Boolean.TRUE) {
                     nullable = false;
                     break;
                  }
               }
            }
         }

         writeFieldComment(field, out, ImmutableMap.<String, String>of("@return", "the " + field.name() + " property" + (nullable ? " or null if not set" : "") + "."));

         if (nullable) {
            out.println("@Nullable");
         }
         out.println("public " + properTypeName(field) + " " + getAccessorName(field) + "() {");
         out.println("return " + getUnmodifiableCopyIfCollection(field) + ";");
         out.println("}");
         out.println();
      }
   }
}
