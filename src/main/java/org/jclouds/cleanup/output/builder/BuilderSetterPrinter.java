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
package org.jclouds.cleanup.output.builder;

import java.io.PrintWriter;

import org.jclouds.cleanup.output.ClassDocPrinter;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

public class BuilderSetterPrinter extends ClassDocPrinter {

   @Override
   public void write(ClassDoc clazz, PrintWriter out) {
      for (FieldDoc var : instanceFields(clazz)) {
         String fieldName = var.name();
         out.println("/**");
         out.println(" * @see " + var.containingClass().name() + "#" + getAccessorName(var) + "()");
         out.println(" **/");
         out.println("public T " + fieldName + "(" + properTypeName(var) + " " + fieldName + ") {");
         out.println("this." + fieldName + " = " + fiddleIfCollection(var, "Immutable%1$s.copyOf(checkNotNull(%2$s, \"%2$s\"));", var.name() + ";"));
         out.println("return self();");
         out.println("}");
         out.println();
      }
   }

}
