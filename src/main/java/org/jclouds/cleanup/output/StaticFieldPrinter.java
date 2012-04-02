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
import java.util.Set;

import com.google.common.collect.Sets;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

/**
 * Extended to generated output per instance field in a class.
 */
public class StaticFieldPrinter extends ClassDocPrinter {

   @Override
   public void write(ClassDoc clazz, PrintWriter out) {
      for (FieldDoc field : classFields(clazz)) {
         writeField(field, out);
      }
      out.println();
   }

   protected void writeField(FieldDoc field, PrintWriter out) {
      writeComment(out, null, field);
      out.print("public static " + (field.isFinal() ? "final " : "") + properTypeName(field) + " " + field.name());
      if (field.constantValueExpression() != null) {
         out.print(" = " + field.constantValueExpression());
      }
      out.println(";");
   }

   protected Set<FieldDoc> classFields(ClassDoc classDoc) {
      Set<FieldDoc> result = Sets.newLinkedHashSet();
      for (FieldDoc field : classDoc.fields()) {
         if (field.isStatic()) {
            result.add(field);
         }
      }
      return result;
   }
}
