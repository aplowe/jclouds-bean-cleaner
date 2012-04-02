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

import org.jclouds.cleanup.output.builder.BuilderSetterPrinter;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

public class BeanBuilderPrinter extends ClassDocPrinter {

   @Override
   public void write(ClassDoc theClass, PrintWriter output) {
      if (!theClass.isAbstract()) {
         output.println("public static Builder<?> builder() {");
         output.println("return new ConcreteBuilder();");
         output.println("}");
         output.println();

         output.println("public Builder<?> toBuilder() {");
         output.println("return new ConcreteBuilder().from" + theClass.name() + "(this);");
         output.println("}");
         output.println();
      }

      output.print("public static abstract class Builder<T extends Builder<T>> ");
      if (extendsSomething(theClass)) {
         output.print("extends " + theClass.superclass().name() + ".Builder<T> ");
      }
      output.println("{");

      new InstanceFieldPrinter(false, false, false).write(theClass, output);

      output.println();

      if (!extendsSomething(theClass)) {
         output.println("protected abstract T self();");
         output.println();
      }

      new BuilderSetterPrinter().write(theClass, output);

      if (theClass.isAbstract()) {
         output.println("public abstract " + theClass.name() + " build();");
      } else {
         output.println("public " + theClass.name() + " build() {");
         output.println("return new " + theClass.name() + "(this);");
         output.println("}");
         output.println();
      }

      output.println("public T from" + theClass.name() + "(" + theClass.name() + " in) {");

      if (extendsSomething(theClass)) {
         output.print("return from" + theClass.superclass().name() + "(in)");
      } else {
         output.print("return this");
      }

      for (FieldDoc f : instanceFields(theClass)) {
         output.println();
         output.print("." + f.name() + "(in." + getAccessorName(f) + "())");
      }
      output.println(";");
      output.println("}");
      output.println("}");
      output.println();

      if (!theClass.isAbstract()) {
         output.println("private static class ConcreteBuilder extends Builder<ConcreteBuilder> {");
         output.println("@Override");
         output.println("protected ConcreteBuilder self() {");
         output.println("return this;");
         output.println("}");
         output.println("}");
         output.println();
      }
   }
}

