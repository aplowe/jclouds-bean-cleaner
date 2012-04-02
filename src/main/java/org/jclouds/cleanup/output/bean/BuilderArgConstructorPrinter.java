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

import org.jclouds.cleanup.output.ClassDocPrinter;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

public class BuilderArgConstructorPrinter extends ClassDocPrinter {

   @Override
   public void write(ClassDoc theClass, PrintWriter out) {
      out.println("protected " + theClass.name() + "(Builder<?> b) {");
      if (extendsSomething(theClass)) {
         out.println("super(b);");
      }
      for (FieldDoc vtree : instanceFields(theClass)) {
         out.println("this." + vtree.name() + " = " +
               fiddleIfCollection(vtree, "Immutable%1$s.copyOf(checkNotNull(b.%2$s, \"%2$s\"))", "b." + vtree.name()) + ";");
      }
      out.println("}");
      out.println();
   }

}
